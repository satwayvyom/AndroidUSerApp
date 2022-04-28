package com.vyom.gpstrackersatway;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.circularreveal.CircularRevealLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


    public class  MapView extends FragmentActivity implements OnMapReadyCallback {
    private static final String URL_DATA = "http://68.183.92.233:8000/api/vehicle_location";


    GoogleMap map;
    String IMEInumber;
    String vehicle_no;
    Button normalbtn, satellitebtn, terrbtn;
    Double Lat,Lon,dlan,dlon;
    String username,Log;
    int ignition;
    Double x=11.00,y=67.00,speed,distance;
    String mode;
    SharedPreferences sharedPreferences;

    Handler handler = new Handler();
    Runnable runnable;
    int delay =30000;
    ArrayList<LatLng> points=new ArrayList<LatLng>();
    RequestQueue MyRequestQueue;
    StringRequest MyStringRequest;
    ArrayList<Double> Latset=new ArrayList<Double>();
    ArrayList<Double> Lonset=new ArrayList<Double>();
    Marker m;
    int c=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        loader_class loader_class=new loader_class(MapView.this);
        vehicle_no = getIntent().getStringExtra("vehicle_no");

        IMEInumber = getIntent().getStringExtra("imei");

        setContentView(R.layout.activity_map_view);
        terrbtn = (Button) findViewById(R.id.terrainbtn);
        normalbtn = (Button) findViewById(R.id.normalbtn);
        satellitebtn= findViewById(R.id.abc);
        loader_class.startdialogue();
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        LocationUpdator();


            satellitebtn.setOnClickListener(new View.OnClickListener() {
                @Override
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
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }


            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext();
                LinearLayout info = new CircularRevealLinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                //marker.remove();
                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        if (Lat > 0 && Lon > 0) {
            try {

                 addresses = geocoder.getFromLocation(Lat, Lon, 1);
                String Known = addresses.get(0).getFeatureName();

                String city = addresses.get(0).getLocality();
                PolylineOptions polylineOptions = null;
                String modes = null;
                String snip = null;



                if (ignition == 1) {
                    if (mode.equals("H")) {

                        modes = "Idle";
                    } else if (mode.equals("S")) {

                        modes = "Stopped";
                    } else if (mode.equals("M")) {
                        modes = "Running";

                    }

                    if (city == null) {
                        snip = "\nRoad : " + Known + "\nSpeed : " + speed + "\nIgnition : " + "ON" + "\nStatus : " + modes+ "\nDistance : " + distance;
                    } else {
                        snip = "City : " + city + "\nRoad : " + Known + "\nSpeed : " + speed + "\nIgnition : " + "ON" + "\nStatus : " + modes+ "\nDistance : " + distance;
                    }


                } else if (ignition == 0) {
                    if (mode.equals("H")) {

                        modes = "Idle";
                    } else if (mode.equals("S")) {

                        modes = "Stopped";
                    } else if (mode.equals("M")) {
                        modes = "Running";

                    }

                    if (city == null) {
                        snip = "\nRoad : " + Known + "\nSpeed : " + speed + "\nIgnition : " + "ON" + "\nStatus :" + modes+"\nDistance : " + distance;
                    } else {
                        snip = "City : " + city + "\nRoad : " + Known + "\nSpeed : " + speed + "\nIgnition : " + "OFF" + "\nStatus :"+ modes+ "\nDistance : " + distance;
                    }
                }
                LatLng place = new LatLng(Lat, Lon);
                if (place != null) {
                    points.add(place);
                }
                loader_class.dismissdig();
                if (c == 0) {
                    Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("cartop", "drawable", getPackageName()));
                    Bitmap icon = Bitmap.createScaledBitmap(imageBitmap, 40, 90, false);
                    m = map.addMarker(new MarkerOptions().position(place).title(vehicle_no).snippet(snip));
                    m.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Lat, Lon));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
                    // Set listeners for click events.
                    map.moveCamera(center);
                    map.animateCamera(zoom);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setRotateGesturesEnabled(true);
                    googleMap.getUiSettings().setScrollGesturesEnabled(true);
                    googleMap.getUiSettings().setTiltGesturesEnabled(false);



                } else {
                    m.remove();
                    Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("cartop", "drawable", getPackageName()));
                    Bitmap icon = Bitmap.createScaledBitmap(imageBitmap, 40, 90, false);
                    m = map.addMarker(new MarkerOptions().position(place).title(vehicle_no).snippet(snip).anchor(0.5f, 0.5f));
                    m.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                    CameraUpdate center = CameraUpdateFactory.newLatLng(place);
                    polylineOptions = new PolylineOptions();
                    polylineOptions.add(points.get(c - 1));
                    polylineOptions.add(points.get(c));
                    polylineOptions.width(15f);
                    polylineOptions.jointType(1);
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.geodesic(true);
                    polylineOptions.getEndCap();
                    polylineOptions.jointType(1);
                    map.addPolyline(polylineOptions);
                    map.moveCamera(center);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setRotateGesturesEnabled(true);
                    googleMap.getUiSettings().setScrollGesturesEnabled(true);
                    googleMap.getUiSettings().setTiltGesturesEnabled(false);
                }
                c++;

            } catch (Exception e) {

                e.printStackTrace();
                Log+="Line 308 Catch - MapView - "+e.toString()+""+System.getProperty("line.separator");
            }


        }
        appendLog(Log,"MapView");
    }


        @Override
        protected void onResume() {

            handler.postDelayed(runnable = new Runnable() {
                public void run() {
                    handler.postDelayed(runnable, delay);


                    LocationUpdator();
                }
            }, delay);
            super.onResume();
        }
    @Override
    protected void onStop() {
        handler.removeCallbacksAndMessages(null);
        super.onStop();
    }
    private void LocationUpdator()
    {

             networkcall();
    }

    private void networkcall()
    {
        MyRequestQueue = Volley.newRequestQueue(MapView.this);


        MyStringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object1 = new JSONObject(response);
                    Log+="MapViewResponse - "+object1.toString()+""+System.getProperty("line.separator")+System.getProperty("line.separator");

                    System.out.println("=========="+response);
                    if(object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).get(0).equals(0)
                            || object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).get(1).equals(0)
                            ||object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).get(0).equals(null)
                            || object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).get(1).equals(null)
                            || object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).get(2).equals(0)
                            || object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).get(2).equals(null))
                    {
                        Toast.makeText(MapView.this, "NO DATA FOR THIS VEHICLE", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Lat = (Double) object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).getDouble(0);
                        Lon = (Double) object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).getDouble(1);
                        speed = object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).getDouble(3);
                        mode = (String) object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).get(2);
                        ignition = object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).getInt(4);
                        distance = object1.getJSONObject("vehicle_location").getJSONArray(IMEInumber).getDouble(5);
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MapView.this);
                    }

                } catch (JSONException |NullPointerException e) {

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
                MyData.put("imei", IMEInumber);



                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

}
