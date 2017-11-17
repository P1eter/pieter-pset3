package com.example.pieter.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "https://resto.mprog.nl/categories";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new responsething(),
                new responseerror()
        );

        queue.add(stringRequest);
    }


    private class responsething implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            String[] courses = parseJson(response);
            setAdapter(courses);
        }
    }

    private class responseerror implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            setAdapter(new String[]{"Error!"});
        }
    }

//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        String[] courses = parseJson(response);
//                        setAdapter(courses);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        setAdapter(new String[]{"Error!"});
//                }
//        });



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

        coursesview.setOnItemClickListener(new listfunctionthing());
    }

    private class listfunctionthing implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(MainActivity.this, foodlist.class);
            intent.putExtra("course", adapterView.getItemAtPosition(i).toString());
            startActivity(intent);
        }

//        coursesview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(MainActivity.this, foodlist.class);
//                intent.putExtra("course", adapterView.getItemAtPosition(i).toString());
//                startActivity(intent);
//            }
//        });
    }

    public void toOrderPage(View view) {
        Intent intent = new Intent(this, check_order.class);
        startActivity(intent);
    }
}
