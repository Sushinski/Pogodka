package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.util.Log;
import com.sushinski.pogodka.DL.models.CityModel;
import java.io.IOException;
import java.util.List;

import static com.sushinski.pogodka.DL.models.CityModel.CHECKED;
import static com.sushinski.pogodka.DL.models.CityModel.UNCHECKED;

public class CityManager extends BaseManager {
    public static final String SAINT_PETERBURG = "Sankt-Peterburg";
    public static final String MOSCOW = "Moscow";

    public CityManager(Context context){
        super(context);
    }

    /**
     * Checks if city table is empty
     * @return
     */
    public boolean isEmptyTable(){
        return CityDbReader.read(mContext, null, null).isEmpty();
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
                CityDbReader.create(mContext, cm);
            }
        }catch(IOException e){
            Log.e("CityManager", "Can`t populate cities table");
        }
    }

    public List<CityModel> getCitiesList(Boolean is_selected){
        return CityDbReader.read(mContext, null, is_selected);
    }

    public String getCityIdByName(String city_name) throws IndexOutOfBoundsException{
        return CityDbReader.read(mContext, city_name, null).get(0).mCityCode;
    }

    public void setCitySelection(final String city_name, boolean is_selected){
        CityModel city = new CityModel();
        city.mCityName = city_name;
        city.mIsSelected = is_selected ? CHECKED : UNCHECKED;
        CityDbReader.update(mContext, city);
    }
}
