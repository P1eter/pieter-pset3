package com.example.pieter.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class foodlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);

        Intent intent = getIntent();
        String course = intent.getStringExtra("course");

        String url = "https://resto.mprog.nl/menu";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> dishnames = parseJson(response);
                        setAdapter(dishnames, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ArrayList<String> dishnames = new ArrayList<>();
                dishnames.add("Error!");
                setAdapter(dishnames, "Error");
            }
        });
        queue.add(stringRequest);
    }

    private ArrayList<String> parseJson(String response) {
        Intent intent = getIntent();
        String course = intent.getStringExtra("course");

        ArrayList<String> result;
        try {
            JSONObject jObject = new JSONObject(response);
            JSONArray jArray = jObject.getJSONArray("items");
            result = new ArrayList<>();

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject2 = jArray.getJSONObject(i);
                if (jObject2.getString("category").equals(course)) {
                    result.add(jObject2.getString("name"));
                }
            }
        } catch (Exception e) {
            result = new ArrayList<>();
            result.add("Error");
        }
        return result;
    }

    private void setAdapter(final ArrayList<String> courses, final String response) {
        ListView dishview = findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, courses);
        dishview.setAdapter(adapter);

        dishview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(foodlist.this, seeitem.class);
                intent.putExtra("name", adapterView.getItemAtPosition(i).toString());
                intent.putExtra("response", response);

                startActivity(intent);
            }
        });
    }
}
