package com.corelogic.homevisit.accuservice;

import com.corelogic.homevisit.accuservice.service.impl.AccuTestController;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = "localtest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class AccuZipFlowHandlersTest {

    @Autowired
    private DataSource dataSource;

    @Value("${accu.apikey}")
    private String apiKey;

    @Autowired
    @Qualifier("toUpdatePrintRequestStatus")
    public PublishSubscribeChannel toUpdatePrintRequestStatus;

    @Autowired
    @Qualifier("toPickupAddressesChannel")
    public PublishSubscribeChannel toPickupAddressesChannel;

    @Autowired
    @Qualifier("updateQuoteMessageChannel")
    public PublishSubscribeChannel updateQuoteMessageChannel;

    @Autowired
    @Qualifier("checkUploadMessageChannel")
    public PublishSubscribeChannel checkUploadMessageChannel;

    @Autowired
    @Qualifier("printRequestCsvJobMessageChannel")
    public PublishSubscribeChannel printRequestCsvJobMessageChannel;

    @Autowired
    @Qualifier("printRequestQuoteMessageChannel")
    public PublishSubscribeChannel printRequestQuoteMessageChannel;

    @Autowired
    @Qualifier("printRequestReadyMessageChannel")
    public PublishSubscribeChannel printRequestReadyMessageChannel;

    @Test
    public void changePrintRequestStatusToCheckUpload() throws Exception {

        Message<Long> initFlow = new GenericMessage<>(new Long(8L));
        toPickupAddressesChannel.send(initFlow);

        assertTrue(AccuTestController.uploadCsvReqBody.contains("Dizel"),
                "So csv is uploaded and Vin Dizel must be in file, because he present in DB");
        assertTrue(AccuTestController.uploadCsvReqBody.contains(apiKey)); // part of guid
        assertTrue(AccuTestController.uploadCsvReqBody.contains(".csv")); // some uuid file name, ends with csv
        assertTrue(AccuTestController.uploadCsvReqBody.contains("manual_submit=false"));

        final JSONParser jp = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONObject jo = (JSONObject)  jp.parse(AccuTestController.uploadCsvReqBody);
        String fileContent = jo.getAsString("fileContent");
        assertTrue(fileContent.contains("\"Empty\",\"\",\"\",\"\",\"\",\"\",\"\""));


        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 8");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("CHECKUP", map.get("ACC_STATUS"));
        assertEquals("9999-8888-7777-6666", map.get("ACC_UUID"));
    }

    @Test
    public void changePrintRequestStatusToPrescvup() throws InterruptedException {

        Map<String, Object> req = new HashMap() {{
            put("ID", new Long(8));
            put("ACC_UUID", "5555-sdfg-8888-sdfg");
        }};

        Message<Map> initFlow = new GenericMessage<Map>(req);
        checkUploadMessageChannel.send(initFlow);

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 8");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("PRECSVUP", map.get("ACC_STATUS"));
        assertEquals("5555-sdfg-8888-sdfg", map.get("ACC_UUID"));
    }

    @Test
    public void changePrintRequestStatusToCsvup() throws InterruptedException {

        Map<String, Object> req = new HashMap() {{
            put("ID", new Long(8));
            put("ACC_UUID", "sdfg-sdfg-sdfg-sdfg");
        }};

        Message<Map> initFlow = new GenericMessage<Map>(req);
        updateQuoteMessageChannel.send(initFlow);

        assertTrue(AccuTestController.upQuote.contains("filter_cass_07"));
        assertTrue(AccuTestController.upQuote.contains("filter_cass_04"));

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 8");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("CSVUP", map.get("ACC_STATUS"));
        assertEquals("sdfg-sdfg-sdfg-sdfg", map.get("ACC_UUID"));
    }

    @Test
    public void changePrintRequestStatusToReq() throws InterruptedException {
        Map<String, Object> req = new HashMap() {{
            put("ID", new Long(10));
            put("ACC_UUID", "new-123-123-123");
        }};

        Message<Object> initFlow = new GenericMessage<>(req);
        printRequestCsvJobMessageChannel.send(initFlow);

        assertTrue(AccuTestController.dedupJobReqBody.contains("new-123-123-123 01")); // issued guid and deduplication type

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 10");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("REQ", map.get("ACC_STATUS"));
        assertEquals("new-123-123-123", map.get("ACC_UUID"));
    }


    @Test
    public void changePrintRequestStatusToReady() throws InterruptedException {
        Map<String, Object> req = new HashMap() {{
            put("ID", new Long(11));
            put("ACC_UUID", "ASDF-QWER-ZXVV-1234");
        }};

        Message<Object> initFlow = new GenericMessage<>(req);
        printRequestQuoteMessageChannel.send(initFlow);

        assertTrue(AccuTestController.quoteReqBody.contains("ASDF-QWER-ZXVV-1234")); // issued by accu guid

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 11");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("READY", map.get("ACC_STATUS"));
        assertEquals("ASDF-QWER-ZXVV-1234", map.get("ACC_UUID"));
        assertEquals(2001, map.get("ACC_COUNT"));
    }

    @Test
    public void changePrintRequestStatusToReadyNegative() throws InterruptedException {
        Map<String, Object> req = new HashMap() {{
            put("ID", new Long(12));
            put("ACC_UUID", "1212-QWER-ZXVV-1234");
        }};

        Message<Object> initFlow = new GenericMessage<>(req);
        printRequestQuoteMessageChannel.send(initFlow);

        assertTrue(AccuTestController.quoteReqBody.contains("1212-QWER-ZXVV-1234")); // issued by accu guid

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 12");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("REQ", map.get("ACC_STATUS"), "Must be without any changes, because mock server will return false status");
        assertEquals("1212-QWER-ZXVV-1234", map.get("ACC_UUID"));
        assertEquals(null, map.get("ACC_COUNT"));
    }

    /**
     * Discovered that the total_records can be empty. See test controller for details.
     * Request BAD-NUM-ZXVV-1234
     * @throws InterruptedException
     */
    @Test
    public void changePrintRequestStatusToReadyWithEmptyTotalRecords() throws InterruptedException {
        Map<String, Object> req = new HashMap() {{
            put("ID", new Long(1012));
            put("ACC_UUID", "BAD-NUM-ZXVV-1234");
        }};

        Message<Object> initFlow = new GenericMessage<>(req);
        printRequestQuoteMessageChannel.send(initFlow);

        assertTrue(AccuTestController.quoteReqBody.contains("BAD-NUM-ZXVV-1234")); // issued by accu guid

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 1012");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("READY", map.get("ACC_STATUS"), "READY status changes, because mock server will return true status");
        assertEquals("BAD-NUM-ZXVV-1234", map.get("ACC_UUID"));
        assertEquals(null, map.get("ACC_COUNT"));
    }



    @Test
    public void changePrintRequestStatusToDoneAndDownloadedAddresses() throws InterruptedException {

        Map<String, Object> req = new HashMap() {{
            put("ID", new Long(15));
            put("ACC_UUID", "5555-EDDF-DEAD-BEEF");
        }};

        Message<Map> initFlow = new GenericMessage<>(req);
        printRequestReadyMessageChannel.send(initFlow);

        assertTrue(AccuTestController.downloadReqBody.contains("5555-EDDF-DEAD-BEEF csv"),
                "Going to download csv with 5555-EDDF-DEAD-BEEF guid");

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 15");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("DONE", map.get("ACC_STATUS"));
        assertEquals("5555-EDDF-DEAD-BEEF", map.get("ACC_UUID"));


        jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printAddressVerified where print_request_id = 15 order by id");
        checkRez = jpca.receive();
        lst = (List) checkRez.getPayload();
        assertEquals(2, lst.size());

        map = (Map) lst.get(0);
        assertEquals("Addr11 5555-EDDF-DEAD-BEEF", map.get("ADDRESS"));
        assertEquals("Addr12", map.get("ADDRESS2"));
        assertEquals("/0113241234123450/", map.get("BARCODE"));
        assertEquals("Greenfield", map.get("CITY"));
        //assertEquals("1", map.get("CONT_ID"));
        assertEquals("123crrt", map.get("CRRT"));
        //assertEquals("*************SCH 3-DIGITS 015", map.get("ENDORSE"));
        assertEquals("Fat", map.get("FIRST_NAME"));
        assertEquals("Dazy", map.get("LAST_NAME"));
        assertEquals(15L, map.get("PRINT_REQUEST_ID"));
        assertEquals("Mr", map.get("SAL"));
        assertEquals("MA", map.get("STATE"));
        assertEquals("01301", map.get("ZIP"));

        map = (Map) lst.get(1);
        assertEquals("Addr21", map.get("ADDRESS"), "Second record must distinguish from first");

    }


    /**
     * Test to be sure, that we are cleaning up previously verified addresses  if any.
     */
    @Test
    public void printRequestReadyMessageFlowTestCleanupVerifiedAddresses() {
        Map<String, Object> req = new HashMap() {{
            put("ID", new Long(20));
            put("ACC_UUID", "CAFE-BABE-CAFE-BABE");
        }};

        Message<Map> initFlow = new GenericMessage<>(req);
        printRequestReadyMessageChannel.send(initFlow);

        assertTrue(AccuTestController.downloadReqBody.contains("CAFE-BABE-CAFE-BABE csv"),
                "Going to download csv with CAFE-BABE-CAFE-BABE guid");


        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printAddressVerified where print_request_id = 20");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        assertEquals(1, lst.size());
        Map map = (Map) lst.get(0);

        //
        printRequestReadyMessageChannel.send(initFlow);
        jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printAddressVerified where print_request_id = 20");
        checkRez = jpca.receive();
        lst = (List) checkRez.getPayload();
        assertEquals(1, lst.size());

    }



}
