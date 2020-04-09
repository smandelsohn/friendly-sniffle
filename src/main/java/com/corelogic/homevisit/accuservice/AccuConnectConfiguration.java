package com.corelogic.homevisit.accuservice;

import com.corelogic.homevisit.accuservice.component.SecurityTokenHolder;
import com.corelogic.homevisit.accuservice.service.AccuConnectJsonService;
import com.corelogic.homevisit.accuservice.service.impl.AccuConnectJsonServiceImpl;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.*;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.jdbc.JdbcMessageHandler;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.corelogic.homevisit.accuservice.service.AccuConnectJsonService.*;

@Configuration
@EnableIntegration
public class AccuConnectConfiguration {

    @Autowired
    private DataSource dataSource;

    @Value("${accu.connect.client.id}")
    private String clientId;

    @Value("${accu.connect.client.password}")
    private String clientSecret;

    @Value("${accu.connect.url.token}")
    private String connectUrlToken;

    @Value("${accu.connect.url.order}")
    private String connectUrlOrder;

    @Value("${corelogic.accuservice.callback.apikey}")
    private String callbackApiKey;

    @Value("${corelogic.accuservice.callback.path}")
    private String callbackApiPath;

    //@Value("${corelogic.accuservice.hvid}")
    //private String hvid;

    @Value("${corelogic.accuservice.aws-bucket-url}")
    private String awsBucketUrl;


    //------------------------------------------- Flow to (re) obtain security token from accu connect --------------

    /**
     * Each 5 minutes check is token expired by timeout and request new if so KISS noo need to go with dynamic poller to
     * not overcomplicated The rest of accu connect http calls must filter flow before call something like .filter( p ->
     * (Boolean.FALSE.equals( securityTokenHolder().isExpired()  ))  )
     */
    @Bean
    public SecurityTokenHolder securityTokenHolder() {
        return new SecurityTokenHolder();
    }

