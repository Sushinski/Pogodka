/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import com.sushinski.pogodka.DL.models.CityModel;
import java.io.IOException;
import java.util.List;
import static com.sushinski.pogodka.DL.models.CityModel.CHECKED;
import static com.sushinski.pogodka.DL.models.CityModel.UNCHECKED;

/**
 * City table manager, holds useful database-releted methods
 */
public class CityManager extends BaseManager {
    public static final String SAINT_PETERBURG = "Sankt-Peterburg";
    public static final String MOSCOW = "Moscow";

    public CityManager(Context context){
        super(context);
    }

    /**
     * Checks if city table is empty
     * @return  true is table is empty, else false
     */
    public boolean isEmptyTable(){
        return CityDbReader.read(null, null).isEmpty();
    }

    /**
     * populates cities database table with etalonic city names and ids
     * which needed to be used in queries
     */
    public void populateDatabase() {
        try {
            CityFileReader reader = new CityFileReader(mContext);
            List<CityModel> cities =
                    reader.JSONtoModelList(CityFileReader.CITIES_CONF_DEFAULT_NAME);
            for (CityModel cm : cities) {
                CityDbReader.create(cm);
            }
        }catch(IOException e){
            Log.e("CityManager", "Can`t populate cities table");
        }
    }

    /**
     * Gets cities with selected/diselected mark(or all, if null)
     * @param is_selected selected mark of records to get
     * @return list of city objects
     */
    public List<CityModel> getCitiesList(@Nullable Boolean is_selected){
        return CityDbReader.read(null, is_selected);
    }

    /**
     * Gets city id for remote api request with given name
     * @param city_name name of city to get id for
     * @return city id
     * @throws IndexOutOfBoundsException if city with given name not presented
     */
    String getCityIdByName(String city_name) throws IndexOutOfBoundsException{
        return CityDbReader.read(city_name, null).get(0).mCityCode;
    }

    /**
     * Sets seleted flag for record with given city name
     * @param city_name Name of city to set flag for
     * @param is_selected Selection Flag to set
     */
    public void setCitySelection(final String city_name, boolean is_selected){
        CityModel city = new CityModel();
        city.mCityName = city_name;
        city.mIsSelected = is_selected ? CHECKED : UNCHECKED;
        CityDbReader.update(city);
    }
}
