package com.corelogic.homevisit.accuservice.service.impl;

import com.corelogic.homevisit.accuservice.service.AccuConnectJsonService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/** {@inheritDoc} */
@Service
public class AccuConnectJsonServiceImpl implements AccuConnectJsonService {

    @Value("${accu.connect.creatoruid}")
    private String creatorUid;

    @Value("${accu.connect.substitute.name}")
    private boolean substituteName;

    @Value("${accu.connect.substitute.value}")
    private String substituteValue;

    @Value("${accu.connect.mailclass.addrless}")
    private int addressLess;

    @Value("${accu.connect.mailclass.value}")
    private String mailClass;





    /** {@inheritDoc} */
    @Override
    public String createRequest(final Map<String, Object> req) {
        return createJsonRequest(req).toJSONString();
    }

    /** {@inheritDoc} */
    public JSONArray createJsonRequest(final Map<String, Object> req) {
        final JSONArray reqBodyArr = new JSONArray();
        final JSONObject reqBody = new JSONObject();
        reqBody.appendField(FIELD_CREATORUID, creatorUid);
        reqBody.appendField(FIELD_EXTORDERREF, "" + req.get(IN_FIELD_PRINTREQUESTID));
        reqBody.appendField(FIELD_BILLINGADDRESS, createBillingAddress(req));
        Map[] items = new Map[1];
        items[0] = createItems(req);
        reqBody.appendField(FIELD_ITEMS, items);
        reqBodyArr.appendElement(reqBody);
        return reqBodyArr;
    }


    /**
     * Fille billling address from request.
     * @param req entire request map
     * @return list of pay-value pair, where each Key for pair hold "name" or "value". The "name" value is predefined -
     * Address1, Zip, etc. Unfortunately the list of predefined names in unknown and need to
     * guess ATM, comes from readyForConnectMessageSource() sql
     */
    List<JSONObject> createBillingAddress(final Map<String, Object> req) {
        final List<JSONObject> lst = new ArrayList<>();
        lst.add(
                new JSONObject()
                        .appendField(FIELD_NAME, "Address1")
                        .appendField(FIELD_VALUE, req.get(IN_FIELD_BILLINGADDRESS))
        );
        lst.add(
                new JSONObject()
                        .appendField(FIELD_NAME, "Zip")
                        .appendField(FIELD_VALUE, req.get(IN_FIELD_BILLINGZIP))
        );
        lst.add(
                new JSONObject()
                        .appendField(FIELD_NAME, "State")
                        .appendField(FIELD_VALUE, req.get(IN_FIELD_BILLINGSTATE))
        );
        lst.add(
                new JSONObject()
                        .appendField(FIELD_NAME, "City")
                        .appendField(FIELD_VALUE, req.get(IN_FIELD_BILLINGCITY))
        );
        return lst;
    }


    JSONObject createItems(final Map<String, Object> req) {
        final JSONObject rez =  new JSONObject();
        rez.appendField(FIELD_NAME, req.get(IN_FIELD_ITEMNAME));
        rez.appendField(FIELD_QTY, req.get(IN_FIELD_ITEMQTY));
        rez.appendField(FIELD_MCLASS, req.get(IN_FIELD_MAILCLASSNAME));

        rez.appendField(FIELD_DIALS, createDials(req));
        List<JSONObject> jo = createListData(req);
        rez.appendField(FIELD_LISTDATA, jo);

        if (jo.size() < addressLess) { // todo remove hardcoded values
            rez.appendField(FIELD_MCLASS, mailClass);
        }
        return rez;
    }

    List<JSONObject> createDials(final Map<String, Object> req) {
        final List<JSONObject> lst = new ArrayList<>();
        final List<Map<String, Object>> dials = (List<Map<String, Object>>) req.get(IN_FIELD_DIALS);
        dials.forEach(
                rawDial -> {
                    JSONObject dial = new JSONObject();
                    dial.appendField(FIELD_VALUE, rawDial.get(IN_FIELD_VALUE));
                    dial.appendField(FIELD_NAME, rawDial.get(IN_FIELD_NAME));
                    dial.appendField(FIELD_ISURL, rawDial.get(IN_FIELD_ISURL));
                    lst.add(dial);
                }
        );
        return lst;
    }

    List<JSONObject> createListData(final Map<String, Object> req) {
        final List<JSONObject> lst = new ArrayList<>();
        final List<Map<String, Object>> addrSqlRez = (List<Map<String, Object>>) req.get(IN_FIELD_ADDRESSES);
        addrSqlRez.forEach( addr  -> {
            JSONObject ta = new JSONObject();

            if (substituteName) {
                ta.appendField(FIELD_FIRSTNAME, substituteValue);
            } else {
                if (addr.get(IN_FIELD_FIRST_NAME) != null) {
                    ta.appendField(FIELD_FIRSTNAME, addr.get(IN_FIELD_FIRST_NAME));
                }
                if (addr.get(IN_FIELD_LAST_NAME) != null) {
                    ta.appendField(FIELD_LASTNAME, addr.get(IN_FIELD_LAST_NAME));
                }
            }


            if (addr.get(IN_FIELD_ADDRESS) != null) {
                ta.appendField(FIELD_ADDRESS1, addr.get(IN_FIELD_ADDRESS));
            }
            if (addr.get(IN_FIELD_ADDRESS2) != null) {
                ta.appendField(FIELD_ADDRESS2, addr.get(IN_FIELD_ADDRESS2));
            }
            if (addr.get(IN_FIELD_CITY) != null) {
                ta.appendField(FIELD_CITY, addr.get(IN_FIELD_CITY));
            }
            if (addr.get(IN_FIELD_STATE) != null) {
                ta.appendField(FIELD_STATE, addr.get(IN_FIELD_STATE));
            }
            if (addr.get(IN_FIELD_ZIP) != null) {
                ta.appendField(FIELD_ZIP, addr.get(IN_FIELD_ZIP));
            }

            lst.add(ta);
        });
        return lst;
    }

    void setSubstituteName(boolean substituteName) {
        this.substituteName = substituteName;
    }

    void setSubstituteValue(String substituteValue) {
        this.substituteValue = substituteValue;
    }

    void setAddressLess(int addressLess) {
        this.addressLess = addressLess;
    }

    void setMailClass(String mailClass) {
        this.mailClass = mailClass;
    }
}
