package com.sushinski.pogodka.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.sushinski.pogodka.DL.DBReaderContract.CityRecord;
import com.sushinski.pogodka.models.CityModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityDbReader {
    private CityDbReader(){}

    public static long create(Context context, CityModel record){
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

    public static CityModel read(Context context){
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
        c.moveToFirst();
        try {
            CityModel city = new CityModel();
            city.mCityName = c.getString(c.getColumnIndexOrThrow(CityRecord.COLUMN_CITY_TITLE));
            city.mCityCode = c.getString(c.getColumnIndexOrThrow(CityRecord.COLUMN_CITY_CODE));
            city.mIsSelected = c.getString(c.getColumnIndexOrThrow(CityRecord.COLUMN_CITY_SELECTED));
            return city;
        }catch(IllegalArgumentException e){
            Log.e("Pogodka", "Can`t find all columns for city table");
        }finally {
            c.close(); // todo: check using finally block
        }
        return null;
    }

    public static void update(Context context, CityModel city){
        CityReaderDbHelper mDbHelper = new CityReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values;
        String selection = CityRecord.COLUMN_CITY_TITLE + " LIKE ?";

        values = new ContentValues();
        values.put(CityRecord.COLUMN_CITY_CODE, city.mCityCode);
        values.put(CityRecord.COLUMN_CITY_SELECTED, city.mIsSelected);
        String selectionArgs[] = { city.mCityName };
        db.update(
                CityRecord.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public static void delete(Context context, List<CityModel> cities_list){

    }

}