    @Profile("!localtest")
    @Bean
    public IntegrationFlow updateSecurityTokenFlow() {
        return IntegrationFlows.from(securityTokenHolder()::isExpired,
                        c -> c.poller(Pollers.fixedDelay(15, TimeUnit.MINUTES)))
                .filter(p -> (Boolean.TRUE.equals(p)))
                .delay("httpMessage", (DelayerEndpointSpec e) -> e.defaultDelay(300))
                .log(LoggingHandler.Level.INFO, m -> MessageFormat.format("ACCUCONNECT SEC token is expired {0}", securityTokenHolder()))
                .<Boolean, Object>transform(m -> createNewTokenRequestMultipartForm())
                .handle(
                        Http.outboundGateway(connectUrlToken)
                                .httpMethod(HttpMethod.POST)
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .channel("toUpdateSecurityToken")
                .get();
    }

    @Bean
    public PublishSubscribeChannel toUpdateSecurityToken() {
        return MessageChannels.publishSubscribe().get();
    }


    @Profile("!sourcetest")
    @Bean
    @ServiceActivator(inputChannel = "toUpdateSecurityToken")
    public MessageHandler updateSecurityToken() {
        return new AbstractMessageHandler() {
            @Override
            protected void handleMessageInternal(Message<?> message) {
                logger.info(MessageFormat.format("ACCUCONNECT SEC Response with new token is {0}", message.getPayload()) );
                final String json = (String) message.getPayload();
                final JSONParser jp = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
                try {
                    final JSONObject jo = (JSONObject) jp.parse(json);
                    securityTokenHolder().updateToken(
                            jo.get("access_token").toString(),
                            Integer.parseInt(jo.get("expires_in").toString()));
                } catch (ParseException e) {
                    logger.error(MessageFormat.format("ACCUCONNECT SEC Cannot parse {0} error is {1}", message, e));
                }
            }
        };
    }




    //------------------------------------------- End of obtain security token --------------------------------------

    //------------------------------------------- Pickup order to compose json request to print -------------------


    /**
     * Retrieves complete jobs that are ready to be sent to AccuConnect.
     *
     * @return
     */
    @Profile("!localtest")
    @Bean
    public MessageSource<Object> readyForConnectMessageSource() {
        return new JdbcPollingChannelAdapter(dataSource,


        "SELECT cart_id.id                                          AS cartid_id,\n" +
                "       cart_id.id                            AS printRequestId,\n" +
                "       cart_id.aaddress                                    AS billingAddress,\n" +
                "       cart_id.azip                                        AS billingZip,\n" +
                "       cart_id.acity                                        AS billingCity,\n" +
                "       cart_id.astate                                        AS billingState,\n" +
                "       cart_id.azip                                        AS billingZip,\n" +
                "       cart_id.userid                                      AS CREATORUID,\n" +
                "       aPV.print_name                                      AS print_name,\n" +
                "       cart_id.hvId                                        AS hvID,\n" +
                "       (SELECT _ap.mail_class\n" +
                "        FROM   cart _c\n" +
                "                   INNER JOIN item _i\n" +
                "                              ON _i.id = _c.selection\n" +
                "                   INNER JOIN agvproduct _ap\n" +
                "                              ON _i.code = _ap.sku\n" +
                "        WHERE  _c.cartid = cart_id.id\n" +
                "          AND _ap.product_type = 'PRINT_DIRECT_MAIL') AS mailClass,\n" +
                "       (select count(pav.id)\n" +
                "        from printAddressVerified pav\n" +
                "        where pav.print_request_id = cart_id.print_request_id) as add_count,\n" +
                "       cart.chili_print_id                            as cpid,\n" +
                "\n" +
                "       (select _cuPrintPage.printPagePdf\n" +
                "        from chiliUserPrint _cup\n" +
                "                 inner join chiliUserPrintProduction _cupp\n" +
                "                 inner join chiliUserPrintPage _cuPrintPage on _cupp.upID = _cuPrintPage.upID on _cup.id = _cupp.upID\n" +
                "        where _cupp.id = cart.chili_print_id\n" +
                "          AND _cuPrintPage.pageNumber = 1)            as frontVar,\n" +
                "\n" +
                "       (select top (1) _cuPrintPage.printPagePdf\n" +
                "        from chiliUserPrint _cup\n" +
                "                 inner join chiliUserPrintProduction _cupp\n" +
                "                 inner join chiliUserPrintPage _cuPrintPage on _cupp.upID = _cuPrintPage.upID on _cup.id = _cupp.upID\n" +
                "        where _cupp.id = cart.chili_print_id\n" +
                "        ORDER BY _cuPrintPage.pageNumber DESC)        as backVar,\n" +
                "\n" +
                "       (select top (1)  chiliDefPostagePlacement.placement\n" +
                "        from chiliUserPrint cup\n" +
                "                 inner join chiliUserPrintProduction cupp\n" +
                "                 inner join chiliUserPrintPage cuPrintPage on cupp.upID = cuPrintPage.upID on cup.id = cupp.upID\n" +
                "                 left outer join chiliMasterTemplateAltLayout master on master.mtID = cuPrintPage.mtID\n" +
                "                 inner join chiliDefPostagePlacement\n" +
                "                            ON chiliDefPostagePlacement.postagePlacement = master.postagePlacement\n" +
                "        where cupp.id = cart.chili_print_id\n" +
                "          AND (master.name = '[DEFAULT]' or master.itemID = cuPrintPage.selectedLayoutID)\n" +
                "        ORDER BY cuPrintPage.pageNumber DESC)         as postagePlacement\n" +
                "\n" +
                "FROM   cart_id\n" +
                "           INNER JOIN cart\n" +
                "                      ON cart_id.id = cart.cartid\n" +
                "           INNER JOIN item\n" +
                "                      ON cart.selection = item.id\n" +
                "           INNER JOIN printrequest\n" +
                "                      ON cart_id.print_request_id = printrequest.id\n" +
                "           INNER JOIN agvproductvariant aPV\n" +
                "                      ON item.id = aPV.item_id\n" +
                "           INNER JOIN agvproduct aP\n" +
                "                      ON aPV.product_id = aP.id\n" +
                "WHERE  cart_id.complete = 1\n" +
                "  AND item.code LIKE 'HVA%'\n" +
                "  AND item.isprinted = 1\n" +
                "  AND printrequest.acc_status = 'DONE'\n" +
                "ORDER  BY cartid_id DESC"

                );
    }

    @Profile("!localtest")
    @Bean
    public IntegrationFlow readyForConnectFlow() {
        return IntegrationFlows.from(readyForConnectMessageSource(), c -> c.poller(Pollers.fixedDelay(10, TimeUnit.SECONDS)))
                .filter(p -> (Boolean.FALSE.equals(securityTokenHolder().isExpired())))
                .log(LoggingHandler.Level.INFO, p -> "ACCUCONNECT PICKUP token not expired")
                .log(LoggingHandler.Level.INFO, p -> "ACCUCONNECT PICKUP payload has " + p.getPayload().getClass())
                .filter( p -> p instanceof List)
                .log(LoggingHandler.Level.INFO, p -> MessageFormat.format("ACCUCONNECT PICKUP record - {0}",
                        p.getPayload() == null ? -1 : ((List)p.getPayload()).size()))
                .split()
                .channel("toEnrichWithVerifiedAddresses")
                .get();
    }

    @Bean
    public PublishSubscribeChannel toEnrichWithVerifiedAddresses() {
        return MessageChannels.publishSubscribe().get();
    }

    /**
     * Get verified addresses for an outgoing AccuConnect request.
     *
     * @return
     */
    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow enrichWithVerifiedAddressesFlow() {
        return IntegrationFlows.from("toEnrichWithVerifiedAddresses")
                .log(LoggingHandler.Level.INFO, p ->
                        MessageFormat.format("ACCUCONNECT ADDR Enrich with verified addresses cartid_id {0} print request id {1}",
                                ((Map)p.getPayload()).get(AccuConnectJsonService.IN_FIELD_CARTIDID),
                                ((Map)p.getPayload()).get(AccuConnectJsonService.IN_FIELD_PRINTREQUESTID))
                )
                .enrichHeaders(
                        h -> {
                            h.headerExpression("printRequestId", "payload['PRINTREQUESTID']");
                            h.headerExpression("addCount", "payload['ADD_COUNT']");
                            h.headerExpression("name", "payload['PRINT_NAME']");
                            h.headerExpression("mailclassname", "payload['MAILCLASS']");
                        }
                )
                .enrich( e -> {
                    e.requestPayload( p-> {
                        Map m = (Map) p.getPayload();

                        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                                "select first_name, last_name, address, address2, city, state, zip " +
                                        "from printAddressVerified where print_request_id = :printRequestId");
                        jpca.setSelectSqlParameterSource(new MapSqlParameterSource("printRequestId",
                                m.get(AccuConnectJsonService.IN_FIELD_PRINTREQUESTID)));

                        Message<Object> rows = jpca.receive();
                        if (rows == null) {
                            m.put(AccuConnectJsonService.IN_FIELD_ADDRESSES, Collections.EMPTY_LIST);
                        } else {
                            m.put(AccuConnectJsonService.IN_FIELD_ADDRESSES, rows.getPayload());
                        }


                        return p.getPayload();
                    } );
                })
                .log(LoggingHandler.Level.INFO, p ->
                        MessageFormat.format("ACCUCONNECT ADDR Enriched with verifyed addresses cartid_id {0} print request id {1} with address quantity {2}",
                            ((Map)p.getPayload()).get(AccuConnectJsonService.IN_FIELD_CARTIDID),
                            ((Map)p.getPayload()).get(AccuConnectJsonService.IN_FIELD_PRINTREQUESTID),
                            ((List)((Map)p.getPayload()).get(AccuConnectJsonService.IN_FIELD_ADDRESSES)).size())
                )
                .channel("toEnrichWithDial")
                .get();
    }

