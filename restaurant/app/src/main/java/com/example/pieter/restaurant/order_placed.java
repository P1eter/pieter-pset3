package com.example.pieter.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class order_placed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        Intent intent = getIntent();
        int time = intent.getIntExtra("waittime", 0);

        TextView view = findViewById(R.id.resultingtimetext);
        view.setText("Your order has been placed! Preparations will take approximately " + time + " minutes.");
    }
}
