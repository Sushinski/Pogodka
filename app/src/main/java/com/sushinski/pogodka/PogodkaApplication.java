package com.sushinski.pogodka;

import android.app.Application;

import com.sushinski.pogodka.DAL.CityManager;

public class PogodkaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDatabase();
    }


    private void initDatabase(){
        CityManager mngr = new CityManager(getApplicationContext());
        if(mngr.isEmptyTable()){
            mngr.populateDatabase();
            mngr.setCitySelection(CityManager.SAINT_PETERBURG, true);
            mngr.setCitySelection(CityManager.MOSCOW, true);
        }
    }
}
