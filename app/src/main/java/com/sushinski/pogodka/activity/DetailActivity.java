/*
* Created by Golubev Pavel, 2017
* No license applied
*/

package com.sushinski.pogodka.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.sushinski.pogodka.R;
import com.sushinski.pogodka.interfaces.OnDetailInteractionListener;


/**
 * Activity which holds detailed forecast fragment
 */
public class DetailActivity extends AppCompatActivity implements OnDetailInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public Bundle getFragmentParams(){
        Intent intent = getIntent();
        Bundle bnd = new Bundle();
        if(intent.hasExtra(MainActivity.CITY_NAME)){
            bnd.putString(MainActivity.CITY_NAME, intent.getStringExtra(MainActivity.CITY_NAME));
        }
        return bnd;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
