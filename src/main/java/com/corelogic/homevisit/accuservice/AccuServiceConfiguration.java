package com.corelogic.homevisit.accuservice;

import com.corelogic.homevisit.accuservice.service.AccuZipCSVService;
import com.corelogic.homevisit.accuservice.service.impl.AccuZipCSVServiceImpl;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.*;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.jdbc.JdbcMessageHandler;
import org.springframework.integration.jdbc.JdbcOutboundGateway;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableIntegration
@EnableIntegrationManagement(
        defaultCountsEnabled = "true",
        defaultStatsEnabled = "true"
)
public class AccuServiceConfiguration {


    private final String STOP = UUID.randomUUID().toString();

    @Autowired
    private DataSource dataSource;

    @Value("${accu.url.accountinfo}")
    private String accountInfoUrl;

    @Value("${accu.url.uploadcsv}")
    private String uploadCsvUrl;

    @Value("${accu.url.cass}")
    private String cassUrl;

    @Value("${accu.url.dedup}")
    private String dedupUrl;

    @Value("${accu.url.quote}")
    private String quoteUrl;

    @Value("${accu.url.downloadsv}")
    private String downloadUrl;

    @Value("${accu.apikey}")
    private String apiKey;


    //--------------------- Pickup print requests for validation flow  ---------
    @Profile("!localtest")
    @Bean
    public MessageSource<Object> printRequestVerificationMessageSource() {
        return new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest e where e.acc_status = 'NEW'");
        //TODO Most propably for last X hours ???
    }

    @Profile("!localtest")
    @Bean
    public IntegrationFlow printRequestVerificationFlow() {
        return IntegrationFlows.from(printRequestVerificationMessageSource(),
                c -> c.poller(Pollers.fixedDelay(5, TimeUnit.SECONDS)))
                .split()
                .<LinkedCaseInsensitiveMap, Number>transform(  m -> (Number) m.get("ID"))
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP CSVUP PrintRequest to validate " + m )
                .channel("toPickupAddressesChannel")
                .get();
    }

