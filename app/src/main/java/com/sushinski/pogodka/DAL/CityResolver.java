package com.sushinski.pogodka.DAL;

/**
 * Created by пк on 20.04.2017.
 */

public class CityResolver {
    public static String getCityIdByName(String city_name){
        String res = null; // todo: get codes from database table
        switch(city_name){
            case("Sankt-Peterburg"):
                res = "536203";
                break;
            case("Moscow"):
                res = "5601538";
                break;
            default:
                res = null;
        }
        return res;
    }
}
