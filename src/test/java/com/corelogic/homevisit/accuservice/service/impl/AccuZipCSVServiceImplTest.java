package com.corelogic.homevisit.accuservice.service.impl;

import com.corelogic.homevisit.accuservice.service.AccuZipCSVService;
import com.opencsv.ICSVWriter;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;

public class AccuZipCSVServiceImplTest {

    AccuZipCSVService accuZipCSVService = new AccuZipCSVServiceImpl();

    @Test
    public void createScvFileHeader() {

        String csv = accuZipCSVService.createScvFile(Collections.emptyList());

        assertTrue(csv.contains("\"first\""));
        assertTrue(csv.contains("\"last\""));
        assertTrue(csv.contains("\"address\""));
        assertTrue(csv.contains("\"address2\""));
        assertTrue(csv.contains("\"city\""));
        assertTrue(csv.contains("\"st\""));
        assertTrue(csv.contains("\"zip\""));


        assertEquals(csv.length()-1, csv.indexOf('\n'));
        assertEquals(csv.length()-2, csv.indexOf('\r'));



    }

    @Test
    public void parseScvFile() {
        final String csvData = "first,last,address,address2,city,st,zip,sal,middle,crrt,barcode,imbarcode,imbdigits,sequence,cont_id,gpb_id,endorce\r\n"
                + "\"Long\",\"Martin\",\"Addr\",\"Addr2\",\"Greenfield\",\"MA\",\"01301-2614\", \"Mr\",\"\", \"123crrt\",\"/0113241234123450/\", \"ATTFASDFASDFASDFASDFASFADSFASDFSDFASDASDFASDFASDF\", \"00270999999999990314\", \"1\", \"1\", \"1\", \"*************SCH 3-DIGITS 015\"\r\n"
                + "\"Short\",\"Melissa\",\"Addr\",\"Addr2\",\"Duxbury\",\"WA\",\"01301-2614\",\"Mrs\",\"\", \"345crrt\",\"/9113241234123450/\", \"DFGHDGFDGFDFGHDHDGDDGFHDFHDFGHDFGHDFGHDFGGHDFGHDF\", \"12431412424241431413\", \"2\", \"2\", \"2\", \"*************SCH 7-DIGITS 015\"\r\n"
                + "\"\",\"\",\"\",\"\",\"\",\"GA\",\"01301-2614\",\"Mrs\",\"\", \"345crrt\",\"/9113241234123450/\", \"DFGHDGFDGFDFGHDHDGDDGFHDFHDFGHDFGHDFGHDFGGHDFGHDF\", \"12431412424241431413\", \"2\", \"2\", \"2\", \"*************SCH 7-DIGITS 015\"\r\n";
        List<Map<String,String>> rows = accuZipCSVService.parseScvFile(csvData);
        assertEquals(3, rows.size());
        Map<String,String> row1 = rows.get(0);
        assertEquals("Long", row1.get(AccuZipCSVService.NAME_FIRST));
        assertEquals("Martin", row1.get(AccuZipCSVService.NAME_LAST));
        assertEquals("Addr", row1.get(AccuZipCSVService.NAME_ADDRESS));
        assertEquals("Addr2", row1.get(AccuZipCSVService.NAME_ADDRESS2));
        assertEquals("Greenfield", row1.get(AccuZipCSVService.NAME_CITY));
        assertEquals("MA", row1.get(AccuZipCSVService.NAME_ST));
        assertEquals("01301", row1.get(AccuZipCSVService.NAME_ZIP));
        // CWH-5322 assertEquals("01301-2614", row1.get(AccuZipCSVService.NAME_ZIP));
        assertEquals("Mr", row1.get(AccuZipCSVService.NAME_SAL));
        assertEquals("", row1.get(AccuZipCSVService.NAME_MIDDLE));
        assertEquals("123crrt", row1.get(AccuZipCSVService.NAME_CRRT));
        assertEquals("/0113241234123450/", row1.get(AccuZipCSVService.NAME_BARCODE));

        Map<String,String> row2 = rows.get(2);
        assertEquals("", row2.get(AccuZipCSVService.NAME_FIRST));
        assertEquals("", row2.get(AccuZipCSVService.NAME_LAST));


    }

