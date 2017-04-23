package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sushinski.pogodka.DL.DBReaderContract;

public class CityReaderDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String UNIQUE  = " UNIQUE";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBReaderContract.CityRecord.TABLE_NAME + " (" +
                    DBReaderContract.CityRecord._ID + " INTEGER PRIMARY KEY," +
                    DBReaderContract.CityRecord.COLUMN_CITY_TITLE + TEXT_TYPE + UNIQUE + COMMA_SEP +
                    DBReaderContract.CityRecord.COLUMN_CITY_CODE + TEXT_TYPE + COMMA_SEP +
                    DBReaderContract.CityRecord.COLUMN_CITY_SELECTED + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBReaderContract.CityRecord.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pogodka.db";
    public CityReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        // create database
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // actually not supported now
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}