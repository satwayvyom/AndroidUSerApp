

package com.vyom.gpstrackersatway;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.atan;

public class Map_History extends FragmentActivity implements OnMapReadyCallback {

    private static final String URL_DATA = "http://68.183.92.233:8000/api/vehicle_history";
    private static PolylineOptions polylineOptions = new PolylineOptions();

    GoogleMap map;
    Double Lat = 0.0, Lon = 0.0;
    Marker m;
    int x = 0;
    static long Delay;
    ArrayList<Double> longitude =new ArrayList<>();
    ArrayList<Double> latitude =new ArrayList<>();
    Button normalbtn, satellitebtn, terrbtn;
    SharedPreferences sharedPreferences;
    CameraUpdate center;
    CameraUpdate zoom;
    String username;
    long delay;


    ArrayList<LatLng> points = new ArrayList<LatLng>();
    String StartDate, EndDate, Vehiclenumber, imei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map__history);
        StartDate = getIntent().getStringExtra("StartDate");
        EndDate = getIntent().getStringExtra("EndDate");
        imei = getIntent().getStringExtra("imei");


        terrbtn = (Button) findViewById(R.id.terrainbtn);
        normalbtn = (Button) findViewById(R.id.normalbtn);
        satellitebtn = findViewById(R.id.abc);
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        satellitebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                satellitebtn.setBackgroundColor(getResources().getColor(R.color.purple1));
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
                terrbtn.setBackgroundColor(getResources().getColor(R.color.purple1));
                satellitebtn.setBackgroundColor(Color.WHITE);
                normalbtn.setBackgroundColor(Color.WHITE);
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });
        normalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalbtn.setBackgroundColor(getResources().getColor(R.color.purple1));
                satellitebtn.setBackgroundColor(Color.WHITE);
                terrbtn.setBackgroundColor(Color.WHITE);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            }
        });

        try {
            networkcall();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.slow:
                if (checked)
                    onMapReady(map);
                break;
            case R.id.medium:
                if (checked)
                    onMapReady(map);
                break;
            case R.id.fast:
                if (checked)

                    onMapReady(map);
                break;
        }
    }
    public void onMapReady(GoogleMap googleMap) {
        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));


        map = googleMap;
        m = null;
        points.clear();
        map.clear();
        polylineOptions = null;
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogrp);

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            //radioGroup.getChildAt(i).setEnabled(false);
        }
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
        String selectedText = (String) radioButton.getText();
//        if (selectedText.equals("Medium")) {
//            delay = 50;
//            points.clear();
//            map.clear();
//        } else if (selectedText.equals("Fast")) {
//
//            delay = 5;
//            points.clear();
//            map.clear();
//        } else if (selectedText.equals("Slow")) {
//
//            delay = 100;
//            points.clear();
//            map.clear();
//        }



        try {

            System.out.println(latitude+"-"+longitude);
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude.get(latitude.size()/2),longitude.get(longitude.size()/2)));

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(10.0f);

            map.setMinZoomPreference(15.0f);
            map.setMaxZoomPreference(20.0f);

            map.moveCamera(center);

          map.animateCamera(zoom);
          googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setTiltGesturesEnabled(false);
            polylineOptions = new PolylineOptions();
            polylineOptions.width(10f);
            polylineOptions.color(Color.BLUE);
            polylineOptions.geodesic(true);
            polylineOptions.getEndCap();
            polylineOptions.jointType(1);
            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("cartop", "drawable", getPackageName()));
            Bitmap icon = Bitmap.createScaledBitmap(imageBitmap, 40, 90, false);
            for (int i = 0; i < longitude.size(); i++) {


                if ((Double) latitude.get(i) > 0 && (Double) longitude.get(i) > 0) {
                    LatLng place = new LatLng((Double) latitude.get(i), (Double) longitude.get(i));
                    points.add(place);

                }
            }

           setAnimation(map, points, icon, delay);


