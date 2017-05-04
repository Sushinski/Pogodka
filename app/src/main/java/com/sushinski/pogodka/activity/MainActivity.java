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

public class MainActivity extends AppCompatActivity {
    private SelectCitiesFragment mSelectCitiesFargment;
    private boolean mMenuState;
    public static final String CITY_NAME = "com.sushinski.podvodka.CITY_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMenuState = true;
        if(findViewById(R.id.fragment_container) != null) {
            CityItemFragment mCitiesForecastFragment = new CityItemFragment();
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
        MenuItem addCitiesItem = menu.findItem(R.id.edit_cities_list);
        addCitiesItem.setVisible(mMenuState);
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
                toggleHomeButton(true);
                return true;
            case android.R.id.home:
                onBackPressed();
                toggleHomeButton(false);
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

    private void toggleHomeButton(boolean bToggle){
        // Enable the Up button
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(bToggle);
            mMenuState = !bToggle;
            invalidateOptionsMenu();
        }
    }

}
