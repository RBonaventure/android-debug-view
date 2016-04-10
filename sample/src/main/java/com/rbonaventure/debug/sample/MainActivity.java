package com.rbonaventure.debug.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rbonaventure.debug.DebugView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DebugView actionBarDebugView = new DebugView(this);
        actionBarDebugView.setTextColor(getResources().getColor(android.R.color.white));

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(actionBarDebugView);
    }
}
