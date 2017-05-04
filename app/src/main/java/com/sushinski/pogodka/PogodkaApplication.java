/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka;

import android.app.Application;
import com.sushinski.pogodka.DAL.CityManager;

/**
 * main pogodka application class
 */
public class PogodkaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // inits database
        initDatabase();
    }

    /**
     * populates tables with cities content(uploads from assets/city.list.json);
     * sets default selection for saint-petersburg and moscow
     */
    private void initDatabase(){
        CityManager mngr = new CityManager(getApplicationContext());
        if(mngr.isEmptyTable()){
            mngr.populateDatabase();
            mngr.setCitySelection(CityManager.SAINT_PETERBURG, true);
            mngr.setCitySelection(CityManager.MOSCOW, true);
        }
    }
}
