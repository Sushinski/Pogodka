package com.sushinski.pogodka.DAL;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.sushinski.pogodka.R;
import com.sushinski.pogodka.models.ForecastModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by пк on 26.04.2017.
 */

public class WeatherManager {
    private Context mContext;

    public WeatherManager(Context context){
        mContext = context;
    }

    public List<ForecastModel> updateWeatherData(final String city_name, final String days_count){
        final List<ForecastModel> result;
        final Handler handler;
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetcher.getWeatherJSON(mContext,
                        city_name,
                        days_count);
                Runnable r;
                if(json == null){
                    r = new Runnable(){
                        public void run(){
                            // todo: try get from db
                        }
                    };
                } else {
                    r = new Runnable(){
                        public void run(){
                            result = RemoteFetcher.renderWeather(json);
                        }
                    };
                }
                handler.post(r);
            }
        }.start();
    }


}