    @Bean
    public PublishSubscribeChannel toPickupAddressesChannel() {
        return MessageChannels.publishSubscribe().get();
    }


    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow addressForVerificationIntegrationFlow() {

        return IntegrationFlows.from("toPickupAddressesChannel")
                .enrichHeaders( h -> h.headerExpression("printRequestId", "payload"))
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP CSVUP Collecting addresses for printRequestId " + m )
                .<Number, Object>transform(
                        m -> {
                            JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                                    "select * from printAddress where printRequestId = :printRequestId");
                            jpca.setSelectSqlParameterSource(new MapSqlParameterSource("printRequestId", m));
                            if (jpca.receive() == null || jpca.receive().getPayload() == null) {
                                return STOP;
                            }
                            return jpca.receive().getPayload();
                        }
                )
                .filter( p -> p instanceof List)
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP CSVUP  PrintRequest " + m.getHeaders().get("printRequestId") +
                        " has " + ((List)m.getPayload()).size() + " for validation" )
                .channel("toCsvUploadChannel")
                .get();

    }

    @Bean
    public PublishSubscribeChannel toCsvUploadChannel() {
        return MessageChannels.publishSubscribe().get();
    }


    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow toCsvMessageFlow() {
        return IntegrationFlows.from("toCsvUploadChannel")
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP CSVUP  Uploading printrequest id " + m.getHeaders().get("printRequestId") + " with addresses " + ((List)m.getPayload()).size() + " to validate" )
                .<List, Object>transform(m -> createCvsUploadMultipartForm(accuZipCSVService().createScvFile( m )) )
                .log(LoggingHandler.Level.INFO, message -> "ACCUZIP CSVUP request " + message.getPayload())
                .handle(
                        Http.outboundGateway(uploadCsvUrl)
                                .httpMethod(HttpMethod.POST)
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP CSVUP upload result " + m.getPayload() + " for print request id " + m.getHeaders().get("printRequestId"))
                .filter(p -> extractGuid(p) != null )
                .<String, String>transform( m -> extractGuid(m))
                .enrichHeaders(
                        h -> {
                            h.header("acc_status", "CHECKUP");
                            h.headerExpression("acc_uuid", "payload");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP CSVUP Change print request status to CHECKUP from NEW " + m )
                .channel("toUpdatePrintRequestStatus")
                .get();
    }

    // ------------------ End of csv upload flow -------------

    //-------------------- Check upload

    @Profile("!localtest")
    @Bean
    public MessageSource<Object> checkUploadMessageSource() {
        return new JdbcPollingChannelAdapter(dataSource,
                "select id, acc_uuid from printRequest where acc_status = 'CHECKUP' and acc_uuid is not null");
    }

    @Profile("!localtest")
    @Bean
    public IntegrationFlow checkUploadFlow() {
        return IntegrationFlows.from(checkUploadMessageSource(),
                c -> c.poller(Pollers.fixedDelay(5, TimeUnit.SECONDS).maxMessagesPerPoll(1)))
                .split()
                .log(LoggingHandler.Level.INFO, m ->
                        "ACCUZIP CHECKUP Request for printRequest id " + ((Map) m.getPayload()).get("ID")
                                + " accu guid " + ((Map) m.getPayload()).get("ACC_UUID")  )
                .channel("checkUploadMessageChannel")
                .get();
    }

    @Bean
    public PublishSubscribeChannel checkUploadMessageChannel() {
        return MessageChannels.publishSubscribe().get();
    }


    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow checkUploadMessageFlow() {
        return IntegrationFlows.from("checkUploadMessageChannel")
                .enrichHeaders(
                        h -> {
                            h.headerExpression("acc_uuid", "payload['ACC_UUID']");
                            h.headerExpression("printRequestId", "payload['ID']");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP CHECKUP Request  printRequest id " + m  )
                .handle(
                        Http.outboundGateway(quoteUrl)
                                .httpMethod(HttpMethod.GET)
                                .uriVariable("guid", m -> m.getHeaders().get("acc_uuid"))
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP CHECKUP Quote result " + m.getPayload())
                .transform( p -> extractStatusTotalRec(p))
                .filter(p -> Boolean.TRUE.equals(  ((Map)p).get("status")  ))
                .enrichHeaders(
                        h -> {
                            h.header("acc_status", "PRECSVUP");
                            h.headerExpression("records", "payload['records']");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP CHECKUP Quote for print request " + m.getHeaders().get("printRequestId")
                        + " with uuid " + m.getHeaders().get("acc_uuid") + " ok, So will change status to READY")
                .channel("toUpdatePrintRequestStatusCounter")
                .get();
    }

    //-------------------- End check upload

    // ------------------- Get quote + update quote to pass additional parameters ----------------------
    /*Update Quote
     Use this web service call to update the object returned with the “Quote”,
     e.g., Class of Mail, Piece Size, etc…  It is necessary to make a call to this web service before
     calling the Presort web service.  Otherwise, the Presort web service will not know what Class of Mail
     or Piece Size you want to use for the presort
     */

    // From PRECSVUP  to CSVUP


    @Profile("!localtest")
    @Bean
    public MessageSource<Object> updateQuoteMessageSource() {
        return new JdbcPollingChannelAdapter(dataSource,
                "select id, acc_uuid from printRequest where acc_status = 'PRECSVUP' and acc_uuid is not null");
    }

    @Profile("!localtest")
    @Bean
    public IntegrationFlow updateQuoteFlow() {
        return IntegrationFlows.from(updateQuoteMessageSource(),
                c -> c.poller(Pollers.fixedDelay(5, TimeUnit.SECONDS)))
                .log(LoggingHandler.Level.INFO, m ->
                        "ACCUZIP UPQUOTE All records in PRECSVUP " + m   )
                .split()
                .log(LoggingHandler.Level.INFO, m ->
                        "ACCUZIP UPQUOTE Request for printRequest id " + ((Map) m.getPayload()).get("ID")
                                + " accu guid " + ((Map) m.getPayload()).get("ACC_UUID")  )
                .channel("updateQuoteMessageChannel")
                .get();
    }

    @Bean
    public PublishSubscribeChannel updateQuoteMessageChannel() {
        return MessageChannels.publishSubscribe().get();
    }


    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow updateQuoteMessageFlow() {
        return IntegrationFlows.from("updateQuoteMessageChannel")
                .enrichHeaders(
                        h -> {
                            h.headerExpression("acc_uuid", "payload['ACC_UUID']");
                            h.headerExpression("printRequestId", "payload['ID']");
                        }
                )
                .transform(m -> {
                    return "{\n" +
                            "  \"filter_cass_02\": \"1\",\n" +
                            "  \"filter_cass_03\": \"1\",\n" +
                            "  \"filter_cass_04\": \"1\",\n" +
                            "  \"filter_cass_05\": \"1\",\n" +
                            "  \"filter_cass_06\": \"1\",\n" +
                            "  \"filter_cass_07\": \"1\",\n" +
                            "  \"filter_cass_08\": \"1\",\n" +
                            "  \"filter_cass_09\": \"1\",\n" +
                            "  \"filter_cass_10\": \"1\"\n" +
                            "}";
                })
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP UPQUOTE Request  printRequest id " + m  )
                .handle(
                        Http.outboundGateway(quoteUrl)
                                .httpMethod(HttpMethod.PUT)
                                .uriVariable("guid", m -> m.getHeaders().get("acc_uuid"))
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP UPQUOTE Quote result " + m.getPayload())
                // not a json, just 200 as ok
                .<ResponseEntity, Integer> transform(
                        m -> {
                            return new Integer(m.getStatusCode().value());
                        }
                )
                .enrichHeaders(
                        h -> {
                            h.headerExpression("acc_status", "payload == 200 ? 'CSVUP' :'ERRUPQUOTE' ");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP UPQUOTE Quote for print request " + m.getHeaders().get("printRequestId")
                        + " with uuid " + m.getHeaders().get("acc_uuid") + " ok, So will change status to " + m.getHeaders().get("acc_status"))
                .channel("toUpdatePrintRequestStatus")
                .get();
    }

    // ------------------- END Get quote + update quote to pass additional parameters ------------------

    //------------------------ Print requests for cass and dedup jobs --------------

    @Profile("!localtest")
    @Bean
    public MessageSource<Object> printRequestCsvJobMessageSource() {
        return new JdbcPollingChannelAdapter(dataSource,
                "select id, acc_uuid from printRequest where acc_status = 'CSVUP' and acc_uuid is not null");
    }

    @Profile("!localtest")
    @Bean
    public IntegrationFlow printRequestCsvJobVerificationFlow() {
        return IntegrationFlows.from(printRequestCsvJobMessageSource(),
                c -> c.poller(Pollers.fixedDelay(5, TimeUnit.SECONDS).maxMessagesPerPoll(1)))
                .split()
                .log(LoggingHandler.Level.INFO, m ->
                        "ACCUZIP REQ Request cass and dedup for printRequest id " + ((Map) m.getPayload()).get("ID")
                                + " accu guid " + ((Map) m.getPayload()).get("ACC_UUID")  )
                .channel("printRequestCsvJobMessageChannel")
                .get();
    }

    @Bean
    public PublishSubscribeChannel printRequestCsvJobMessageChannel() {
        return MessageChannels.publishSubscribe().get();
    }


    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow printRequestCsvJobMessageFlow() {
        return IntegrationFlows.from("printRequestCsvJobMessageChannel")
                .enrichHeaders(
                        h -> {
                            h.headerExpression("acc_uuid", "payload['ACC_UUID']");
                            h.headerExpression("printRequestId", "payload['ID']");
                        }
                )
                .log(LoggingHandler.Level.INFO, m ->
                        "ACCUZIP REQ Request cass and dedup for printRequest id " + m  )
                .handle(
                        Http.outboundGateway(cassUrl)
                                .httpMethod(HttpMethod.GET)
                                .uriVariable("guid", m -> m.getHeaders().get("acc_uuid"))
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP REQ Cass job request result " + m.getPayload())
                .transform( p -> extractStatus(p))
                .filter(p -> Boolean.TRUE.equals(p))//
                .handle(
                        Http.outboundGateway(dedupUrl)
                                .httpMethod(HttpMethod.GET)
                                .uriVariable("guid", m -> m.getHeaders().get("acc_uuid"))
                                .uriVariable("type","'01'")
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP REQ Dedup job request result " + m.getPayload())
                .transform( p -> extractStatus(p))
                .filter(p -> Boolean.TRUE.equals(p))
                .enrichHeaders(
                        h -> {
                            h.header("acc_status", "REQ");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP REQ Jobs requested for " + m + " changing status to REQ")
                .channel("toUpdatePrintRequestStatus")
                .get();
    }

    //-------------- end requests jobs

    //--------------- Quote flow. Used to ask job status instead of callback url
    @Profile("!localtest")
    @Bean
    public MessageSource<Object> printRequestQuoteMessageSource() {
        return new JdbcPollingChannelAdapter(dataSource,
                "select id, acc_uuid from printRequest where acc_status = 'REQ' and acc_uuid is not null");
    }

    @Profile("!localtest")
    @Bean
    public IntegrationFlow printRequestQuoteFlow() {
        return IntegrationFlows.from(printRequestQuoteMessageSource(),
                c -> c.poller(Pollers.fixedDelay(10, TimeUnit.SECONDS).maxMessagesPerPoll(1)))
                .split()
                .log(LoggingHandler.Level.INFO, m ->
                        "ACCUZIP QUOTE Request for printRequest id " + ((Map) m.getPayload()).get("ID")
                                + " accu guid " + ((Map) m.getPayload()).get("ACC_UUID")  )
                .channel("printRequestQuoteMessageChannel")
                .get();
    }

    @Bean
    public PublishSubscribeChannel printRequestQuoteMessageChannel() {
        return MessageChannels.publishSubscribe().get();
    }


    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow printRequestQuoteMessageFlow() {
        return IntegrationFlows.from("printRequestQuoteMessageChannel")
                .enrichHeaders(
                        h -> {
                            h.headerExpression("acc_uuid", "payload['ACC_UUID']");
                            h.headerExpression("printRequestId", "payload['ID']");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP QUOTE Request  printRequest id " + m  )
                .handle(
                        Http.outboundGateway(quoteUrl)
                                .httpMethod(HttpMethod.GET)
                                .uriVariable("guid", m -> m.getHeaders().get("acc_uuid"))
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP QUOTE Quote result " + m.getPayload())
                .transform( p -> extractStatusTotalRec(p))
                .filter(p -> Boolean.TRUE.equals(  ((Map)p).get("status")  ))
                .enrichHeaders(
                        h -> {
                            h.header("acc_status", "READY");
                            h.headerExpression("records", "payload['records']");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP QUOTE Quote for print request " + m.getHeaders().get("printRequestId")
                        + " with uuid " + m.getHeaders().get("acc_uuid") + " ok, So will change status to READY")
                .channel("toUpdatePrintRequestStatusCounter")
                .get();
    }
    //------------------ End quote flow ---------------
    //------------------ Download scv file flow ----------

    /**
     * Need to check is download ready and is order is related complete.
     * TODO
     * @return
     */
    @Profile("!localtest")
    @Bean
    public MessageSource<Object> printRequestReadyMessageSource() {
        return new JdbcPollingChannelAdapter(dataSource,
                "select p.id, p.acc_uuid from printRequest p, cart_id c " +
                        "where c.print_request_id = p.id " +
                        "and c.complete = 1 " +
                        "and p.acc_status = 'READY' and p.acc_uuid is not null");
    }

    @Profile("!localtest")
    @Bean
    public IntegrationFlow printRequestReadyFlow() {
        return IntegrationFlows.from(printRequestReadyMessageSource(),
                c -> c.poller(Pollers.fixedDelay(30, TimeUnit.SECONDS).maxMessagesPerPoll(1)))
                .split()
                .log(LoggingHandler.Level.INFO, m ->
                        "ACCUZIP READY  PR id " + ((Map) m.getPayload()).get("ID")
                                + " accu guid " + ((Map) m.getPayload()).get("ACC_UUID")  )
                .channel("printRequestReadyMessageChannel")
                .get();
    }

    @Bean
    public PublishSubscribeChannel printRequestReadyMessageChannel() {
        return MessageChannels.publishSubscribe().get();
    }


    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow printRequestReadyMessageFlow() {
        return IntegrationFlows.from("printRequestReadyMessageChannel")
                .enrichHeaders(
                        h -> {
                            h.headerExpression("acc_uuid", "payload['ACC_UUID']");
                            h.headerExpression("printRequestId", "payload['ID']");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP READY Deleting previously verified addresses if any printRequest id " + m  )
                .handle(
                        new JdbcOutboundGateway(dataSource,"delete from printAddressVerified where print_request_id = :headers[printRequestId]")
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP READY Downloading printRequest id " + m  )
                .handle(
                        Http.outboundGateway(downloadUrl)
                                .httpMethod(HttpMethod.GET)
                                .uriVariable("guid", m -> m.getHeaders().get("acc_uuid"))
                                .uriVariable("ftype", "'csv'")
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP READY Downloaded file scv " + m.getPayload()
                        + " with size " + String.valueOf(m.getPayload()).length() + " bytes")
                .<String, List<Map<String, String>>>transform(csv -> accuZipCSVService().parseScvFile(csv))
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP READY Downloaded csv for printRequest id " + m.getHeaders().get("printRequestId")
                        + " has " + ((List)m.getPayload()).size()  + " records ")
                .handle(
                        new JdbcOutboundGateway(dataSource, "insert into printAddressVerified "
                                + "(address, address2, barcode, city, " +
                                "cont_id, crrt, endorse, first_name, " +
                                "gpb_id, imbarcode, imbdigits, last_name, " +
                                "middle_name, print_request_id, sal, sequenc, " +
                                "state, zip) values "
                                + "(:payload[ADDRESS], :payload[ADDRESS2], :payload[BARCODE], :payload[CITY], " +
                                ":payload[CONT_ID], :payload[CRRT], :payload[ENDORSE], :payload[FIRST], " +
                                ":payload[GPB_ID], :payload[IMBARCODE], :payload[IMBDITITS], :payload[LAST], " +
                                ":payload[MIDDLE], :headers[printRequestId], :payload[SAL], :payload[SEQUENCE], " +
                                ":payload[ST], :payload[ZIP])"
                        )
                )
                .log(LoggingHandler.Level.INFO, m-> "ACCUZIP READY verifyed addresses inserted for " + m.getHeaders().get("printRequestId"))
                .enrichHeaders(
                        h -> {
                            h.header("acc_status", "DONE");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> "ACCUZIP READY download is ok, change status to DONE for pr " + m.getHeaders().get("printRequestId")
                        + " with uuid " + m.getHeaders().get("acc_uuid") )
                .channel("toUpdatePrintRequestStatus")
                .get();
    }
    //------------------ End download  ----------


    // ------------------ Just ask for subscription level flow


    /**
     * Just get account info from accu ones per day
     * @return
     */
    @Bean
    public IntegrationFlow accountInfo() {
        return IntegrationFlows
                .from(() -> (MessageSource)new GenericMessage<>(accountInfoBody()), e -> e.poller(p -> p.cron("0 0 12 * * *")))
                .log(LoggingHandler.Level.INFO, message -> "ACCUZIP ACCZIPLEVEL Ask accu zip for level with " + message.getPayload())
                .handle(
                        Http.outboundGateway(accountInfoUrl)
                                .httpMethod(HttpMethod.POST)
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .log(LoggingHandler.Level.INFO, message -> "ACCUZIP ACCZIPLEVEL Back from accu zip " + message.getPayload())
                .get();
    }

    // ------------------ End of accu zip level flow -----------------------------------

    //----------------- Common used part to update print request status ---------------

    @Bean
    public PublishSubscribeChannel toUpdatePrintRequestStatusCounter() {
        return MessageChannels.publishSubscribe().get();
    }


    /**
     * The value we care about is dq_dpvhsa_y
     * and not TOTAL records.  We need to update status at each interval but not records since it gets
     * reset to the original list count.
     * So if records IS null we will not update the count.
     * @return
     */

    @Bean
    @ServiceActivator(inputChannel= "toUpdatePrintRequestStatusCounter")
    // I don't know how to access the headers object in java code, so I am just performing the logic inside of the query.
    public MessageHandler updatePrintRequestStatusCounter() {        return new JdbcMessageHandler(this.dataSource,
                "update printRequest set acc_status = :headers[acc_status] , acc_uuid = :headers[acc_uuid], " +
                        " acc_count = ISNULL(:headers[records],acc_count) " +
                        " where id = :headers[printRequestId] ");
    }

    @Bean
    public PublishSubscribeChannel toUpdatePrintRequestStatus() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    @ServiceActivator(inputChannel= "toUpdatePrintRequestStatus")
    public MessageHandler updatePrintRequestStatus() {
        return new JdbcMessageHandler(this.dataSource,
                "update printRequest set acc_status = :headers[acc_status] , acc_uuid = :headers[acc_uuid]" +
                        " where id = :headers[printRequestId] ");
    }

    //---------------------- Regular spring part - services, components, etc ------------------------

    @Bean
    public AccuZipCSVService accuZipCSVService() {
        return new AccuZipCSVServiceImpl();
    }

    //---------------------- Small mixins ----------------------

    Boolean extractStatus(Object obj) {
        return (Boolean) extract(obj, "success");
    }

    /**
     * The vvalue that we need for exstract Status and Total Rec only works on the first request to
     * Quote.  Once this happens it only return the original list of total recs.
     * This product is @#$@#%@%$##%
     * @param obj
     * @return
     */
    Map extractStatusTotalRec(Object obj) {
        Map tm = new TreeMap();
        Boolean status = (Boolean) extract(obj, "success");
        Integer cnt = null;
        if (status) {

            String totalRec = extract(obj, "dq_dpvhsa_y") != null ?
                    String.valueOf(extract(obj, "dq_dpvhsa_y")).replace(",", "") : null;
            JSONArray dups = (JSONArray)extract(obj,"Duplicates");
            String dupFound = null;
            if(dups!=null && dups.get(0)!=null) {
                dupFound =  ((JSONObject)dups.get(0)).getAsString("found");
            }

            dupFound = dupFound != null ?
                            String.valueOf(dupFound).replace(",", "") : null;
            if(totalRec!=null) {
                cnt = NumberUtils.toInt(totalRec);
            }
            if(cnt != null && dupFound != null) {
                cnt = cnt - NumberUtils.toInt(dupFound);
            }
        }
        tm.put("status", status); //Boolean
        tm.put("records", cnt); //Integer
        return tm;
    }

    String extractGuid(Object obj) {
        return (String) extract(obj, "guid");
    }

    Object extract(Object obj,String extract) {
        final String json = (String) obj;
        final JSONParser jp = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        try {
            final JSONObject jo = (JSONObject) jp.parse(json);
            return jo.get(extract);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    String accountInfoBody() {
        final JSONObject reqBody = new JSONObject();
        reqBody.appendField("apiKey", apiKey);
        return reqBody.toString();
    }

    HttpEntity createCvsUploadMultipartForm(String csvData) {
        final byte [] data = csvData.getBytes(StandardCharsets.UTF_8);
        final ByteArrayResource contentsAsResource = new ByteArrayResource(data){
            final String fileName = UUID.randomUUID().toString() + ".csv";
            @Override
            public String getFilename(){
                return fileName;
            }
        };
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new HttpEntity(createMultipartFormParam(contentsAsResource), headers);
    }

    MultiValueMap<String, Object> createMultipartFormParam(final ByteArrayResource contentsAsResource) {
        final MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("backOfficeOption", "json");
        map.add("apiKey", apiKey);
        map.add("callbackURL", "");
        map.add("guid", "");
        map.add("file", contentsAsResource);
        map.add("dataQualityResults_Dups_01","true");
        return map;
    }

}
