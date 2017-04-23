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
    private Context myContext;
    public CityFileReader(Context context){
        myContext = context;
    }

    public List<CityModel> renderJSON(JSONObject json){
        List<CityModel> cm_list = new ArrayList<>();
        try {
            JSONArray objs = json.getJSONArray("");
            for(int i = 0; i < objs.length(); i++){
                JSONObject obj = objs.getJSONObject(i);
                CityModel cm = new CityModel();
                cm.mCityName = obj.getString("name");
                cm.mCityCode = obj.getString("id");
                cm.mIsSelected = "0";
                cm_list.add(cm);
            }
        }catch(Exception e) {
            Log.e("Error", "Can`t render cities json object");
        }
        return cm_list;
    }

    public JSONObject getJSONObject(String json_filename) throws IOException {
        try {
            InputStream istream = myContext.getAssets().open(json_filename);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(istream));
            StringBuilder json = new StringBuilder(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();
            istream.close();
            return new JSONObject(json.toString());
        }catch(Exception e){
            throw new IOException("Can`t create JSON Object");
        }
    }
}
