package com.sushinski.pogodka.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.sushinski.pogodka.fragment.CityItemFragment;
import com.sushinski.pogodka.R;
import com.sushinski.pogodka.fragment.SelectCitiesFragment;


/**
 * Main activity holds list fragment with cities with current day short forecast
 */
public class MainActivity extends AppCompatActivity {
    private SelectCitiesFragment mSelectCitiesFargment;
    private boolean mMenuState;
    public static final String CITY_NAME = "com.sushinski.podvodka.CITY_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMenuState = true;
        // add cities forecast fragment initially
        if(findViewById(R.id.fragment_container) != null) {
            CityItemFragment mCitiesForecastFragment = new CityItemFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                    beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mCitiesForecastFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem addCitiesItem = menu.findItem(R.id.edit_cities_list);
        // adjust menu items visibility
        addCitiesItem.setVisible(mMenuState);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit_cities_list:
                if(mSelectCitiesFargment == null){
                    mSelectCitiesFargment = new SelectCitiesFragment();
                }
                // replace cities forecast frag with select cities list fragment
                replaceMainFragment(mSelectCitiesFargment);
                // adjust menu items for it
                toggleHomeAndMenu(true);
                return true;
            case android.R.id.home:
                onBackPressed();
                toggleHomeAndMenu(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Makes fragment exchange transaction with current visible fragment
     * @param fragment fragment to show instead visible
     */
    private void replaceMainFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        fragmentTransaction.setTransitionStyle(android.R.style.Animation_Activity);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Toggles home button and cities menu item
     * @param bToggle state to be toggled
     */
    private void toggleHomeAndMenu(boolean bToggle){
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(bToggle);
            mMenuState = !bToggle;
            invalidateOptionsMenu();
        }
    }
}
