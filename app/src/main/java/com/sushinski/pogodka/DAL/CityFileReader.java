package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.util.Log;

import com.sushinski.pogodka.models.CityModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CityFileReader {
    public static final String CITIES_CONF_DEFAULT_NAME = "city.list.json";
    public static final String START_TAG = "{";
    public static final String END_TAG = "}";
    private Context myContext;
    public CityFileReader(Context context){
        myContext = context;
    }

    private CityModel renderJSON(JSONObject obj){
        try {
            CityModel cm = new CityModel();
            cm.mCityName = obj.getString("name");
            cm.mCityCode = obj.getString("id");
            cm.mIsSelected = "0";
            return cm;
        }catch(Exception e) {
            Log.e("Error", "Can`t render cities json object");
        }
        return null;
    }

    public List<CityModel> JSONtoModelList(String json_filename) throws IOException {
        try {
            InputStream istream = myContext.getAssets().open(json_filename);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(istream));
            StringBuilder json = new StringBuilder(1024);
            String tmp = "";
            int tags_count = 0;
            List<CityModel> res_list = new ArrayList<>();
            while ((tmp = reader.readLine()) != null){
                if(tmp.contains(START_TAG)){
                    tags_count += 1;
                }else if(tmp.contains(END_TAG)){
                    tags_count -= 1;
                    if(tags_count == 0) {
                        json.append(tmp).append("\n");
                        CityModel cm = renderJSON(new JSONObject(json.toString()));
                        if (cm != null) {
                            res_list.add(cm);
                        }
                        json.setLength(0);
                    }
                }
                if(tags_count > 0){
                    json.append(tmp).append("\n");
                }
            }

            reader.close();
            istream.close();
            return res_list;
        }catch(Exception e){
            throw new IOException("Can`t create JSON Object");
        }
    }
}
