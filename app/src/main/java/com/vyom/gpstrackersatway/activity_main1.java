package com.vyom.gpstrackersatway;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Policy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class activity_main1 extends FragmentActivity implements OnMapReadyCallback {
    private static final String URL_DATA = "http://68.183.92.233:8000/api/load_history_start";
    String StartDate, EndDate, vehiclenumber, Status;

    String username,Log;
    String mode;
    Button play;
    JSONArray TripData;
    Geocoder geocoder;
    List<Address> addresses1,addresses2;
    JSONArray startAddress;
    JSONArray endAddress;
    TextView Vehiclenumbertxt,dist1;
    List<HistoryData> HD_List = new ArrayList<>();
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    JSONObject VehicleData,ImeiDetails,VehicleImei;

    List<String> user = new ArrayList<>();
    String imei ="";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.d("jishnu","hellooo from history");
        super.onCreate(savedInstanceState);
        loader_class loader_class = new loader_class(activity_main1.this);
        setContentView(R.layout.activity_main1);
        recyclerView = findViewById(R.id.recyclerView);
        Vehiclenumbertxt = findViewById(R.id.vehicletext);
        EndDate = getIntent().getStringExtra("EndDate");
        System.out.println(EndDate);
        StartDate = getIntent().getStringExtra("StartDate");
        System.out.println(StartDate);
        imei = getIntent().getStringExtra("imei");
        System.out.println(imei);
        Status = getIntent().getStringExtra("mode");
        System.out.println(Status);
        play = findViewById(R.id.play);
        loader_class.startdialogue();
        if (Status.equals("STOPPED")) {
            mode = "S";

        } else if (Status.equals("RUNNING")) {
            mode = "M";

        }
        Vehiclenumbertxt.setText(vehiclenumber);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView12);
        mapFragment.getMapAsync(activity_main1.this);
    }
    public void appendLog(String text,String page)
    {
        File logFile = new File("/storage/emulated/0/Android/data/com.vyom.gpstrackersatway/"+page+".txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log+="Line 120 Catch - activity_main1 - "+e.toString()+""+System.getProperty("line.separator");
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap map = googleMap;
        geocoder = new Geocoder(this, Locale.getDefault());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            private View view;
            private int position;
            @Override
            public void onClick(View view, int position) {
                HistoryData HD;
                HD = HD_List.get(position);
                Intent i = new Intent(activity_main1.this, Map_History.class);
                i.putExtra("imei", imei);
                i.putExtra("StartDate", HD.startDate);
                i.putExtra("EndDate", HD.endDate);
                i.putExtra("longitude", HD.getStartAddress());
                i.putExtra("latitude", HD.getStartAddress());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        sharedPreferences = getSharedPreferences("userdata",Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        try {
            networkcall();
    }

        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);

            Log+="Line 230 Catch - activity_main1 - "+e.toString()+""+System.getProperty("line.separator");



 }
     RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final Intent i = new Intent(activity_main1.this, Map_History.class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
 }

 public void networkcall()
 {
     RequestQueue MyRequestQueue = Volley.newRequestQueue(activity_main1.this);

     final StringRequest request = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {

         @Override
         public void onResponse(String response) {
             System.out.println(response);
             try {
                 JSONObject object1 = new JSONObject(response);

                 System.out.println(object1);

                 Log+="activity_main1 - "+object1.toString()+""+System.getProperty("line.separator")+System.getProperty("line.separator");
                 TripData = object1.getJSONArray(Status.toLowerCase());
                 for(int i=0;i<TripData.length();i++) {
                     if (Double.parseDouble(TripData.getJSONArray(i).getString(0)) != 0 && Double.parseDouble(TripData.getJSONArray(i).getString(1)) != 0
                             && Double.parseDouble(TripData.getJSONArray(i).getString(3)) != 0 && Double.parseDouble(TripData.getJSONArray(i).getString(4)) != 0) {
                         addresses1 = geocoder.getFromLocation(Double.valueOf(TripData.getJSONArray(i).getString(0)), Double.valueOf(TripData.getJSONArray(i).getString(1)), 1);
                         addresses2 = geocoder.getFromLocation(Double.valueOf(TripData.getJSONArray(i).getString(3)), Double.valueOf(TripData.getJSONArray(i).getString(4)), 1);
                         String city1 = addresses1.get(0).getLocality();
                         String city2 = addresses2.get(0).getLocality();
                         String Distance =TripData.getJSONArray(i).getString(6);

                         HistoryData HD = new HistoryData(TripData.getJSONArray(i).getString(2), TripData.getJSONArray(i).getString(5), city1, city2, TripData.getJSONArray(i).getString(5), TripData.getJSONArray(i).getString(5),Distance);
                         HD_List.add(HD);

                     }
                 }
                 loader_class.dismissdig();

                 HistoryData_Adapter HD_A = new HistoryData_Adapter(HD_List);
                 recyclerView.setAdapter(HD_A);
                 appendLog(Log,"activity_main1");
             }
             catch (JSONException | IOException e) {
                 e.printStackTrace();
                 Log+="Line 183 Catch - activity_main1 - "+e.toString()+""+System.getProperty("line.separator");
             }
         }

     }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
         @Override
         public void onErrorResponse(VolleyError error) {
             networkcall();
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



             /// System.out.println();

         }
     };
     int socketTimeout = 70000;
     RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
     request.setRetryPolicy(policy);
     MyRequestQueue.add(request);



 }

}
