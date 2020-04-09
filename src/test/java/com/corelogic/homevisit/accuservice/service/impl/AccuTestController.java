package com.corelogic.homevisit.accuservice.service.impl;

import com.corelogic.homevisit.accuservice.service.AccuConnectJsonService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
public class AccuTestController {

    public static String accInfoReqBody = null;
    public static String uploadCsvReqBody = null;
    public static String upQuote = null;
    public static String cassJobReqBody = null;
    public static String dedupJobReqBody = null;
    public static String quoteReqBody = null;
    public static String downloadReqBody = null;
    public static String connectReqBody = null;
    public static String tokenReqBody = null;
    public static String accConnectRequestReqBody = null;
    public static int tokenTtl = 3;
    public static Map<String, String> accuConnectOrders = new HashMap<>();
    public static Map<String, String> accuConnectOrdersSecToken = new HashMap<>();



    @RequestMapping("/ws_360_webapps/download.jsp")
    public String downloadCsv(HttpServletRequest req) {
        String guid = req.getParameter("guid");
        String ftype = req.getParameter("ftype");
        downloadReqBody = guid + " " + ftype;
        final String csvData;
        if ("CAFE-BABE-CAFE-BABE".equals(guid)) {

            csvData = "first,last,address,address2,city,st,zip,sal,middle,crrt,barcode,imbarcode,imbdigits,sequence,cont_id,gpb_id,endorce\r\n"
                    + "\"Sweet\",\"Monika\",\"addr line 1020-1\",\"addr line 1020-2\",\"Sacramento\",\"MA\",\"01301-2614\", \"Mr\",\"\", \"123crrt\",\"/0113241234123450/\", \"ATTFASDFASDFASDFASDFASFADSFASDFSDFASDASDFASDFASDF\", \"00270999999999990314\", \"1\", \"1\", \"1\", \"*************SCH 3-DIGITS 015\"\r\n";

        } else {
            csvData = "first,last,address,address2,city,st,zip,sal,middle,crrt,barcode,imbarcode,imbdigits,sequence,cont_id,gpb_id,endorce\r\n"
                    + "\"Fat\",\"Dazy\",\"Addr11 "+guid+"\",\"Addr12\",\"Greenfield\",\"MA\",\"01301-2614\", \"Mr\",\"\", \"123crrt\",\"/0113241234123450/\", \"ATTFASDFASDFASDFASDFASFADSFASDFSDFASDASDFASDFASDF\", \"00270999999999990314\", \"1\", \"1\", \"1\", \"*************SCH 3-DIGITS 015\"\r\n"
                    + "\"Fast\",\"Gonzales\",\"Addr21\",\"Addr22\",\"Duxbury\",\"WA\",\"01301-2614\",\"Mrs\",\"\", \"345crrt\",\"/9113241234123450/\", \"DFGHDGFDGFDFGHDHDGDDGFHDFHDFGHDFGHDFGHDFGGHDFGHDF\", \"12431412424241431413\", \"2\", \"2\", \"2\", \"*************SCH 7-DIGITS 015\"";

        }

        return csvData;

    }

    @RequestMapping(value = "/v2_0/job/{guid}/QUOTE", method = RequestMethod.PUT)
    public void updateQuoteJob(@PathVariable("guid") String guid, HttpServletRequest req) {

        try {
            upQuote = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            upQuote = e.getMessage();
        }

    }

