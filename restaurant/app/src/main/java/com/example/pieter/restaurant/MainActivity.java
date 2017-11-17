package com.example.pieter.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import com.android.volley.*;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "https://resto.mprog.nl/categories";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] courses = parseJson(response);
                        setAdapter(courses);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setAdapter(new String[]{"Error!"});
            }
        });
        queue.add(stringRequest);



    }

    private String[] parseJson(String response) {
        String[] result;
        try {
            JSONObject jObject = new JSONObject(response);
            JSONArray jArray = jObject.getJSONArray("categories");
            result = new String[jArray.length()];
            for (int i = 0; i < jArray.length(); i++) {
                result[i] = jArray.getString(i);
            }
        } catch (Exception e) {
            result = new String[1];
            result[0] = "Error";
        }
        return result;
    }

    private void setAdapter(String[] courses) {
        ListView coursesview = findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, courses);
        coursesview.setAdapter(adapter);

        coursesview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, foodlist.class);
                intent.putExtra("course", adapterView.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });
    }

    public void toOrderPage(View view) {
        Intent intent = new Intent(this, check_order.class);
        startActivity(intent);

//        System.out.println("YOU PUSHED THE BUTTON");
//        SharedPreferences prefs = getSharedPreferences("settings", this.MODE_PRIVATE);
//        int n = prefs.getInt("n", -1);
//        System.out.println("n = " + n);
//        System.out.println(prefs.getString("dish1", "ERROR"));
//        System.out.println("GOING TO FOR LOOP");
//        if (n != -1) {
//            for (int i = 0; i < n; i++) {System.out.println("dish" + Integer.toString(i));
//                System.out.println(prefs.getString("dish" + Integer.toString(i+1), null));
//            }
//        }
    }
}
