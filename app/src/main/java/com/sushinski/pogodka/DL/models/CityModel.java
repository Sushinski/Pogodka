/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.DL.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Class for city databse table row mapping
 */
@Table(name=CityModel.TABLE_NAME, id=CityModel.ID)
public class CityModel extends Model{
    public static final String TABLE_NAME = "CityRec";
    public static final String ID = "_id";
    public static final String COLUMN_CITY_TITLE = "city_name";
    public static final String COLUMN_CITY_CODE = "city_code";
    public static final String COLUMN_CITY_SELECTED = "selected";
    public static final String CHECKED = "1";
    public static final String UNCHECKED = "0";

    @Column(name=COLUMN_CITY_TITLE, unique=true)
    public String mCityName;
    @Column(name=COLUMN_CITY_CODE)
    public String mCityCode;
    @Column(name=COLUMN_CITY_SELECTED)
    public String mIsSelected;

    @Override
    public String toString() {
        return mCityName;
    }
}