    @Test
    public void createScvFileData() {
        final String notUsed = "not used";
        /*data taken from generic message select printAddresses
        { ID=1,
          ADDRESS1=Justforidtoaddressesflowtest,    ADDRESS2=address2,     ADDRESSNAME=addressName,
          ADDRESSTYPE=tttype,     ADDRESSEE=havenoidea,   CERTIFIED=false,    CITY=San-Diego,       COMPANY=CR,
          FIRSTNAME=John,     LASTNAME=Dou,    LOCALITY=null,     OCCUPANT=null,     PRINTREQUESTID=6,  STATE=CA,
          ZIP=60123 }, ......
         */
        final Map<String, Object> row1 = new HashMap<String, Object>() {{
            put("ID", BigInteger.valueOf(1));
            put(AccuZipCSVService.ADDRESS1, "a1");
            put(AccuZipCSVService.ADDRESS2, "a2");
            put("ADDRESSNAME", notUsed);
            put("ADDRESSTYPE", notUsed);
            put("ADDRESSEE", notUsed);
            put("CERTIFIED", false);
            put(AccuZipCSVService.CITY, "Lost angeles");
            put("COMPANY", "CR");
            put(AccuZipCSVService.FIRSTNAME, "John");
            put(AccuZipCSVService.LASTNAME, "Dou");
            put("LOCALITY", notUsed);
            put("OCCUPANT", notUsed);
            put("PRINTREQUESTID", notUsed);
            put(AccuZipCSVService.STATE, "CA");
            put(AccuZipCSVService.ZIP, "12345");
        }};
        final Map<String, Object> row2 = new HashMap<String, Object>() {{
            put("ID", BigInteger.valueOf(1));
            put(AccuZipCSVService.ADDRESS1, "a1");
            put(AccuZipCSVService.ADDRESS2, "a2");
            put("ADDRESSNAME", notUsed);
            put("ADDRESSTYPE", notUsed);
            put("ADDRESSEE", notUsed);
            put("CERTIFIED", false);
            put(AccuZipCSVService.CITY, "Seattle");
            put("COMPANY", "CR");
            put(AccuZipCSVService.FIRSTNAME, null);
            put(AccuZipCSVService.LASTNAME, null);
            put("LOCALITY", notUsed);
            put("OCCUPANT", notUsed);
            put("PRINTREQUESTID", notUsed);
            put(AccuZipCSVService.STATE, "WA");
            put(AccuZipCSVService.ZIP, "55555");
        }};
        final List<Map> data = new ArrayList<>();
        data.add( row1 );
        data.add( row2 );

        String csv = accuZipCSVService.createScvFile(data);
        String [] lines = csv.split("\r\n");
        assertEquals(3, lines.length);
        String [] head = lines[0].split(String.valueOf(ICSVWriter.DEFAULT_SEPARATOR));
        String [] dline1 = lines[1].split(String.valueOf(ICSVWriter.DEFAULT_SEPARATOR));
        String [] dline2 = lines[2].split(String.valueOf(ICSVWriter.DEFAULT_SEPARATOR));
        assertEquals(head.length, dline1.length);
        assertEquals(AccuZipCSVService.ACCU_ZIP_HEADER[0], head[0].replace("\"", ""));
        assertEquals( AccuZipCSVService.ACCU_ZIP_HEADER[1], head[1].replace("\"", ""));
        assertEquals( AccuZipCSVService.ACCU_ZIP_HEADER[2], head[2].replace("\"", ""));
        assertEquals( AccuZipCSVService.ACCU_ZIP_HEADER[3], head[3].replace("\"", ""));
        assertEquals( AccuZipCSVService.ACCU_ZIP_HEADER[4], head[4].replace("\"", ""));
        assertEquals( AccuZipCSVService.ACCU_ZIP_HEADER[5], head[5].replace("\"", ""));
        assertEquals( AccuZipCSVService.ACCU_ZIP_HEADER[6], head[6].replace("\"", ""));

        assertEquals("\"John\"", dline1[0]);
        assertEquals( "\"Dou\"", dline1[1]);
        assertEquals( "\"a1\"", dline1[2]);
        assertEquals( "\"a2\"", dline1[3]);
        assertEquals( "\"Lost angeles\"", dline1[4]);
        assertEquals( "\"CA\"", dline1[5]);
        assertEquals( "\"12345\"", dline1[6]);

        assertEquals( "", dline2[0]);
        assertEquals( "", dline2[1]);
        assertEquals( "\"a1\"", dline2[2]);
        assertEquals( "\"a2\"", dline2[3]);
        assertEquals( "\"Seattle\"", dline2[4]);
        assertEquals( "\"WA\"", dline2[5]);
        assertEquals( "\"55555\"", dline2[6]);

    }

}