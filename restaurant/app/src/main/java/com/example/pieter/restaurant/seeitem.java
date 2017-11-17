package com.example.pieter.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class seeitem extends AppCompatActivity {

    String dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeitem);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String response = intent.getStringExtra("response");

        JSONObject dinges = getobject(response, name);
        dish = dinges.toString();

        try {
            int price = dinges.getInt("price");
            String description = dinges.getString("description");

            TextView nameview = findViewById(R.id.name);
            TextView priceview = findViewById(R.id.price);
            TextView descriptionview = findViewById(R.id.description);

            nameview.setText(name);
            priceview.setText("$" + Integer.toString(price));
            descriptionview.setText(description);
        } catch (Exception e) {

        }

        String url = "https://resto.mprog.nl/images/spaghetti.jpg";
        try {
            url = dinges.getString("image_url");
        } catch (Exception e) {

        }
        ImageView foodimage = findViewById(R.id.foodimage);
        Picasso.with(this).load(url).into(foodimage);
    }

    private JSONObject getobject(String response, String name) {
        try {
            JSONObject jObject = new JSONObject(response);
            JSONArray jArray = jObject.getJSONArray("items");

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject2 = jArray.getJSONObject(i);
                if (jObject2.getString("name").equals(name)) {
                    return jObject2;
                }
            }
        } catch (Exception e) {
        }
        return new JSONObject();
    }

    public void addthisitem(View view) {
        SharedPreferences prefs = getSharedPreferences("settings", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int n = prefs.getInt("n", -1);
        if (n != -1) {
            editor.putInt("n", n + 1);
            editor.putString("dish" + Integer.toString(n+1), dish);
        } else {
            editor.putInt("n", 1);
            editor.putString("dish1", dish);
        }
        editor.commit();
        finish();
    }
}