    @Bean
    public PublishSubscribeChannel toEnrichWithDial() {
        return MessageChannels.publishSubscribe().get();
    }


    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow enrichWithDialFlow() {
        return IntegrationFlows.from("toEnrichWithDial")
                .log(LoggingHandler.Level.INFO, p ->
                        MessageFormat.format("ACCUCONNECT PDF Enrich print request id {0} with PDFs ",
                            ((Map)p.getPayload()).get(AccuConnectJsonService.IN_FIELD_PRINTREQUESTID)  )
                )
                .enrich( e -> {
                    e.requestPayload( p-> {

                        Map m = (Map) p.getPayload();
                        Map<String, String> itemFrontVar = new HashMap<>();
                        itemFrontVar.put(IN_FIELD_NAME, "FrontVar");
                        itemFrontVar.put(IN_FIELD_ISURL, "true");
                        Long hvid = 0L;
                        if (m.get("HVID") != null) {
                            hvid = NumberUtils.toLong(m.get("HVID").toString());
                        }
                        itemFrontVar.put(IN_FIELD_VALUE, composeAwsUrl((String) m.get("FRONTVAR"), hvid));
                        Map<String, String> itemBackVar = new HashMap<>();
                        itemBackVar.put(IN_FIELD_NAME, "BackVar");
                        itemBackVar.put(IN_FIELD_ISURL, "true");
                        itemBackVar.put(IN_FIELD_VALUE, composeAwsUrl((String) m.get("BACKVAR"), hvid));
                        Map<String, String> itemPlacement = new HashMap<>();
                        itemPlacement.put(IN_FIELD_NAME, "Position_Var");
                        itemPlacement.put(IN_FIELD_ISURL, "false");
                        itemPlacement.put(IN_FIELD_VALUE, (String) m.get("POSTAGEPLACEMENT"));
                        List<Map> dials = new ArrayList<Map>();
                        dials.add(itemFrontVar);
                        dials.add(itemBackVar);
                        dials.add(itemPlacement);
                        m.put(AccuConnectJsonService.IN_FIELD_DIALS, dials);

                        return p.getPayload();
                    } );
                })
                .log(LoggingHandler.Level.INFO, p ->
                        MessageFormat.format("ACCUCONNECT PDF Enriched print request {0}", p)
                )
                .route( p -> {
                    boolean firstPageValueNull = ((Map)((List) ((Map) p).get(AccuConnectJsonService.IN_FIELD_DIALS)).get(0)).get(AccuConnectJsonService.IN_FIELD_VALUE) == null;
                    if (firstPageValueNull
                            || CollectionUtils.isEmpty((List)((Map)p).get(AccuConnectJsonService.IN_FIELD_ADDRESSES))) {
                        return "toPrintRequestHasError";
                    } else {
                        return "toRequestPrintJob";
                    }
                } )
                .get();
    }

