package com.sushinski.pogodka.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sushinski.pogodka.R;

/**
 *  Splash screen class, showed on app startup
 */
public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onResume(){
        TextView tw = (TextView) findViewById(R.id.app_title);
        final long changeTime = 1000L;
        // delay launch second activity by 1s
        tw.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, changeTime);
        super.onResume();
    }
}
