package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.util.Log;
import com.sushinski.pogodka.DL.POJO.ForecastField;
import com.sushinski.pogodka.DL.models.CityModel;
import com.sushinski.pogodka.interfaces.UpdateFinishListener;
import com.sushinski.pogodka.DL.models.ForecastModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import android.os.Handler;

public class ForecastManager extends BaseManager {
    public ForecastManager(Context context){
        super(context);
    }

    public synchronized void updateWeatherData(final List<String> city_names,
                                                 final String days_count,
                                                 final UpdateFinishListener listener){
        if(isOnline()) {
            final Handler handler = new Handler();
            new Thread() {
                public void run() {
                    for(String city_name: city_names ) {
                        Log.d("started:", city_name);
                        final List<ForecastModel> result;
                        final JSONObject json = RemoteFetcher.getWeatherJSON(mContext,
                                city_name,
                                days_count);
                        if (json != null) {
                            result = RemoteFetcher.renderWeather(json);
                            ForecastDbReader.delete(mContext, city_name);
                            for (ForecastModel m : result) {
                                ForecastDbReader.create(mContext, m);
                            }
                        }
                        Log.d("finished:", city_name);
                    }
                    if (listener != null) {
                        Runnable r = new Runnable() {
                            public void run() {
                                listener.updateFinish();
                            }
                        };
                        handler.post(r);
                    }
                }
            }.start();
        }
    }

    public List<ForecastModel> getActualForecast(final String city_name,
                                                 final String days_count,
                                                 final Long date_from){
        return ForecastDbReader.read(mContext, city_name, days_count, date_from);
    }

    public ForecastField parseForecastJson(String json_repr){
        try {
            ForecastField fields = new ForecastField();
            JSONObject data = new JSONObject(json_repr);
            fields.day_in_ms = data.getLong("dt") * 1000;
            JSONObject temp = data.getJSONObject("temp");
            fields.day_temp = temp.getDouble("day");
            fields.eve_temp = temp.getDouble("eve");
            fields.night_temp = temp.getDouble("night");
            fields.max_temp = temp.getDouble("max");
            fields.min_temp = temp.getDouble("min");
            fields.morn_temp = temp.getDouble("morn");
            fields.pressure = data.getDouble("pressure");
            fields.humidity = data.getDouble("humidity");
            JSONArray weather = data.getJSONArray("weather");
            JSONObject w_item = weather.getJSONObject(0);
            fields.main_descr = w_item.getString("main");
            fields.detailed_descr = w_item.getString("description");
            fields.wind_speed = data.getDouble("speed");
            fields.deg = data.getDouble("deg");
            fields.clouds = data.getDouble("clouds");
            return fields;
        }catch(JSONException e){
            Log.e("parseForecastJson", "unable to create json representation");
        }
        return null;
    }

}