    @RequestMapping(value = "/v2_0/job/{guid}/QUOTE", method = RequestMethod.GET)
    public String quoteJob(@PathVariable("guid") String guid) {
        boolean status = true;
        quoteReqBody = guid;
        if ("1212-QWER-ZXVV-1234".equals(guid)) {
            status = false;
        }
        if ("BAD-NUM-ZXVV-1234".equals(guid)) {
            return "{\n" +
                    "  \"task_name\": \"FINISHED\",\n" +
                    "  \"Estimated_Postage_Standard_Nonprofit_Letter\": \"$24\",\n" +
                    "  \"task_percentage_completed\": \"0\",\n" +
                    "  \"Estimated_Postage_Standard_Card\": \"$24\",\n" +
                    "  \"Standard_Nonprofit_Card\": \"$0\",\n" +
                    "  \"Estimated_Postage_Standard_Letter\": \"$24\",\n" +
                    "  \"First_Class_Card\": \"$0\",\n" +
                    "  \"Estimated_Postage_First_Class_Card\": \"$16\",\n" +
                    "  \"Estimated_Postage_Standard_Nonprofit_Card\": \"$24\",\n" +
                    "  \"First_Class_Letter\": \"$0\",\n" +
                    "  \"Standard_Card\": \"$0\",\n" +
                    "  \"total_postage\": \"\",\n" +
                    "  \"presort_class\": \"\",\n" +
                    "  \"Estimated_Postage_Standard_Nonprofit_Flat\": \"$47\",\n" +
                    "  \"total_records\": \"\",\n" +
                    "  \"mail_piece_size\": \"\",\n" +
                    "  \"Standard_Nonprofit_Flat\": \"$0\",\n" +
                    "  \"First_Class_Flat\": \"$0\",\n" +
                    "  \"Standard_Nonprofit_Letter\": \"$0\",\n" +
                    "  \"format\": \"\",\n" +
                    "  \"task_state\": \"WARNING: This database has already been CASS Certified and cannot be processed again. The completed files are available for immediate download.\",\n" +
                    "  \"drop_zip\": \"\",\n" +
                    "  \"task_process\": \"CASS\",\n" +
                    "  \"total_presort_records\": \"\",\n" +
                    "  \"Estimated_Postage_Standard_Flat\": \"$47\",\n" +
                    "  \"Standard_Letter\": \"$0\",\n" +
                    "  \"Estimated_Postage_First_Class_Flat\": \"$47\",\n" +
                    "  \"success\": true,\n" +
                    "  \"Estimated_Postage_First_Class_Letter\": \"$24\",\n" +
                    "  \"Standard_Flat\": \"$0\",\n" +
                    "  \"postage_saved\": \"\"\n" +
                    "}";

        }
        return "{\n" +
                "  \"task_name\": \"FINISHED\",\n" +
                "  \"First_Class_Flat\": \"$619\",\n" +
                "  \"task_percentage_completed\": \"100\",\n" +
                "  \"Estimated_Postage_Standard_Card\": \"$549\",\n" +
                "  \"Estimated _Postage_Standard_Letter\": \"$549\",\n" +
                "  \"format\": \"\",\n" +
                "  \"task_state\": \"FINISHED\",\n" +
                "  \"First_Class_Card\": \"$154\",\n" +
                "  \"Estimated_Postage_First_Class_Card\": \"$526\",\n" +
                "  \"drop_zip\": \"\",\n" +
                "  \"First_Class_Letter\": \"$132\",\n" +
                "  \"total_presort_records\": \"\",\n" +
                "  \"Estimated_Postage_Standard_Flat\": \"$936\",\n" +
                "  \"Standard_Card\": \"$382\",\n" +
                "  \"total_postage\": \"\",\n" +
                "  \"Standard_Letter \": \"$382\",\n" +
                "  \"Estimated_Postage_First_Class_Flat\": \"$1,262\",\n" +
                "  \"success\": " + status + ",\n" +
                "  \"presort_class\": \"\",\n" +
                "  \"total_records\": \"2,001\",\n" +
                "  \"dq_dpvhsa_y\": \"2,001\",\n" +
                "  \"Estimated_Postage_First_Class_Letter\": \"$79 9\",\n" +
                "  \"mail_piece_size\": \"\",\n" +
                "  \"Standard_Flat\": \"$945\",\n" +
                "  \"postage_saved\": \"\",\n" +
                "  \"guid\": \""+guid+"\"\n" +
                "}";

    }

    @RequestMapping(value = "/v2_0/uploadProcess.jsp", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadCsv(
            @RequestParam("backOfficeOption") String backOfficeOption,
            @RequestParam("apiKey") String apiKey,
            @RequestParam("callbackURL") String callbackURL,
            @RequestParam("guid") String guid,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "manual_submit", required = false) String manualSumbmit,
            HttpServletRequest req
    ) {

        String uuidRet = UUID.randomUUID().toString();

        try {

            final String content = new String(file.getBytes());
            if (content.contains("Dizel")) {
                uuidRet = "9999-8888-7777-6666";
            }

            final JSONObject rbJSon = new JSONObject();
            rbJSon.appendField("backOfficeOption", backOfficeOption);
            rbJSon.appendField("apiKey", apiKey);
            rbJSon.appendField("callbackURL", callbackURL);
            rbJSon.appendField("guid", guid);
            rbJSon.appendField("manual_submit", manualSumbmit);
            rbJSon.appendField("file", file.getOriginalFilename());
            rbJSon.appendField("fileContent", content);
            rbJSon.appendField("url", req.getQueryString());
            uploadCsvReqBody = rbJSon.toString();
        } catch (IOException e) {
            uploadCsvReqBody = e.getMessage();
        }


        return "{\n" +
                "  \"success360Import\": true,\n" +
                "  \"quote_started\": true,\n" +
                "  \"cass_started\": false,\n" +
                "  \"guid\": \"" + uuidRet + "\"\n" +
                "}";
    }

