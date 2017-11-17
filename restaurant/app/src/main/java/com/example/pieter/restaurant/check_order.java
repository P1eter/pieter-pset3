package com.example.pieter.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class check_order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);

        updateList();
    }

    private void updateList() {
        String[] dishlist;
        String[] pricelist;

        SharedPreferences prefs = getSharedPreferences("settings", this.MODE_PRIVATE);
        int n = prefs.getInt("n", -1);


        if (n == -1) {
            dishlist = new String[1];
            pricelist = new String[0];
            dishlist[0] = "Nothing was added yet!";
        } else {
            dishlist = new String[n];
            pricelist = new String[n];
            for (int i = 0; i < n; i++) {
                String item = prefs.getString("dish" + Integer.toString(i+1), "error");
                String name = "error";
                int price = 0;

                if (!item.equals("error")) {
                    JSONObject obj;
                    try {
                        obj = new JSONObject(item);
                        name = obj.getString("name");
                        price = obj.getInt("price");
                    } catch (Exception e) {
                        break;
                    }
                }
                dishlist[i] = name;
                pricelist[i] = "$" + Integer.toString(price) + ".00";
            }
        }

        ListView dishes = findViewById(R.id.disheslist);
        ListView prices = findViewById(R.id.prizeslist);

        ArrayAdapter<String> dishadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dishlist);
        ArrayAdapter<String> priceadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pricelist);

        dishes.setAdapter(dishadapter);
        prices.setAdapter(priceadapter);
    }

    public void clearlists(View view) {
        SharedPreferences prefs = getSharedPreferences("settings", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        this.updateList();
    }

    public void sendorder(View view) {
        String url = "https://resto.mprog.nl/order";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int preptime = 0;
                        try {
                            JSONObject obj = new JSONObject(response);
                            preptime = obj.getInt("preparation_time");
                        } catch (Exception e) {

                        }
                        Intent intent = new Intent(check_order.this, order_placed.class);
                        intent.putExtra("waittime", preptime);
                        clearlists(findViewById(R.id.clearbutton));
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
}
