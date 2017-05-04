package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sushinski.pogodka.DL.Contracts.DBReaderContract;

class PogodkaDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String UNIQUE  = " UNIQUE";
    private static final String SQL_CREATE_CITIES =
            "CREATE TABLE " + DBReaderContract.CityRecord.TABLE_NAME + " (" +
                    DBReaderContract.CityRecord._ID + " INTEGER PRIMARY KEY," +
                    DBReaderContract.CityRecord.COLUMN_CITY_TITLE + TEXT_TYPE + UNIQUE + COMMA_SEP +
                    DBReaderContract.CityRecord.COLUMN_CITY_CODE + TEXT_TYPE + COMMA_SEP +
                    DBReaderContract.CityRecord.COLUMN_CITY_SELECTED + TEXT_TYPE + " )";
    private static final String SQL_CREATE_FORECAST_ITEMS =
            "CREATE TABLE " + DBReaderContract.ForecastRecord.TABLE_NAME + " (" +
                    DBReaderContract.ForecastRecord._ID + " INTEGER PRIMARY KEY," +
                    DBReaderContract.ForecastRecord.COLUMN_CITY_ID + INT_TYPE + COMMA_SEP +
                    DBReaderContract.ForecastRecord.COLUMN_DATE + INT_TYPE + COMMA_SEP +
                    DBReaderContract.ForecastRecord.COLUMN_FORECAST_DATA + TEXT_TYPE + " )";


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pogodka.db";
    public PogodkaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        // create database
        db.execSQL(SQL_CREATE_CITIES);
        db.execSQL(SQL_CREATE_FORECAST_ITEMS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // actually not supported now
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}