package com.sushinski.pogodka.DL.models;

public class ForecastModel {
    public Integer mCityId;
    public Long mDate;
    public String mForecast;

    @Override
    public String toString() {
        return mCityId.toString();
    }
}