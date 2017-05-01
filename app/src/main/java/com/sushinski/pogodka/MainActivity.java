package com.sushinski.pogodka;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sushinski.pogodka.DL.POJO.CitiesItemField;
import com.sushinski.pogodka.interfaces.OnListFragmentInteractionListener;
import com.sushinski.pogodka.DL.models.CityModel;

public class MainActivity extends AppCompatActivity implements
        SelectCitiesFragment.OnEditCitiesListEndListener {
    private CityItemFragment mCitiesForecastFragment;
    private SelectCitiesFragment mSelectCitiesFargment;

    public static final String CITY_NAME = "com.sushinski.podvodka.CITY_NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.fragment_container) != null) {
            mCitiesForecastFragment = new CityItemFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                    beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mCitiesForecastFragment);
            fragmentTransaction.commit();
        }
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

    /** Process menu item selection
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit_cities_list:
                if(mSelectCitiesFargment == null){
                    mSelectCitiesFargment = new SelectCitiesFragment();
                }
                replaceMainFragment(mSelectCitiesFargment);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceMainFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        fragmentTransaction.setTransitionStyle(android.R.style.Animation_Activity);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Callback method called after cities list edition ends
     */
    @Override
    public void onEditCitiesListEnd(CitiesItemField field) {
        // let`s return cities forecast list back
        // update its content with new cities
        mCitiesForecastFragment.updateFragmentContent(field);
        // return it back to view
        // replaceMainFragment(mCitiesForecastFragment);
    }
}
