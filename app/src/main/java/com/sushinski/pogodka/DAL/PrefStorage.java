package com.sushinski.pogodka.DAL;

class PrefStorage {
    static final String OPENWEATHER_API_KEY_KEY = "api_key";

    static String getPref(String key){
        String res = null; // todo: prefs based on database table
        if(key.equals(OPENWEATHER_API_KEY_KEY)){
            res = "865caab6c9049dd31fee757cf4bbd380";
        }
        return res;
    }
}