    @Bean
    public PublishSubscribeChannel toPrintRequestHasError() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel toRequestPrintJob() {
        return MessageChannels.publishSubscribe().get();
    }



    @Profile("!sourcetest")
    @Bean
    public IntegrationFlow submitOrderFlow() {
        return IntegrationFlows.from("toRequestPrintJob")
                .log(LoggingHandler.Level.INFO, p ->
                        MessageFormat.format("ACCUCONNECT REQ print req id {0}",
                            ((Map)p.getPayload()).get(AccuConnectJsonService.IN_FIELD_PRINTREQUESTID))
                )
                .filter(p -> (Boolean.FALSE.equals(securityTokenHolder().isExpired())))
                .<Map, String>transform( p -> accuConnectJsonService().createRequest(p) )
                .log(LoggingHandler.Level.INFO, p -> MessageFormat.format("ACCUCONNECT REQ json is {0}", p))
                .<String, HttpEntity<String>>transform( p -> createPlaceOrderRequestMultipartForm(p, securityTokenHolder().getToken()) )
                .log(LoggingHandler.Level.INFO, p -> MessageFormat.format("ACCUCONNECT REQ http entry is {0}", p))
                .handle(
                        Http.outboundGateway(connectUrlOrder)
                                .httpMethod(HttpMethod.POST)
                                .extractPayload(true)
                                .expectedResponseType(String.class)
                )
                .log(LoggingHandler.Level.INFO, p -> MessageFormat.format("ACCUCONNECT REQ responce is {0}", p))
                .channel("toPrintJobResponse")
                .get();
    }

    @Bean
    public PublishSubscribeChannel toPrintJobResponse() {
        return MessageChannels.publishSubscribe().get();
    }



