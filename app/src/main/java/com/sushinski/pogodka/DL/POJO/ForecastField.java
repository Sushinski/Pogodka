package com.sushinski.pogodka.DL.POJO;

/**
 * class describes limited forecast field for exchanging purposes
 */
public class ForecastField {
    public static final String CELSIUM = "\u00B0 C"; // unicode celsium symbol
    public long day_in_ms;
    public double day_temp;
    public double min_temp;
    public double max_temp;
    public double night_temp;
    public double eve_temp;
    public double morn_temp;
    public double pressure;
    public double humidity;
    public String main_descr;
    public String detailed_descr;
    public double wind_speed;
    public double deg;
    public double clouds;
    public double rain;
}
