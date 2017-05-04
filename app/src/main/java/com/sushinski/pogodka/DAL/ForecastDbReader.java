/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.sushinski.pogodka.DL.Contracts.DBReaderContract;
import com.sushinski.pogodka.DL.models.ForecastModel;
import java.util.ArrayList;
import java.util.List;

class ForecastDbReader {
    private ForecastDbReader(){

    }

    public static long create(Context context, ForecastModel record){
        PogodkaDbHelper mDbHelper = new PogodkaDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBReaderContract.ForecastRecord.COLUMN_CITY_ID, record.mCityId);
        values.put(DBReaderContract.ForecastRecord.COLUMN_DATE, record.mDate);
        values.put(DBReaderContract.ForecastRecord.COLUMN_FORECAST_DATA, record.mForecast);
        return db.insert(
                    DBReaderContract.ForecastRecord.TABLE_NAME,
                    null,
                    values);
    }

    public static List<ForecastModel> read(Context context,
                                           String city_name,
                                           String days_count,
                                           Long date_from){
        PogodkaDbHelper mDbHelper = new PogodkaDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                DBReaderContract.ForecastRecord.COLUMN_CITY_ID,
                DBReaderContract.ForecastRecord.COLUMN_DATE,
                DBReaderContract.ForecastRecord.COLUMN_FORECAST_DATA
        };

        String sortOrder =
                DBReaderContract.ForecastRecord._ID + " ASC";

        String column = "";
        ArrayList<String> city_sel = new ArrayList<>();
        if(city_name != null ) {
            column = DBReaderContract.ForecastRecord.COLUMN_CITY_ID + " = " +
                    " (SELECT " + DBReaderContract.CityRecord.COLUMN_CITY_CODE + 
                    " FROM " + DBReaderContract.CityRecord.TABLE_NAME + " WHERE " +
                    DBReaderContract.CityRecord.COLUMN_CITY_TITLE + " = ? )";
            city_sel.add(city_name);
        }
        if(date_from != null){
            if(!column.equals("")){
                column += " AND ";
            }
            column += DBReaderContract.ForecastRecord.COLUMN_DATE + " >= ? ";
            city_sel.add(date_from.toString());
        }
        String[] sel_args = null;
        if(city_sel.size() != 0){
            sel_args = new String[city_sel.size()];
            city_sel.toArray(sel_args);
        }

        Cursor c = db.query(
                DBReaderContract.ForecastRecord.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                column,                                // The columns for the WHERE clause
                sel_args,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                 // The sort order
                days_count
        );
        List<ForecastModel> res_list = new ArrayList<>();

        c.moveToFirst();
        while(!c.isAfterLast()){
            ForecastModel model  = new ForecastModel();
            try {
                model.mCityId = c.getInt(c.getColumnIndexOrThrow(
                        DBReaderContract.ForecastRecord.COLUMN_CITY_ID));
                model.mDate = c.getLong(c.getColumnIndexOrThrow(
                        DBReaderContract.ForecastRecord.COLUMN_DATE));
                model.mForecast = c.getString(c.getColumnIndexOrThrow(
                        DBReaderContract.ForecastRecord.COLUMN_FORECAST_DATA));
                res_list.add(model);
            }catch(Exception e){
                // don`t add forecast to res list
            }
            c.moveToNext();
        }
        c.close();
        return res_list;
    }

    public static void update(Context context, ForecastModel model){
        PogodkaDbHelper mDbHelper = new PogodkaDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values;
        String selection = DBReaderContract.ForecastRecord.COLUMN_CITY_ID + " = ?";

        values = new ContentValues();
        values.put(DBReaderContract.ForecastRecord.COLUMN_CITY_ID, model.mCityId);
        values.put(DBReaderContract.ForecastRecord.COLUMN_DATE, model.mDate);
        values.put(DBReaderContract.ForecastRecord.COLUMN_FORECAST_DATA, model.mForecast);
        String selectionArgs[] = { model.mCityId.toString() };
        db.update(
                DBReaderContract.ForecastRecord.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public static void delete(Context context, String city_name){
        PogodkaDbHelper mDbHelper = new PogodkaDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String where = DBReaderContract.ForecastRecord.COLUMN_CITY_ID +
                " = " +
                " (SELECT " + DBReaderContract.CityRecord.COLUMN_CITY_CODE +
                " FROM " + DBReaderContract.CityRecord.TABLE_NAME + " WHERE " +
                DBReaderContract.CityRecord.COLUMN_CITY_TITLE + " = ? )";
        String[] where_params = new String[]{city_name};
        db.delete(DBReaderContract.ForecastRecord.TABLE_NAME, where, where_params);
    }


}
