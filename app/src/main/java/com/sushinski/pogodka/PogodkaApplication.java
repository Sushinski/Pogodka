package com.sushinski.pogodka;

import android.app.Application;

import com.sushinski.pogodka.DAL.CityManager;

public class PogodkaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CityManager mngr = new CityManager(getApplicationContext());
        if(mngr.isEmptyTable()){
            mngr.populateDatabase();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