    @RequestMapping("/v2_0/job/{guid}/DUPS/{type}")
    public String dedupJob(@PathVariable("guid") String guid, @PathVariable("type") String type) {
        dedupJobReqBody = guid + " " +type;
        return "{\"Addresses\":{\"Rows\":[]},\"NoFilteredRows\":0,\"TotalRows\":1,\"success\":true}";
    }



    @RequestMapping("/v2_0/job/{guid}/CASS")
    public String cassJob(@PathVariable("guid") String guid) {
        cassJobReqBody = guid;
        return "{\"Addresses\":{\"Rows\":[]},\"NoFilteredRows\":0,\"TotalRows\":1,\"success\":true}";

    }


    @RequestMapping("/v2_0/INFO")
    public String accInfo(HttpServletRequest req) {
        try {
            accInfoReqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            accInfoReqBody = e.getMessage();
        }
        return "{\n" +
                "  \"account_type\": \"Subscription\",\n" +
                "  \"level\": \"4\",\n" +
                "  \"success\": true,\n" +
                "  \"active\": true,\n" +
                "  \"credits_remaining\": {\n" +
                "    \"total\": \"106\",\n" +
                "    \"monthly\": \"96\",\n" +
                "    \"annual\": \"193\"\n" +
                "  },\n" +
                "  \"services\": \"Direct Mail with EDDM and Limited 25-record Mailing Lists Test Environment\",\n" +
                "  \"credits_used\": {\n" +
                "    \"total\": \"94\",\n" +
                "    \"monthly\": \"4\",\n" +
                "    \"annual\": \"7\"\n" +
                "  }\n" +
                "}";
    }

    @RequestMapping("/api/punchin/orderconfirmation")
    public String connect(HttpServletRequest req) {
        try {
            connectReqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            connectReqBody = e.getMessage();
        }
        return "{\n" +
                "  \"account_type\": \"Subscription\",\n" +
                "  \"level\": \"4\",\n" +
                "  \"success\": true,\n" +
                "  \"active\": true,\n" +
                "  \"credits_remaining\": {\n" +
                "    \"total\": \"106\",\n" +
                "    \"monthly\": \"96\",\n" +
                "    \"annual\": \"193\"\n" +
                "  },\n" +
                "  \"services\": \"Direct Mail with EDDM and Limited 25-record Mailing Lists Test Environment\",\n" +
                "  \"credits_used\": {\n" +
                "    \"total\": \"94\",\n" +
                "    \"monthly\": \"4\",\n" +
                "    \"annual\": \"7\"\n" +
                "  }\n" +
                "}";
    }

    private final AtomicInteger reqCnt = new AtomicInteger(0);

    @RequestMapping(value = "/acapp/token", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String acAppToken(
            @RequestParam("client_id") String client_id,
            @RequestParam("client_secret") String client_secret,
            @RequestParam("grant_type") String grant_type ) {

        tokenReqBody = client_id + " " + client_secret;

        return "{\n" +
                "  \"access_token\": \"U29Zb3VEZWNpZGVUb0xvb2tJbnRvUmFiYml0SG9sZTopTmljZQ==" + reqCnt.incrementAndGet() + "\",\n" +
                "  \"token_type\": \"bearer\",\n" +
                "  \"expires_in\": " + tokenTtl + "\n" +
                "}";

    }

    @RequestMapping(value = "/acapp/order", method = RequestMethod.POST)
    public String acAppToken(HttpServletRequest req) {

        final JSONParser jp = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

        String externalOrderId = "Parsing error";

        try {
            accConnectRequestReqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            JSONArray ja = (JSONArray) jp.parse(accConnectRequestReqBody);
            JSONObject jo = (JSONObject) ja.get(0);
            externalOrderId = jo.getAsString(AccuConnectJsonService.FIELD_EXTORDERREF);
            accuConnectOrders.put(externalOrderId, accConnectRequestReqBody);
            accuConnectOrdersSecToken.put(externalOrderId, req.getHeader(HttpHeaders.AUTHORIZATION));
        } catch (Exception e) {
            accConnectRequestReqBody = e.getMessage();
        }

        return "{\n" +
                "  \"resultcode\": 0,\n" +
                "  \"message\": \" Success\",\n" +   // the " Success" just emulation of error from accu side
                "  \"orderresponse\": [\n" +
                "    {\n" +
                "      \"errormessages\": \"\",\n" +
                "      \"orderextreference\": \"" + externalOrderId + "\",\n" +
                "      \"ordernumber\": 1023\n" +
                "    }\n" +
                "  ]\n" +
                "}";

    }



}
