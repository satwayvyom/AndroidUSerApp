
package com.vyom.gpstrackersatway;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class LocationTracking extends FragmentActivity implements OnMapReadyCallback {
    private static final String URL_DATA = "http://68.183.92.233:8000/api/all_vehicle_locations";
    GoogleMap map;

    Button normalbtn, satellitebtn, terrbtn;
    JSONObject VehicleData,ImeiDetails;
    String username,Loge;
    ArrayList<String> vhno = new ArrayList<String>();
    ArrayList<Float> lon = new ArrayList<>();
    ArrayList<String> imeiarray = new ArrayList<>();
    ArrayList<String> vehiclearray = new ArrayList<>();
    ArrayList<Float> lat = new ArrayList<>();
    ArrayList<String> time_stamp = new ArrayList<String>();
    SharedPreferences sharedPreferences, VehicleDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final loader_class loader_class = new loader_class(LocationTracking.this);
        setContentView(R.layout.activity_mapview2);
        terrbtn = (Button) findViewById(R.id.terrainbtn);
        normalbtn = (Button) findViewById(R.id.normalbtn);
        satellitebtn = findViewById(R.id.abc);
        loader_class.startdialogue();
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        try {

             VehicleData = new JSONObject(sharedPreferences.getString("VehicleDetails", ""));
             ImeiDetails = new JSONObject(sharedPreferences.getString("ImeiDetails", ""));
        } catch (JSONException e) {

            e.printStackTrace();
            Loge+="Line 81 Catch - LocationTrackingPage - "+e.toString()+""+System.getProperty("line.separator");
        }
        satellitebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                satellitebtn.setBackgroundColor(getResources().getColor(R.color.yellow));
                terrbtn.setBackgroundColor(Color.WHITE);
                normalbtn.setBackgroundColor(Color.WHITE);
                // Do something in response to button click
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });


        terrbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                terrbtn.setBackgroundColor(getResources().getColor(R.color.yellow));
                satellitebtn.setBackgroundColor(Color.WHITE);
                normalbtn.setBackgroundColor(Color.WHITE);
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });

        normalbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                normalbtn.setBackgroundColor(getResources().getColor(R.color.yellow));
                satellitebtn.setBackgroundColor(Color.WHITE);
                terrbtn.setBackgroundColor(Color.WHITE);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            }
        });



        networkcall();

        //  //Text(com.example.gpstrackersatway.MapView.this, title, Toast.LENGTH_SHORT).show();

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
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        map = googleMap;

 int count = 0;

      for (int j = 0; j < lat.size(); j++) {


            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                if(lat.get(j)==0.0f){
                   count = count+1;
                   System.out.println(count+",,,,,,,,,,,,,,,,,,,,,");
//                   continue;
            }
                System.out.println(count+",,,,,,,,,,,,,,,,,,,,,");
                addresses = geocoder.getFromLocation(lat.get(j), lon.get(j), 1);
//                String address = addresses.get(0).getAddressLine(0);
//
//                String Known = addresses.get(0).getFeatureName();;
//                String city = addresses.get(0).getLocality();


                System.out.println(lat.size());
                System.out.println(lat.size());



                LatLng place = new LatLng(lat.get(j), lon.get(j));




                if(j-count==-1)
                {
                    continue;
                }

                map.addMarker(new MarkerOptions().position(place).title(vhno.get(j)).snippet(time_stamp.get(j-count)));
                System.out.println(vhno.get(j)+"kkkkkkkkkkkkkkkkkkkkk");
                System.out.println(time_stamp);


                Log.d("jishnu","--------------------------------------------------------lat="+lat.get(j)+"lon="+lon.get(j)+"vhno="+vhno.get(j));
System.out.println("--------------------------------------------------------lat="+lat.get(j)+"lon="+lon.get(j)+"vhno="+vhno.get(j));





                map.moveCamera(CameraUpdateFactory.newLatLng(place));
                CameraUpdate center = CameraUpdateFactory.newLatLng(place);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
                map.moveCamera(center);
                map.animateCamera(zoom);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                googleMap.getUiSettings().setScrollGesturesEnabled(true);
                googleMap.getUiSettings().setTiltGesturesEnabled(false);




            } catch (IOException e) {
                e.printStackTrace();
                Loge+="Line 298 Catch - LocationTrackingPage - "+e.toString()+""+System.getProperty("line.separator");


            }

            appendLog(Loge,"LocationTracking");
        }
        loader_class.dismissdig();
    }

    public void networkcall()
    {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(LocationTracking.this);


        final StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                System.out.println(response);
                int count=1;
                try {


                    JSONObject object1 = new JSONObject(response);//key:vehicle_no...compare and fetch vehicle number

                    JSONObject imeino = object1.getJSONObject("imei_locations");
                    Loge+="LocationTrackingResponse - "+imeino+""+System.getProperty("line.separator")+System.getProperty("line.separator");
                    System.out.println(imeino);
                    if(imeino.length() != 0) {

                        Iterator<String> ImeiIterator = imeino.keys();
                        Iterator<String> SharedImeiIterator = ImeiDetails.keys();


                        System.out.println(ImeiDetails.keys());

                        for (int i = 1; i <= ImeiDetails.length(); i++) {
                            imeiarray.add(ImeiDetails.getString("imei" + i));
                            vehiclearray.add(VehicleData.getString("vehicle" + i));
                        }
                        String jsonstring = "{";
                        for (int i = 0; i < imeiarray.size(); i++) {

                            if (i == (imeiarray.size() - 1)) {
                                jsonstring += "\"" + imeiarray.get(i) + "\":\"" + vehiclearray.get(i) + "\"}";
                            } else {
                                jsonstring += "\"" + imeiarray.get(i) + "\":\"" + vehiclearray.get(i) + "\",";
                            }
                        }


                        JSONObject VehicleImei = new JSONObject(jsonstring);
                        while (ImeiIterator.hasNext()) {
                            String key = ImeiIterator.next();
                            vhno.add(VehicleImei.getString(key));
                            if (imeino.getJSONArray(key).get(0).equals(0) || imeino.getJSONArray(key).get(1).equals(0)
                                    || imeino.getJSONArray(key).get(0).equals(null) || imeino.getJSONArray(key).get(1).equals(null)) {
//                                continue;
                                lat.add(0.0f);
                                lon.add(0.0f);



                            } else {
                                lat.add(Float.valueOf(String.valueOf(imeino.getJSONArray(key).get(0))));
                                lon.add(Float.valueOf(String.valueOf(imeino.getJSONArray(key).get(1))));

                                if (imeino.getJSONArray(key).get(2).equals(null) || imeino.getJSONArray(key).get(2).equals(0))
                                    time_stamp.add("2021-07-25T19:44:14");
                                else
                                    time_stamp.add((String) imeino.getJSONArray(key).get(2));
                            }
                        }
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(LocationTracking.this);
                    }
                    else
                    {
                        Toast.makeText(LocationTracking.this, "No Locations Available", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            networkcall();
            }
        }) {
            protected Map<String, String> getParams() {

                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", username);

                return MyData;
            }
        };

        int socketTimeout = 70000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        MyStringRequest.setRetryPolicy(policy);


        MyRequestQueue.add(MyStringRequest);

    }

}
