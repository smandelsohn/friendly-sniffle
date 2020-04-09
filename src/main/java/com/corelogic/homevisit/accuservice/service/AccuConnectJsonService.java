package com.corelogic.homevisit.accuservice.service;

import java.util.Map;

/**
 * Designated to create json request and parse response of accu connect.
 * Incoming information for request - SI resilt of selects. The addresses list must me present
 * as array of map.
 */
public interface AccuConnectJsonService {

    /**
     * Resulting field names.
     */
    String FIELD_CREATORUID = "creatoruid";
    String FIELD_EXTORDERREF = "orderextreference";
    String FIELD_BILLINGADDRESS = "billingaddress";
    String FIELD_NAME = "name";
    String FIELD_VALUE = "value";
    String FIELD_ITEMS = "items";
    String FIELD_DIALS = "dials";
    String FIELD_QTY = "quantity";
    String FIELD_MCLASS = "mailclassname";
    String FIELD_LISTDATA = "listdata";
    String FIELD_FIRSTNAME = "firstname";
    String FIELD_LASTNAME = "lastname";
    String FIELD_ADDRESS1 = "address1";
    String FIELD_ADDRESS2 = "address2";
    String FIELD_CITY = "city";
    String FIELD_STATE = "state";
    String FIELD_ZIP = "zip";
    String FIELD_EMAIL = "email";
    String FIELD_PHONE = "phone";
    String FIELD_ISURL = "isurl";


    /**
     * Field names in sql result map
     */
    String IN_FIELD_CARTIDID = "CARTID_ID";
    String IN_FIELD_BILLINGADDRESS = "BILLINGADDRESS";
    String IN_FIELD_BILLINGZIP = "BILLINGZIP";
    String IN_FIELD_BILLINGSTATE = "BILLINGSTATE";
    String IN_FIELD_BILLINGCITY = "BILLINGCITY";
    String IN_FIELD_ITEMQTY = "ADD_COUNT";
    String IN_FIELD_ITEMNAME = "PRINT_NAME";
    String IN_FIELD_PRINTREQUESTID = "PRINTREQUESTID";
    String IN_FIELD_MAILCLASSNAME = "MAILCLASS";

    /**
     * Field names from sql in verifiedAddressIntegrationFlow
     * "select first_name, last_name, address, address2, city, state, zip from printAddressVerified where print_request_id = :printRequestId"
     */
    String IN_FIELD_ADDRESSES = "ADDRESSES";
    String IN_FIELD_FIRST_NAME = "FIRST_NAME";
    String IN_FIELD_LAST_NAME = "LAST_NAME";
    String IN_FIELD_ADDRESS = "ADDRESS";
    String IN_FIELD_ADDRESS2 = "ADDRESS2";
    String IN_FIELD_CITY = "CITY";
    String IN_FIELD_STATE = "STATE";
    String IN_FIELD_ZIP = "ZIP";


    String IN_FIELD_DIALS = "DIALS";
    String IN_FIELD_NAME = "NAME";
    String IN_FIELD_ISURL = "ISURL";
    String IN_FIELD_VALUE = "VALUE";


    /**
     * Create Json string with accu connect request.
     *
     * @param req given result of SI flow
     * @return accu connect request
     */
    String createRequest(Map<String, Object> req);


}
