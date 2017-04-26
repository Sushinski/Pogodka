package com.sushinski.pogodka;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import com.sushinski.pogodka.interfaces.OnDetailInteractionListener;
import static com.sushinski.pogodka.MainActivity.CITY_NAME;

public class DetailActivity extends AppCompatActivity implements OnDetailInteractionListener{
    String mCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
    }

    @Override
    public Bundle getFragmentParams(){
        Intent intent = getIntent();
        Bundle bnd = new Bundle();
        if(intent.hasExtra(CITY_NAME)){
            bnd.putString(CITY_NAME, intent.getStringExtra(CITY_NAME));
        }
        return bnd;
    }

}
