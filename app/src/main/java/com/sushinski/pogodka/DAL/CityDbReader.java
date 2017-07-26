/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.DAL;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.sushinski.pogodka.DL.models.CityModel;
import java.util.List;
import static com.sushinski.pogodka.DL.models.CityModel.CHECKED;
import static com.sushinski.pogodka.DL.models.CityModel.UNCHECKED;

/**
 * Cities table class, implements CRUD metods
 */
class CityDbReader {
    private CityDbReader(){}

    /**
     * Creates city record in database table
     * @param city_model model object to save in db
     * @return record primary key
     */
    static long create(@NonNull CityModel city_model){
        city_model.save();
        return city_model.getId();
    }

    /**
     * Read cities table records
     * @param city_name city name to get record for
     * @param is_selected selected status
     * @return list of city records objects
     */
    static List<CityModel> read(@Nullable String city_name,
                                       @Nullable Boolean is_selected){
        String _sort_order = CityModel.TABLE_NAME + "." +
                CityModel.COLUMN_CITY_TITLE +
                " ASC";
        From sel = new Select().from(CityModel.class);

        if(city_name != null) {
            sel = sel.where(CityModel.COLUMN_CITY_TITLE + " = ?", city_name);
        }
        if(is_selected != null){
            sel = sel.where(CityModel.COLUMN_CITY_SELECTED + " = ?",
                    is_selected ? CHECKED : UNCHECKED);
        }

        return sel.orderBy(_sort_order).execute();
    }

    /**
     * Upadate city table record with given parameters
     * @param city model object with parameters
     */
    static void update(CityModel city) {
        List<CityModel> saved_city = read(city.mCityName, null);
        if (saved_city.size() > 0) {
            CityModel cm = saved_city.get(0);
            cm.mIsSelected = city.mIsSelected;
            if(city.mCityCode != null){
                cm.mCityCode = city.mCityCode;
            }
            cm.save();
        }
    }

    /**
     * Deletes city records with given params
     * @param cities_list list of city model objects to delete
     */
    public static void delete(List<CityModel> cities_list){
        // todo: should we actually delete cities ?
    }

}
