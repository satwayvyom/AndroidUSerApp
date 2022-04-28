package com.vyom.gpstrackersatway;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table_Details extends AppCompatActivity {

    private static final String URL_DATA = "http://68.183.92.233:8000/vehicle_history";
    String StartDate,EndDate,vehiclenumber;
    private RecyclerView HistoryrecyclerView;
    HistoryData_Adapter History;
    String username;
    SharedPreferences sharedPreferences;
    List<String> user = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("jishnu","hello");
        super.onCreate(savedInstanceState);
        HistoryrecyclerView = findViewById(R.id.HistoryDetails);
        EndDate = getIntent().getStringExtra("EndDate");
        StartDate = getIntent().getStringExtra("StartDate");
        vehiclenumber = getIntent().getStringExtra("VehicleNum");
        System.out.println(vehiclenumber);
        System.out.println(StartDate);
        System.out.println(EndDate);
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        networkcall();


    }
    public void networkcall()
    {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(Table_Details.this);
        final StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override


            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject obj = new JSONObject(response);

                } catch (Exception e) {
                }
            }


        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                networkcall();
//                //Text(Vehicle_Details.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("vn", vehiclenumber);
                MyData.put("datefrom",StartDate);
                MyData.put("dateto",EndDate);//
                return MyData;
            }
        };


        MyRequestQueue.add(MyStringRequest);
    }
}