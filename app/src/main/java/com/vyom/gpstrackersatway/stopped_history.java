package com.vyom.gpstrackersatway;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class stopped_history extends FragmentActivity implements OnMapReadyCallback {
    private static final String URL_DATA = "http://68.183.92.233:8000/api/load_history_stop";
    String StartDate, EndDate, vehiclenumber, Status;
    String username;
    String mode;
    Button play;
    JSONArray TripData;

    JSONArray avgSpeed;
    JSONArray topSpeed;
    JSONArray startAddress;
    JSONArray endAddress;
    TextView Vehiclenumbertxt;
    Geocoder geocoder;

    List<MyStop_Data> SD_List = new ArrayList<>();
    List<HistoryData> HD_List = new ArrayList<>();
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    List<String> user = new ArrayList<>();
    String imei ="";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopped_history);
        loader_class loader_class=new loader_class(stopped_history.this);
        recyclerView = findViewById(R.id.recyclerView1);
        Vehiclenumbertxt = findViewById(R.id.vehicletext);

        EndDate = getIntent().getStringExtra("EndDate");
        StartDate = getIntent().getStringExtra("StartDate");
        Status = getIntent().getStringExtra("mode");
        imei = getIntent().getStringExtra("imei");
        play = findViewById(R.id.play);
        loader_class.startdialogue();

        if (Status.equals("STOPPED")) {
            mode = "S";
        } else if (Status.equals("RUNNING")) {
            mode = "M";

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(stopped_history.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap map = googleMap;
        System.out.println("getting in");
        geocoder = new Geocoder(this, Locale.getDefault());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            private View view;
            private int position;

            @Override
            public void onClick(View view, int position) {
                MyStop_Data SD;
                SD=SD_List.get(position);
                Intent i = new Intent(stopped_history.this,HistoryStop_map.class);
                i.putExtra("time",SD.endDate);
                i.putExtra("latitude",Double.parseDouble(SD.getEndLat()));
                i.putExtra("longitude",Double.parseDouble(SD.getEndLon()));
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        try {
            RequestQueue MyRequestQueue = Volley.newRequestQueue(stopped_history.this);
            final StringRequest request = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject object1 = new JSONObject(response);
                        TripData = object1.getJSONArray(Status.toLowerCase());
                        for(int i=0;i<TripData.length();i++) {
                            if (Double.parseDouble(TripData.getJSONArray(i).getString(0)) != 0 && Double.parseDouble(TripData.getJSONArray(i).getString(1)) != 0) {
                                List<Address> addresses = geocoder.getFromLocation(Double.valueOf(TripData.getJSONArray(i).getString(0)), Double.valueOf(TripData.getJSONArray(i).getString(1)), 1);
                                String city = addresses.get(0).getLocality();
                                if(city == null)
                                {
                                    city="No Proper City";
                                }
                                MyStop_Data HD = new MyStop_Data(TripData.getJSONArray(i).getString(2),city,TripData.getJSONArray(i).getString(0),TripData.getJSONArray(i).getString(1));
                                SD_List.add(HD);
                            }
                        }
                        loader_class.dismissdig();
                        MyStop_Adapter SD_A = new MyStop_Adapter(SD_List);
                        recyclerView.setAdapter(SD_A);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("start", StartDate);
                    MyData.put("end", EndDate);
                    MyData.put("mode",mode);
                    MyData.put("imei", imei);
                    return MyData;
                }
            };
            int socketTimeout = 70000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            MyRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        final Intent i = new Intent(stopped_history.this, HistoryStop_map.class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


}
