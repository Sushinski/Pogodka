/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.DAL;

import android.support.annotation.Nullable;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.sushinski.pogodka.DL.models.CityModel;
import com.sushinski.pogodka.DL.models.ForecastModel;

import java.util.List;

class ForecastDbReader {
    private ForecastDbReader(){

    }

    static long create(ForecastModel record){
        record.save();
        return  record.getId();
    }

    static List<ForecastModel> read(String city_name,
                                           @Nullable String days_count,
                                           @Nullable Long date_from){

        String _sort_order = ForecastModel.TABLE_NAME + "." +
                ForecastModel.ID +
                " ASC";

        From sel = new Select().from(ForecastModel.class);

        if(city_name != null ) {
            sel = sel.innerJoin(CityModel.class)
                    .on(ForecastModel.TABLE_NAME
                        + "."
                        + ForecastModel.COLUMN_CITY_ID
                        + "="
                        + CityModel.TABLE_NAME
                        + "."
                        + CityModel.COLUMN_CITY_CODE)
                    .where(CityModel.TABLE_NAME
                            + "."
                            + CityModel.COLUMN_CITY_TITLE
                            + " = ?", city_name);
        }

        if(date_from != null){
            sel = sel.where(ForecastModel.COLUMN_DATE + " >= ?", date_from.toString());
        }

        if(days_count != null){
            sel = sel.limit(days_count);
        }

        return sel.orderBy(_sort_order).execute();
    }

    public static void update(ForecastModel model){
        ForecastModel fm = new Select().from(ForecastModel.class)
                .where(ForecastModel.COLUMN_DATE, model.mDate.toString())
                .where(ForecastModel.COLUMN_CITY_ID, model.mCityId)
                .executeSingle();
        if(fm != null){
            fm.mForecast = model.mForecast;
            fm.save();
        }
    }

    static void delete(String city_name){
        List<ForecastModel> fm_l = read(city_name, null, null);
        for(ForecastModel fm: fm_l){
            fm.delete();
        }
    }
}