    @Bean
    public IntegrationFlow processPrintJobRequestFlow() {
        return IntegrationFlows.from("toPrintJobResponse")
                .log(LoggingHandler.Level.INFO, m -> MessageFormat.format("ACCUCONNECT RESP json to parse {0}", m))
                .<String, String>transform( json -> {
                    final JSONParser jp = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
                    try {
                        final JSONObject jo = (JSONObject) jp.parse(json);
                        String messageVal = ObjectUtils
                                .defaultIfNull(jo.getAsString("message"), "null")
                                .trim();
                        return "Success".equalsIgnoreCase(messageVal)
                                ? "PRINTOK" : "PRINTFAIL";
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return "PRINTFAIL";
                    }
                } )
                .enrichHeaders(
                        h -> {
                            h.headerExpression("acc_status", "payload");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> MessageFormat.format("ACCUCONNECT RESP change print request status {0}",m))
                .channel("toUpdatePrintJobRequestStatus")
                .get();
    }

    @Bean
    public IntegrationFlow printRequestHasErrorFlow() {

        return IntegrationFlows.from("toPrintRequestHasError")
                .log(LoggingHandler.Level.INFO, m -> MessageFormat.format("ACCUCONNECT ERR Dials or Address list is empty {0}", m))
                .<Map, String>transform(
                        p -> {
                            if (CollectionUtils.isEmpty((List)((Map)p).get(AccuConnectJsonService.IN_FIELD_ADDRESSES))) {
                                return "ERRNOADDR";
                            } else if (((Map)((List) p.get(AccuConnectJsonService.IN_FIELD_DIALS)).get(0)).get(AccuConnectJsonService.IN_FIELD_VALUE) == null) {
                                return "ERRNOPDF";
                            }
                            return "ERRUNKNOWN";
                        }
                )
                .enrichHeaders(
                        h -> {
                            h.headerExpression("acc_status", "payload");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> MessageFormat.format("ACCUCONNECT ERR change print request status {0}", m))
                .channel("toUpdatePrintJobRequestStatus")
                .get();
    }

    @Bean
    public PublishSubscribeChannel toUpdatePrintJobRequestStatus() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    @ServiceActivator(inputChannel= "toUpdatePrintJobRequestStatus")
    public MessageHandler updatePrintJobRequestStatus() {
        return new JdbcMessageHandler(this.dataSource,
                "update printRequest set acc_status = :headers[acc_status]  where id = :headers[printRequestId] ");
    }

    //-------------------------- Success Callback ------------------------

    /**
     * After submitting jobs to AccuConnect they will be processed in batches. After processing, AccuConnect will POST a
     * request to us with the print jobs that were successfully printed. This endpoint will updated those print jobs
     * to PROCESSED status.
     * TODO: handle multiple print requests in the array?
     */

    @Bean
    public IntegrationFlow inboundSuccessCallbackFlow() {
        return IntegrationFlows.from(Http.inboundChannelAdapter(callbackApiPath)
                .requestMapping(r -> r
                        .methods(HttpMethod.POST)
                        .params("apikey"))
                .requestPayloadType(String.class)
                .headerExpression("apikey", "#requestParams.apikey[0]"))
                .filter(Message.class, m -> callbackApiKey.equals(m.getHeaders().get("apikey")))
                .transform(Transformers.fromJson())
                .enrichHeaders(
                        h -> {
                            h.headerExpression("printRequestId", "payload['batchInfo'][0]['requestId']");
                            h.header("acc_status", "PROCESSED");
                        }
                )
                .log(LoggingHandler.Level.INFO, m -> MessageFormat.format("ACCUCONNECT callback request {0}", m))
                .channel("toUpdatePrintJobRequestStatus")
                .get();
    }

    //-------------------------- regular java beans -----------------------
    @Bean
    public AccuConnectJsonService accuConnectJsonService() {
        return new AccuConnectJsonServiceImpl();
    }

    //---------------------- Small mixins ----------------------

    HttpEntity createPlaceOrderRequestMultipartForm(String json, String bearerToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        return new HttpEntity(json, headers);
    }

    HttpEntity createNewTokenRequestMultipartForm() {
        final MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("grant_type", "client_credentials"); // const
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity(map, headers);
    }

    public String composeAwsUrl(final String printName, final long hvId)  {
        if (StringUtils.isBlank(printName)) {

            return null;

        } else {

            try {
                URL url = new URL(
                        String.format("%s/img/%d/pdf/%s", awsBucketUrl, hvId, printName)
                );
                return  url.toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        }

    }

}
