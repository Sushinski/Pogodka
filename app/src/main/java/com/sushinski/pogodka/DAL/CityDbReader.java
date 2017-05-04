package com.sushinski.pogodka.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;
import com.sushinski.pogodka.DL.Contracts.DBReaderContract.CityRecord;
import com.sushinski.pogodka.DL.models.CityModel;
import java.util.ArrayList;
import java.util.List;
import static com.sushinski.pogodka.DL.models.CityModel.CHECKED;
import static com.sushinski.pogodka.DL.models.CityModel.UNCHECKED;

/**
 * Cities table class, implements CRUD metods
 */
class CityDbReader {
    private CityDbReader(){}

    /**
     * creates cities database table recors
     * @param context Context object
     * @param record Model object holds fields to create with
     * @return id of created record
     */
    public static long create(Context context, CityModel record){
        PogodkaDbHelper mDbHelper = new PogodkaDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CityRecord.COLUMN_CITY_TITLE, record.mCityName);
        values.put(CityRecord.COLUMN_CITY_CODE, record.mCityCode);
        values.put(CityRecord.COLUMN_CITY_SELECTED, UNCHECKED);
        return db.insert(
                CityRecord.TABLE_NAME,
                null,
                values);
    }

    /**
     * Read cities table records
     * @param context context
     * @param city_name city name to get record for
     * @param is_selected selected status
     * @return list of city records objects
     */
    public static List<CityModel> read(Context context,
                                       @Nullable String city_name,
                                       @Nullable Boolean is_selected){
        PogodkaDbHelper mDbHelper = new PogodkaDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                CityRecord.COLUMN_CITY_TITLE,
                CityRecord.COLUMN_CITY_CODE,
                CityRecord.COLUMN_CITY_SELECTED
        };

        // sort by city name ascend
        String sortOrder =
                CityRecord.COLUMN_CITY_TITLE + " ASC";

        String column = "";
        ArrayList<String> city_sel  = new ArrayList<>();
        if(city_name != null) {
            column = CityRecord.COLUMN_CITY_TITLE +" = ?";
            city_sel.add(city_name);
        }
        if(is_selected != null){
            if( !column.equals("") ){
                column += " AND ";
            }
            column += CityRecord.COLUMN_CITY_SELECTED + " = ?";
            String flag = UNCHECKED;
            if(is_selected){
                flag = CHECKED;
            }
            city_sel.add(flag);
        }
        String[] sel_args = null;
        if(city_sel.size() != 0){
            sel_args = new String[city_sel.size()];
            city_sel.toArray(sel_args);
        }

        Cursor c = db.query(
                    CityRecord.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    column,                                // The columns for the WHERE clause
                    sel_args,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        List<CityModel> res_list = new ArrayList<>();
        try {
            c.moveToFirst();
            while(!c.isAfterLast()){
                CityModel city = new CityModel();
                city.mCityName = c.getString(c.getColumnIndexOrThrow(CityRecord.COLUMN_CITY_TITLE));
                city.mCityCode = c.getString(c.getColumnIndexOrThrow(CityRecord.COLUMN_CITY_CODE));
                city.mIsSelected = c.getString(c.getColumnIndexOrThrow(CityRecord.COLUMN_CITY_SELECTED));
                res_list.add(city);
                c.moveToNext();
            }
        }catch(Exception e){
            Log.e("Pogodka", "Can`t get city info");
        }finally {
            c.close();
        }
        return res_list;
    }

    /**
     * Upadate city table record with given parameters
     * @param context context
     * @param city model object with parameters
     */
    public static void update(Context context, CityModel city){
        PogodkaDbHelper mDbHelper = new PogodkaDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values;
        String selection = CityRecord.COLUMN_CITY_TITLE + " LIKE ?";

        values = new ContentValues();
        if(city.mCityCode != null){
            values.put(CityRecord.COLUMN_CITY_CODE, city.mCityCode);
        }
        if(city.mIsSelected != null) {
            values.put(CityRecord.COLUMN_CITY_SELECTED, city.mIsSelected);
        }
        String selectionArgs[] = { city.mCityName };
        db.update(
                CityRecord.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    /**
     * Deletes city records with given params
     * @param context context
     * @param cities_list list of city model objects to delete
     */
    public static void delete(Context context, List<CityModel> cities_list){
        // todo: should we actually delete cities ?
    }

}
