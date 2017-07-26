/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.DL.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Class for forecst database row mapping
 */
@Table(name= ForecastModel.TABLE_NAME, id=ForecastModel.ID)
public class ForecastModel extends Model{
    public static final String ID = "_id";
    public static final String TABLE_NAME = "ForecastRec";
    public static final String COLUMN_CITY_ID = "city_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_FORECAST_DATA = "forecast_data";

    @Column(name=COLUMN_CITY_ID)
    public Integer mCityId;

    @Column(name=COLUMN_DATE)
    public Long mDate;

    @Column(name = COLUMN_FORECAST_DATA)
    public String mForecast;

    @Override
    public String toString() {
        return mCityId.toString();
    }
}
