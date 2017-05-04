/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.DL.models;

/**
 * Class for city databse table row mapping
 */
public class CityModel {
    public static final String CHECKED = "1";
    public static final String UNCHECKED = "0";
    public String mCityName;
    public String mCityCode;
    public String mIsSelected;

    @Override
    public String toString() {
        return mCityName;
    }
}
