package com.sushinski.pogodka.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sushinski.pogodka.DL.DBReaderContract.CityRecord;
import com.sushinski.pogodka.models.CityModel;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class CityDbReader {
    private CityDbReader(){}

    public static long createRecord(Context context, CityModel record){
        CityReaderDbHelper mDbHelper = new CityReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CityRecord.COLUMN_CITY_TITLE, record.mCityName);
        values.put(CityRecord.COLUMN_CITY_CODE, record.mCityCode);
        values.put(CityRecord.COLUMN_CITY_SELECTED, "0");

        return db.insert(
                CityRecord.TABLE_NAME,
                null,
                values);
    }

    public static List<CityModel> getCities(Context context){
        CityReaderDbHelper mDbHelper = new CityReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                CityRecord.COLUMN_CITY_TITLE,
                CityRecord.COLUMN_CITY_CODE,
                CityRecord.COLUMN_CITY_SELECTED
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                CityRecord.COLUMN_CITY_TITLE + " ASC";

        Cursor c = db.query(
                CityRecord.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        List<CityModel> city_list = new ArrayList<>();
        c.moveToFirst();
        try {
            while (!c.isAfterLast()) {
                CityModel city = new CityModel();
                city.mCityName = c.getString(c.getColumnIndexOrThrow(CityRecord.COLUMN_CITY_TITLE));
                city.mCityCode = c.getString(c.getColumnIndexOrThrow(CityRecord.COLUMN_CITY_CODE));
                city.mIsSelected = c.getString(c.getColumnIndexOrThrow(CityRecord.COLUMN_CITY_SELECTED));
                city_list.add(city);
            }
        }catch(IllegalArgumentException e){
            Log.e("Pogodka", "Can`t find all columns for city table");
        }
        c.close();
        return city_list;
    }

    public static void updateCities(Context context, List<CityModel> cities_list){
        CityReaderDbHelper mDbHelper = new CityReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values;
        String selection = CityRecord.COLUMN_CITY_TITLE + " LIKE ?";


        for(CityModel sm : cities_list){
            values = new ContentValues();
            values.put(CityRecord.COLUMN_CITY_CODE, sm.mCityCode);
            values.put(CityRecord.COLUMN_CITY_SELECTED, sm.mIsSelected);
            String selectionArgs[] = { sm.mCityName };
            db.update(
                    CityRecord.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }

}
