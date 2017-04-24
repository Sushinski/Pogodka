package com.sushinski.pogodka.DAL;

import android.content.Context;
import com.sushinski.pogodka.R;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteFetcher {
    private static final String OPENWEATHER_API_URL =
            "http://api.openweathermap.org/data/2.5/forecast/daily?id=%s&APPID=%s&units=metric&cnt=%s ";

    public static JSONObject getWeatherJSON(Context context, String city){
        try {
            CityManager mngr = new CityManager(context);
            URL url = new URL(String.format(OPENWEATHER_API_URL,
                    mngr.getCityIdByName(city),
                    PrefStorage.getPref(PrefStorage.OPENWEATHER_API_KEY_KEY),
                    "3"));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            //connection.addRequestProperty("x-api-key",
            //        context.getString(R.string.open_weather_maps_app_id));

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
}

