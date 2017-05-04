package com.sushinski.pogodka.DAL;

/**
 * Application preferencies storage class
 *
 */
class PrefStorage {
    static final String OPENWEATHER_API_KEY_KEY = "api_key";

    /**
     * Gets key-value request result
     * @param key
     * @return
     */
    static String getPref(String key){
        String res = null; // todo: prefs based on database table
        if(key.equals(OPENWEATHER_API_KEY_KEY)){
            res = "865caab6c9049dd31fee757cf4bbd380"; // actually uses pre-defined number
        }
        return res;
    }
}
