/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.util.Log;
import com.sushinski.pogodka.DL.POJO.ForecastField;
import com.sushinski.pogodka.interfaces.UpdateFinishListener;
import com.sushinski.pogodka.DL.models.ForecastModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.List;
import android.os.Handler;

/**
 * Database Manager to forecast tables/fields
 */
public class ForecastManager extends BaseManager {
    public ForecastManager(Context context){
        super(context);
    }


    /**
     * Gets forecast json object from remote server api and saves its content to databse
     * @param city_names name to get forecasts for
     * @param days_count count of days as {@link String}
     * @param listener callback listener for calling after updating finishes
     */
    public synchronized void updateWeatherData(final List<String> city_names,
                                                 final String days_count,
                                                 final UpdateFinishListener listener){
        final Handler handler = new Handler();
        new Thread() {
            public void run() {
                // get time at bein if the day, used for correct date requesting
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                for(String city_name: city_names ) {
                    // checks if desired forecast alredy presented in database
                    List<ForecastModel> result = ForecastManager.this.
                            getActualForecast(city_name, days_count,
                                    calendar.getTimeInMillis());
                    // of not, or forecast days count less than desired
                    // and device in online mode
                    if( result.size() < Integer.parseInt(days_count)  && isOnline()) {
                        result.clear();
                        // get forecast from remote api
                        final JSONObject json = RemoteFetcher.getWeatherJSON(mContext,
                                city_name,
                                days_count);
                        if (json != null) {
                            // render and save forecast
                            result = RemoteFetcher.renderWeather(json);
                            ForecastDbReader.delete(mContext, city_name);
                            for (ForecastModel m : result) {
                                ForecastDbReader.create(mContext, m);
                            }
                        }
                    }
                    result.clear();
                }
                if (listener != null) {
                    // acknoledges listener that request finishes
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

    /**
     * gets actual forecast saved in database
     * @param city_name city name to get forecast for
     * @param days_count days count as {@link String}
     * @param date_from start date to get forecasts from
     * @return list of actual forecast objects
     */
    public List<ForecastModel> getActualForecast(final String city_name,
                                                 final String days_count,
                                                 final Long date_from){
        return ForecastDbReader.read(mContext, city_name, days_count, date_from);
    }


    /**
     * Parses json object fields as forecast params
     * @param json_repr Json object as {@link String}
     * @return object of selected forecast fields
     */
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