//            for (int w = 0; w < radioGroup.getChildCount(); w++) {
//                radioGroup.getChildAt(w).setEnabled(true);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final Bitmap bitmap,  long delay) {
        Marker marker = myMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoint.get(0))
                .flat(true).title(""));

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 12));
        animateMarker(myMap, marker, directionPoint, false, delay);

    }

    public double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;

        double long2 = latLng2.longitude * PI / 180;
        double dLon = (long2 - long1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);
        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;


        return brng;
    }
    Float getRotation(LatLng start, LatLng end) {
        Double latDifference= abs(start.latitude - end.latitude);
        Double lngDifference= abs(start.longitude - end.longitude);
        Float rotation = -1F;
        if(start.latitude < end.latitude && start.longitude < end.longitude)
            {
                rotation = Float.parseFloat(String.valueOf(Math.toDegrees(atan(lngDifference / latDifference))));
            }
            else if(start.latitude >= end.latitude && start.longitude < end.longitude)
            {
                rotation = Float.parseFloat(String.valueOf(90 - Math.toDegrees(atan(lngDifference / latDifference)) + 90));
            }
            else if(start.latitude >= end.latitude && start.longitude >= end.longitude)
            {
                rotation = Float.parseFloat(String.valueOf(Math.toDegrees(atan(lngDifference / latDifference)) + 180));
            }
            else if (start.latitude < end.latitude && start.longitude >= end.longitude)
            {
                rotation = Float.parseFloat(String.valueOf(90 - Math.toDegrees(atan(lngDifference / latDifference)) + 270));
            }
        return rotation;
    }
    public void rotateMarker(final Marker marker, final float toRotation) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = marker.getRotation();
        final long duration = 1000;
        marker.setAnchor(0.5f, 0.5f);
        final Interpolator interpolator = new LinearInterpolator();


        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                marker.setRotation(-rot > 180 ? rot / 2 : rot);

                if (t < 1.0) {

                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });

    }
    private void animateMarker(final GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint, final boolean hideMarker, long delay)
    {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        Delay = 1500;
        List<LatLng> points=new ArrayList<>();

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {

            int i = 0;
            PolylineOptions polylineOptions1 = new PolylineOptions();
            @Override
            public void run() {
                System.out.println("-----------------------------------------------------");
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / 30000);
                if (i < directionPoint.size()) {
                    int x=0;
                    points.add(directionPoint.get(i));
                    if(i==directionPoint.size()-1){

                        x=i;
                    }
                    else
                    {
                        x=i+1;

                    }
                    //double bearing = getRotation(directionPoint.get(i), directionPoint.get(x));
                    double bearing = bearingBetweenLocations(directionPoint.get(i), directionPoint.get(x));
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(directionPoint.get(i).latitude,directionPoint.get(i).longitude));
                    map.moveCamera(center);
                    rotateMarker(marker, (float) bearing);

                    polylineOptions1.width(10f);
                    polylineOptions1.color(Color.BLUE);

                    polylineOptions1.geodesic(true);

                    polylineOptions1.getEndCap();

                    polylineOptions1.jointType(1);
                    polylineOptions1.add(directionPoint.get(i));
                    myMap.addPolyline(polylineOptions1);

                    marker.setPosition(directionPoint.get(i));

                }

                i++;

                //even after completing the marking in map ,due to this it was still continuing...if any error comes change it to (t<10.0)
                if (t < 1.0) {
                    System.out.println(t);


                    handler.postDelayed(this, Delay);


                } else
                    {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {


                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public void networkcall()
    {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(Map_History.this);
        final StringRequest request = new StringRequest(Request.Method.POST, URL_DATA, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                int i=0;String js ="{";
                try {

                    JSONObject object1 = new JSONObject(response);
                    JSONObject HistoryData = object1.getJSONObject("history");
                    Iterator<String> historyIterator = HistoryData.keys();
                    while (historyIterator.hasNext()){
                        String key = historyIterator.next();

                        JSONArray JA= HistoryData.getJSONArray(key);
                        int a=0,b=1,c=2,d=3,e=4;
                        while(e < HistoryData.getJSONArray(key).length())
                        {
                            if(a+5 != HistoryData.getJSONArray(key).length()) {
                                js += "\""+JA.getString(c) + "\":[\"" + JA.getString(a) + "\",\"" + JA.getString(b) + "\",\"" + JA.getString(d) + "\",\"" + JA.getString(e) + "\"],";
                            }
                            else
                            {
                                js += "\""+JA.getString(c) + "\":[\"" + JA.getString(a) + "\",\"" + JA.getString(b) + "\",\"" + JA.getString(d) + "\",\"" + JA.getString(e) + "\"]}";
                                break;
                            }
                            a+=5;
                            b+=5;
                            c+=5;
                            d+=5;
                            e+=5;

                        }
                    }

                    JSONObject TimeLatLon = new JSONObject(js);

                    ArrayList<String> TimeList = new ArrayList<>();
                    Iterator<String> TimeIterator = TimeLatLon.keys();
                    while (TimeIterator.hasNext()){
                        TimeList.add(TimeIterator.next());
                    }
                    Collections.sort(TimeList, Collections.<String>reverseOrder());

                    for(int w=TimeList.size()-1;w>=0;w--)
                    {
                        longitude.add(TimeLatLon.getJSONArray(TimeList.get(w)).getDouble(1));
                        latitude.add(TimeLatLon.getJSONArray(TimeList.get(w)).getDouble(0));
                    }
//


//                        LatList = object1.getJSONArray("latlst");
//                        LonList = object1.getJSONArray("long");
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Map_History.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                networkcall();
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("imei", imei);

                MyData.put("start",StartDate);
                System.out.println("---------------------------"+StartDate);
                MyData.put("end",EndDate);
                Log.d("jishnu",EndDate);
                MyData.put("mode","M");

                return MyData;
            }
        };
        int socketTimeout = 80000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MyRequestQueue.add(request);
    }
}