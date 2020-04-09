package com.corelogic.homevisit.accuservice.service.impl;

import com.corelogic.homevisit.accuservice.service.AccuConnectJsonService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@ActiveProfiles(profiles = "localtest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class AccuConnectJsonServiceImplTest {

    @Autowired
    @Qualifier("accuConnectJsonServiceImpl")
    private AccuConnectJsonServiceImpl service;

    private final Map<String, Object> req = new HashMap<String, Object>() {{
        put(AccuConnectJsonService.IN_FIELD_PRINTREQUESTID, "1234-0000-ASDF");
        put(AccuConnectJsonService.IN_FIELD_BILLINGADDRESS, "9050 N Capital 95, CA, San-Diego");
        put(AccuConnectJsonService.IN_FIELD_BILLINGADDRESS, "9050 N Capital 95, CA, San-Diego");
        put(AccuConnectJsonService.IN_FIELD_BILLINGCITY, "San-Diego");
        put(AccuConnectJsonService.IN_FIELD_BILLINGSTATE, "CA");
        put(AccuConnectJsonService.IN_FIELD_BILLINGZIP, "12345");
        put(AccuConnectJsonService.IN_FIELD_ITEMNAME, "Moon light");
        put(AccuConnectJsonService.IN_FIELD_ITEMQTY, 42);
        put(AccuConnectJsonService.IN_FIELD_MAILCLASSNAME, "Abracadabra");


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

        Map<String, Object> addr3 = new HashMap<String, Object>() {{
            put(AccuConnectJsonService.IN_FIELD_FIRST_NAME, "");
            put(AccuConnectJsonService.IN_FIELD_LAST_NAME, null);
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
                    add(addr3);
                }}
        );

        put(
                AccuConnectJsonService.IN_FIELD_DIALS,
                new ArrayList() {{
                    add(dialItem1);
                }}
        );

    }};

    @Test
    public void createDials() {
        List<JSONObject> dials = service.createDials(req);
        assertEquals(1, dials.size());
        JSONObject dial = dials.get(0);
        assertEquals("FrontVar", dial.getAsString(AccuConnectJsonService.FIELD_NAME));
        assertEquals("true", dial.getAsString(AccuConnectJsonService.FIELD_ISURL));
        assertEquals("http://www.yes-cart.org/", dial.getAsString(AccuConnectJsonService.FIELD_VALUE));

    }


    @Test
    public void createJsonRequest() {
        JSONArray arr = service.createJsonRequest(req);
        JSONObject rez = (JSONObject) arr.get(0);
        assertEquals("user1", rez.getAsString(AccuConnectJsonService.FIELD_CREATORUID));
        assertEquals("1234-0000-ASDF", rez.getAsString(AccuConnectJsonService.FIELD_EXTORDERREF));
        assertEquals(4, ((List) rez.get(AccuConnectJsonService.FIELD_BILLINGADDRESS)).size());

    }

    @Test
    public void createBillingAddress() {
        List<JSONObject> addr = service.createBillingAddress(req);
        assertEquals(4, addr.size());
        JSONObject kv0 = addr.get(0);
        assertEquals("Address1", kv0.getAsString(AccuConnectJsonService.FIELD_NAME));
        assertEquals("9050 N Capital 95, CA, San-Diego", kv0.getAsString(AccuConnectJsonService.FIELD_VALUE));

        JSONObject kv1 = addr.get(1);
        assertEquals("Zip", kv1.getAsString(AccuConnectJsonService.FIELD_NAME));
        assertEquals("12345", kv1.getAsString(AccuConnectJsonService.FIELD_VALUE));

        JSONObject kv2 = addr.get(2);
        assertEquals("State", kv2.getAsString(AccuConnectJsonService.FIELD_NAME));
        assertEquals("CA", kv2.getAsString(AccuConnectJsonService.FIELD_VALUE));

        JSONObject kv3 = addr.get(3);
        assertEquals("City", kv3.getAsString(AccuConnectJsonService.FIELD_NAME));
        assertEquals("San-Diego", kv3.getAsString(AccuConnectJsonService.FIELD_VALUE));

    }

    @Test
    public void createItemsHead() {
        service.setAddressLess(0);
        JSONObject items = service.createItems(req);
        assertEquals("Moon light", items.getAsString(AccuConnectJsonService.FIELD_NAME));
        assertEquals(42, items.getAsNumber(AccuConnectJsonService.FIELD_QTY));
        assertEquals("Abracadabra", items.getAsString(AccuConnectJsonService.FIELD_MCLASS));

        assertEquals(1, ((List) items.get(AccuConnectJsonService.FIELD_DIALS)).size());
        assertEquals(4, ((List) items.get(AccuConnectJsonService.FIELD_LISTDATA)).size());

        service.setAddressLess(200);
        items = service.createItems(req);
        assertEquals("First_Class", items.getAsString(AccuConnectJsonService.FIELD_MCLASS));

    }

    @Test
    public void createListItems() {
        List<JSONObject> items = service.createListData(req);
        assertEquals(4, items.size());
        assertEquals("John", items.get(0).getAsString(AccuConnectJsonService.FIELD_FIRSTNAME));
        assertEquals("Dow", items.get(0).getAsString(AccuConnectJsonService.FIELD_LASTNAME));
        assertEquals("4523 Under the bridge", items.get(0).getAsString(AccuConnectJsonService.FIELD_ADDRESS1));
        assertEquals("Left corner", items.get(0).getAsString(AccuConnectJsonService.FIELD_ADDRESS2));
        assertEquals("Seattle", items.get(0).getAsString(AccuConnectJsonService.FIELD_CITY));
        assertEquals("GR", items.get(0).getAsString(AccuConnectJsonService.FIELD_STATE));
        assertEquals(1, items.get(0).getAsNumber(AccuConnectJsonService.FIELD_ZIP));

        assertEquals("", items.get(3).getAsString(AccuConnectJsonService.FIELD_FIRSTNAME));
        assertEquals(null, items.get(3).getAsString(AccuConnectJsonService.FIELD_LASTNAME));

        JSONObject jo = service.createItems(req);
        String str = jo.toJSONString().toLowerCase();
        assertFalse(str.contains("null"));




    }

    @Test
    public void createListItemsWithSubstitution() {
        AccuConnectJsonServiceImpl serviceWithSubstitution = new AccuConnectJsonServiceImpl();
        serviceWithSubstitution.setSubstituteName(true);
        serviceWithSubstitution.setSubstituteValue("Unkle Sam");

        List<JSONObject> items = serviceWithSubstitution.createListData(req);
        assertEquals(4, items.size());
        assertEquals("Unkle Sam", items.get(0).getAsString(AccuConnectJsonService.FIELD_FIRSTNAME));
        assertEquals(null, items.get(0).getAsString(AccuConnectJsonService.FIELD_LASTNAME));
        assertEquals("Unkle Sam", items.get(1).getAsString(AccuConnectJsonService.FIELD_FIRSTNAME));
        assertEquals(null, items.get(1).getAsString(AccuConnectJsonService.FIELD_LASTNAME));

        JSONObject jo = service.createItems(req);
        String str = jo.toJSONString().toLowerCase();
        assertFalse(str.contains("null"));
    }

    @Test
    public void createRequest() {
        assertNotNull(service.createRequest(req));
    }

}