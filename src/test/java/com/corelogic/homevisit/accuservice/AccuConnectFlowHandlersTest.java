package com.corelogic.homevisit.accuservice;

import com.corelogic.homevisit.accuservice.component.SecurityTokenHolder;
import com.corelogic.homevisit.accuservice.service.AccuConnectJsonService;
import com.corelogic.homevisit.accuservice.service.impl.AccuTestController;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles(profiles = "localtest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT )
@DirtiesContext
public class AccuConnectFlowHandlersTest {

    @Autowired
    @Qualifier("toEnrichWithDial")
    private PublishSubscribeChannel toEnrichWithDial;

    @Autowired
    @Qualifier("toRequestPrintJob")
    private PublishSubscribeChannel toRequestPrintJob;

    @Autowired
    @Qualifier("toPrintRequestHasError")
    public PublishSubscribeChannel toPrintRequestHasError;

    @Autowired
    private PublishSubscribeChannel toEnrichWithVerifiedAddresses;


    @Autowired
    @Qualifier("toPrintJobResponse")
    public PublishSubscribeChannel toPrintJobResponse;

    @Autowired
    public SecurityTokenHolder securityTokenHolder;

    @Test
    public void testEnrichAddresses() {

        final QueueChannel trap = new QueueChannel();
        toEnrichWithDial.addInterceptor(new WireTap(trap));

        Map<String, Object> req = new HashMap<String, Object>() {{
            put("CARTID_ID", 200L);
            put("PRINTREQUESTID", 100L);
            put("BILLINGADDRESS", "4050 Piedmont Parkway");
            put("BILLINGZIP", "27265");
            put("ADD_COUNT", 3L);
            put("FRONTVAR", "f.pdf");
            put("BACKVAR", "b.prf");
            put("POSTAGEPLACEMENT", "LP");
            put("HVID", 1L);
        }};

        toEnrichWithVerifiedAddresses.send(new GenericMessage<>(req));

        Message msg = trap.receive(1000);
        Map body = (Map) msg.getPayload();

        assertEquals(3, ((List)body.get(AccuConnectJsonService.IN_FIELD_ADDRESSES)).size());

        req.put("CARTID_ID", new Long(400));
        req.put("PRINTREQUESTID", new Long(300));

        toEnrichWithVerifiedAddresses.send(new GenericMessage<>(req));

        msg = trap.receive(100);
        body = (Map) msg.getPayload();

        assertEquals(0, ((List)body.get(AccuConnectJsonService.IN_FIELD_ADDRESSES)).size());

        req.put("CARTID_ID", new Long(500));
        req.put("PRINTREQUESTID", new Long(400));

        toEnrichWithVerifiedAddresses.send(new GenericMessage<>(req));

        msg = trap.receive(100);
        body = (Map) msg.getPayload();

        assertEquals(1, ((List)body.get(AccuConnectJsonService.IN_FIELD_ADDRESSES)).size());


    }

    @Test
    public void testSubmitPrintRequest() throws Exception {

        final Map<String, Object> req = new HashMap<String, Object>(){{
            put(AccuConnectJsonService.IN_FIELD_PRINTREQUESTID, "1234-0000-ASDF");
            put(AccuConnectJsonService.IN_FIELD_BILLINGADDRESS, "9050 N Capital 95, CA, San-Diego");
            put(AccuConnectJsonService.IN_FIELD_BILLINGZIP, "12345");
            put(AccuConnectJsonService.IN_FIELD_ITEMNAME, "Moon light");
            put(AccuConnectJsonService.IN_FIELD_ITEMQTY, 42);


            Map<String, Object> addr0 = new HashMap<String, Object>() {{
                put(AccuConnectJsonService.IN_FIELD_FIRST_NAME, "John");
                put(AccuConnectJsonService.IN_FIELD_LAST_NAME, "Dow");
                put(AccuConnectJsonService.IN_FIELD_ADDRESS, "4523 Under the bridge");
                put(AccuConnectJsonService.IN_FIELD_ADDRESS2, "Left corner");
                put(AccuConnectJsonService.IN_FIELD_CITY, "Seattle");
                put(AccuConnectJsonService.IN_FIELD_STATE, "GR");
                put(AccuConnectJsonService.IN_FIELD_ZIP, 1);
            }};

            Map<String, Object> addr1 = new HashMap<String, Object>() {{
                put(AccuConnectJsonService.IN_FIELD_FIRST_NAME, "Jane");
                put(AccuConnectJsonService.IN_FIELD_LAST_NAME, "Dow");
                put(AccuConnectJsonService.IN_FIELD_ADDRESS, "4523 Under the bridge");
                put(AccuConnectJsonService.IN_FIELD_ADDRESS2, "Left corner");
                put(AccuConnectJsonService.IN_FIELD_CITY, "Seattle");
                put(AccuConnectJsonService.IN_FIELD_STATE, "GR");
                put(AccuConnectJsonService.IN_FIELD_ZIP, 2);
            }};

            Map<String, Object> addr2 = new HashMap<String, Object>() {{
                put(AccuConnectJsonService.IN_FIELD_FIRST_NAME, "Mike");
                put(AccuConnectJsonService.IN_FIELD_LAST_NAME, "Dow");
                put(AccuConnectJsonService.IN_FIELD_ADDRESS, "4523 Under the bridge");
                put(AccuConnectJsonService.IN_FIELD_ADDRESS2, "Left corner");
                put(AccuConnectJsonService.IN_FIELD_CITY, "Seattle");
                put(AccuConnectJsonService.IN_FIELD_STATE, "GR");
                put(AccuConnectJsonService.IN_FIELD_ZIP, 3);
            }};


            Map<String, Object> dialItem1 = new HashMap<String, Object>() {{
                put(AccuConnectJsonService.IN_FIELD_NAME, "FrontVar");
                put(AccuConnectJsonService.IN_FIELD_ISURL, "true");
                put(AccuConnectJsonService.IN_FIELD_VALUE, "http://www.yes-cart.org/");

            }};


            put(
                    AccuConnectJsonService.IN_FIELD_ADDRESSES,
                    new ArrayList() {{
                        add(addr0);
                        add(addr1);
                        add(addr2);
                    }}
            );

            put(
                    AccuConnectJsonService.IN_FIELD_DIALS,
                    new ArrayList() {{
                        add(dialItem1);
                    }}
            );

        }};

        securityTokenHolder.updateToken("Whatever. Garbage in - garbage out", 3600);

        final QueueChannel channelTrap = new QueueChannel();
        toPrintJobResponse.addInterceptor(new WireTap(channelTrap));

        toRequestPrintJob.send(new GenericMessage<Map>(req));

        Message<String> json = ( Message<String> ) channelTrap.receive(100);

        assertNotNull(json);


        assertTrue(AccuTestController.accConnectRequestReqBody.contains("1234-0000-ASDF"));
        assertTrue(AccuTestController.accConnectRequestReqBody.contains("yes-cart"));
        final JSONParser jp = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONArray ja = (JSONArray) jp.parse(AccuTestController.accConnectRequestReqBody);
        JSONObject jo = (JSONObject) ja.get(0);

        JSONArray joItems = (JSONArray) jp.parse(jo.getAsString("items"));
        assertEquals(1, joItems.size(),"Expected one item only");

        JSONObject item = (JSONObject) joItems.get(0);

        assertEquals("Moon light", item.getAsString("name"));
        assertEquals(42, item.getAsNumber("quantity"));


    }


}
