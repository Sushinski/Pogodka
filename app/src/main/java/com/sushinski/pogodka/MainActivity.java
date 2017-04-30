package com.sushinski.pogodka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.sushinski.pogodka.DL.POJO.CitiesItemField;
import com.sushinski.pogodka.interfaces.OnListFragmentInteractionListener;
import com.sushinski.pogodka.DL.models.CityModel;

public class MainActivity extends AppCompatActivity {

    public static final String CITY_NAME = "com.sushinski.podvodka.CITY_NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Inflates menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
