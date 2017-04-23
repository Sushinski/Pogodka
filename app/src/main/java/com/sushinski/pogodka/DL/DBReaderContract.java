package com.sushinski.pogodka.DL;

import android.provider.BaseColumns;

public final class DBReaderContract {

    private DBReaderContract(){}

    public static abstract class CityRecord implements BaseColumns {
        public static final String TABLE_NAME = "CityRec";
        public static final String COLUMN_CITY_TITLE = "city_name";
        public static final String COLUMN_CITY_CODE = "city_code";
        public static final String COLUMN_CITY_SELECTED = "selected";
    }
}
