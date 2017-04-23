package com.sushinski.pogodka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sushinski.pogodka.dummy.DummyContent;
import com.sushinski.pogodka.interfaces.OnListFragmentInteractionListener;

public class MainActivity extends AppCompatActivity
implements OnListFragmentInteractionListener {

    public static final String CITY_NAME = "com.sushinski.podvodka.CITY_NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Intent detail = new Intent(this, DetailActivity.class);
        detail.putExtra(CITY_NAME, item.toString());
        startActivity(detail);
    }
}
