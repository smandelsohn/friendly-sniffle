package com.corelogic.homevisit.accuservice.service.impl;

import com.corelogic.homevisit.accuservice.service.AccuZipCSVService;
import com.opencsv.*;
import com.opencsv.validators.LineValidatorAggregator;
import com.opencsv.validators.RowValidatorAggregator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/** {@inheritDoc} */
@Service
public class AccuZipCSVServiceImpl implements AccuZipCSVService {

    private final static char CR  = (char) 0x0D; // \n
    private final static char LF  = (char) 0x0A; // \r
    private final static String CRLF = Character.toString(CR) + Character.toString(LF);
    private final static String [] ROW_SOURCE_NAME = {AccuZipCSVService.FIRSTNAME, AccuZipCSVService.LASTNAME,
            AccuZipCSVService.ADDRESS1, AccuZipCSVService.ADDRESS2, AccuZipCSVService.CITY,
            AccuZipCSVService.STATE, AccuZipCSVService.ZIP };


    /** {@inheritDoc} */
    @Override
    public String createScvFile(final List<Map> table) {

        final StringWriter sw = new StringWriter();
        final CSVWriter writer = new CSVWriter(sw, ICSVWriter.DEFAULT_SEPARATOR, ICSVWriter.DEFAULT_QUOTE_CHARACTER,
                ICSVWriter.DEFAULT_ESCAPE_CHARACTER,  CRLF);
        writer.writeAll(Collections.singletonList(ACCU_ZIP_HEADER), true);
        writer.writeAll(transform(table), true);
        return sw.toString();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<Map<String, String>>parseScvFile(String csvData)  {
        final List<Map<String, String>> rez = new ArrayList();
        try (CSVReader csvReader = new CSVReader( new StringReader(csvData) ) ) {

            csvReader.readAll().forEach( r -> {
                    final Map<String, String> map = new HashMap();
                    map.put(NAME_FIRST, r[POS_FIRST]);
                    map.put(NAME_LAST, r[POS_LAST]);
                    map.put(NAME_ADDRESS, r[POS_ADDRESS]);
                    map.put(NAME_ADDRESS2, r[POS_ADDRESS2]);
                    map.put(NAME_CITY, r[POS_CITY]);
                    map.put(NAME_ST, r[POS_ST]);
                    map.put(NAME_ZIP, StringUtils.left(r[POS_ZIP],5)); //CWH-5233
                    map.put(NAME_SAL, r[POS_SAL]);
                    map.put(NAME_MIDDLE, r[POS_MIDDLE]);
                    map.put(NAME_CRRT, r[POS_CRRT]);
                    map.put(NAME_BARCODE, r[POS_BARCODE]);
                    // fields are not present in csv
//                    map.put(NAME_IMBARCODE, r[POS_IMBARCODE]);
//                    map.put(NAME_IMBDITITS, r[POS_IMBDITITS]);
//                    map.put(NAME_SEQUENCE, r[POS_SEQUENCE]);
//                    map.put(NAME_CONTID, r[POS_CONTID]);
//                    map.put(NAME_GPBID, r[POS_GPBID]);
//                    map.put(NAME_ENDORSE, r[POS_ENDORSE]);
                    rez.add(map);
                }
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(rez.size() > 0) {
            rez.remove(0);
        }

        return rez;
    }

    List<String[]> transform(List<Map> table) {
        final List<String[]> rez = new ArrayList<>();
        table.forEach(
                r -> {
                    String [] row = new String[7];
                    for (int i = 0; i < ROW_SOURCE_NAME.length; i++) {
                        Object val = r.get(ROW_SOURCE_NAME[i]);
                        if (val != null) {
                            row[i] = String.valueOf(val);
                        }
                    }
                    rez.add(row);
                }
        );
        return rez;
    }


}
