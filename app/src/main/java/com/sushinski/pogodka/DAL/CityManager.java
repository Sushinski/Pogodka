package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.util.Log;

import com.sushinski.pogodka.models.CityModel;

import java.io.IOException;
import java.util.List;

public class CityManager {
    private Context mContext;
    public CityManager(Context context){
        mContext = context;
    }

    /**
     * Checks if city table is empty
     * @return
     */
    public boolean isEmptyTable(){
        return CityDbReader.read(mContext) != null;
    }

    /**
     * populates cities database table with etalonic city names and ids
     * which needed to be used in queries
     */
    public void populateDatabase() {
        try {
            CityFileReader reader = new CityFileReader(mContext);
            List<CityModel> cities = reader.renderJSON(
                    reader.getJSONObject(CityFileReader.CITIES_CONF_DEFAULT_NAME)
            );
            for (CityModel cm : cities) {
                CityDbReader.create(mContext, cm);
            }
        }catch(IOException e){
            Log.e("CityManager", "Can`t populate cities table");
        }
    }
}
