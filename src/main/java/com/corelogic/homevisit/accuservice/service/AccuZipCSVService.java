package com.corelogic.homevisit.accuservice.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Transform list of map to csv files. Single map in List represent one record as
 * key - value , where key is column name.
 */
public interface AccuZipCSVService {

    String [] ACCU_ZIP_HEADER = {"first", "last", "address", "address2", "city", "st", "zip"};

    String ADDRESS1 = "ADDRESS1";
    String ADDRESS2 = "ADDRESS2";
    String CITY = "CITY";
    String FIRSTNAME = "FIRSTNAME";
    String LASTNAME = "LASTNAME";
    String STATE = "STATE";
    String ZIP = "ZIP";


    int POS_FIRST = 0;
    int POS_LAST = 1;
    int POS_ADDRESS = 2;
    int POS_ADDRESS2 = 3;
    int POS_CITY = 4;
    int POS_ST = 5;
    int POS_ZIP = 6;
    int POS_SAL = 7;
    int POS_MIDDLE = 8;
    int POS_CRRT = 9;
    int POS_BARCODE = 10;
    int POS_IMBARCODE = 11;
    int POS_IMBDITITS = 12;
    int POS_SEQUENCE = 13;
    int POS_CONTID = 14;
    int POS_GPBID = 15;
    int POS_ENDORSE = 16;

    String NAME_FIRST = "FIRST";
    String NAME_LAST = "LAST";
    String NAME_ADDRESS = "ADDRESS";
    String NAME_ADDRESS2 = "ADDRESS2";
    String NAME_CITY = "CITY";
    String NAME_ST = "ST";
    String NAME_ZIP = "ZIP";
    String NAME_SAL = "SAL";
    String NAME_MIDDLE = "MIDDLE";
    String NAME_CRRT = "CRRT";
    String NAME_BARCODE = "BARCODE";
    String NAME_IMBARCODE = "IMBARCODE";
    String NAME_IMBDITITS = "IMBDITITS";
    String NAME_SEQUENCE = "SEQUENCE";
    String NAME_CONTID = "CONT_ID";
    String NAME_GPBID = "GPB_ID";
    String NAME_ENDORSE = "ENDORSE";

    /**
     * Transform table to scv . Dont expected much value, so can easily use String
     *
     * @param table given list of maps
     * @return Csv file
     */
    String createScvFile(List<Map> table);

    /**
     * Parce csv to SI format, where row - item in the list has K-V field - value map.
     * The header with column names is removed.
     * @return parced list of map.
     */
    List<Map<String, String>>parseScvFile(String csvData);


}
