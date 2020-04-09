package com.corelogic.homevisit.accuservice;

import com.corelogic.homevisit.accuservice.component.SecurityTokenHolder;
import com.corelogic.homevisit.accuservice.service.impl.AccuTestController;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles(profiles = "end2endtest")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT )
@DirtiesContext
public class End2EndTestAccuConnect {

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("toUpdatePrintJobRequestStatus")
    public PublishSubscribeChannel toUpdatePrintJobRequestStatus;

    @Autowired
    @Qualifier("toEnrichWithVerifiedAddresses")
    public PublishSubscribeChannel toEnrichWithVerifiedAddresses;

    @Autowired
    @Qualifier("readyForConnectFlow")
    public IntegrationFlow readyForConnectFlow;

    @Autowired
    public PublishSubscribeChannel toUpdateSecurityToken;

    @Autowired
    public SecurityTokenHolder securityTokenHolder;


    //@Test
    public void testVerifiedAddressesFlow() throws Exception {

        AccuTestController.tokenTtl  = 3600;
        securityTokenHolder.updateToken("JangaBongoMongo", 3600);

        Thread.sleep(10000);


        Map expected = new HashMap() {{
            put(100L, "PRINTOK");
            put(200L, "PRINTOK");
            put(300L, "ERRNOADDR");
            put(400L, "ERRNOPDF");

        }};

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id in (100, 200, 300, 400)");
        List lst = (List) jpca.receive().getPayload();
        assertEquals(4, lst.size());
        lst.forEach(
                i -> {
                    Map map = (Map) i;
                    assertEquals(
                            expected.get(map.get("ID")),
                            map.get("ACC_STATUS"),
                            "Expecting " + expected.get(map.get("ID")) + " for " + map.get("ID"));
                }
        );

        AccuTestController.tokenTtl  = 3;

        String printRequest100Body = AccuTestController.accuConnectOrders.get("100");
        assertTrue(printRequest100Body.contains("user1")); // from config


        final JSONParser jp = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONArray ja = (JSONArray)  jp.parse(printRequest100Body);
        assertEquals(1, ja.size(),"Expected one item only as single request in array of 1 item :)");
        JSONObject jo = (JSONObject) ja.get(0);

        JSONArray joItems = (JSONArray) jp.parse(jo.getAsString("items"));
        assertEquals(1, joItems.size(),"Expected one item only");
        JSONObject item = (JSONObject) joItems.get(0);

        assertEquals("8-5x5-5_UV", item.getAsString("name"));
        assertEquals("First_Class", item.getAsString("mailclassname"));
        //Standard from db but substituted to First_Class assertEquals("Standard", item.getAsString("mailclassname"));
        assertEquals("user1", jo.getAsString("creatoruid"), "Must be eq to configured user from config");
        assertEquals(3, item.getAsNumber("quantity"));

        JSONArray joDials = (JSONArray) jp.parse(item.getAsString("dials"));
        assertEquals(3, joDials.size(),"Expected 3 items only");

        String token = AccuTestController.accuConnectOrdersSecToken.get("100");
        assertNotNull(token);
        assertEquals(token, "Bearer " + securityTokenHolder.getToken());



    }

}
