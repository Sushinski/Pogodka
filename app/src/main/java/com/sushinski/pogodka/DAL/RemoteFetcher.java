package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.util.Log;

import com.sushinski.pogodka.R;
import com.sushinski.pogodka.models.ForecastModel;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RemoteFetcher {
    private  static final String OPENWEATHER_API_URL =
            "http://api.openweathermap.org/data/2.5/forecast/" +
                    "?id=%s&APPID=%s&units=metric&lang=ru ";
    private static final String OPENWEATHER_API_URL_DETAILED =
            "http://api.openweathermap.org/data/2.5/forecast/" +
                    "daily?id=%s&APPID=%s&units=metric&cnt=%s&lang=ru ";

    public static JSONObject getWeatherJSON(Context context, String city, String days){
        try {
            CityManager mngr = new CityManager(context);
            URL url;
            if(days == null) {
                url = new URL(String.format(OPENWEATHER_API_URL,
                        mngr.getCityIdByName(city),
                        PrefStorage.getPref(PrefStorage.OPENWEATHER_API_KEY_KEY)));
            }else{
                url = new URL(String.format(OPENWEATHER_API_URL_DETAILED,
                        mngr.getCityIdByName(city),
                        PrefStorage.getPref(PrefStorage.OPENWEATHER_API_KEY_KEY),
                        days));
            }
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }

    public static List<ForecastModel> renderWeather(JSONObject json){
        List<ForecastModel> res_list = new ArrayList<>();
        try {
            JSONArray objs = json.getJSONArray("list");
            for(int i = 0; i < Integer.parseInt(json.getString("cnt")); ++i){
                JSONObject obj = objs.getJSONObject(i);
                if(obj != null){
                    ForecastModel item = new ForecastModel();
                    item.mDate = obj.getString("dt");
                    item.mTemp = obj.getJSONObject("temp").getString("day");
                    item.mSecondaryForecast = obj.getJSONArray("weather").
                            getJSONObject(0).getString("description");
                    res_list.add(item);
                }
            }
        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
        return res_list;
    }
}

