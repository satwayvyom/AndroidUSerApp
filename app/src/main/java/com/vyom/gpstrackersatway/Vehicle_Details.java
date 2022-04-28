   package com.vyom.gpstrackersatway;

   import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
   import android.util.Log;
   import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

   public class Vehicle_Details extends AppCompatActivity implements OnMapReadyCallback {
       GoogleMap map;
       TextView textView;
       Dialog dialog;
       ImageButton searchbtn;
       TextView speedtxt;
       TextView timetxt;
       TextView statusbtn;
       TextView powerstatusbtn;
       Button livetrack;
       List<String> user = new ArrayList<>();
       AutoCompleteTextView autoCompleteTextView;
       SharedPreferences sharedPreferences;
       String username;
       FrameLayout mapframe;
       View map1;
       JSONObject VehicleData,ImeiDetails,VehicleImei;
       Double Lat=0.0, Lon=0.0;
       Marker m;
       ArrayList <String>arr = new ArrayList<>();
       ArrayList <String>array2 = new ArrayList<>();
       ArrayList<String> imeiarray = new ArrayList<>();
       ArrayList<String> vehiclearray = new ArrayList<>();
//       ArrayList<Float> Log = new ArrayList<>();
       String imei = "";

       String speed;
       String ctime;
       String mode;
       String powerstatus;

       final String url2 = "http://68.183.92.233:8000/api/vehicle_live_track";

       @Override
       protected void onCreate(Bundle savedInstanceState) {

           super.onCreate(savedInstanceState);
           sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
           username = sharedPreferences.getString("username", "");
           setContentView(R.layout.activity_vehicle__details);
           searchbtn = (ImageButton) findViewById(R.id.searchbtn);
           autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.vehicleno);
           speedtxt = (TextView) findViewById(R.id.textView7);
           timetxt = (TextView) findViewById(R.id.txttime);
           statusbtn = (TextView) findViewById(R.id.txtspeed);
           powerstatusbtn = (TextView) findViewById(R.id.textView12);

           try {
               VehicleData = new JSONObject(sharedPreferences.getString("VehicleDetails", ""));

              for(int i =0;i<VehicleData.length();i++) {


                  System.out.println(VehicleData.get("vehicle"+Integer.parseInt(String.valueOf(i+1)))+"........................");
                  arr.add((String)VehicleData.get("vehicle"+Integer.parseInt(String.valueOf(i+1))));


              }


               ImeiDetails = new JSONObject(sharedPreferences.getString("ImeiDetails", ""));

               for(int i=1;i<=ImeiDetails.length();i++)
               {
                   imeiarray.add(ImeiDetails.getString("imei"+i));
                   vehiclearray.add(VehicleData.getString("vehicle"+i));
               }

               String jsonstring = "{";
               for (int i = 0; i < imeiarray.size(); i++) {
                   if (i == (imeiarray.size() - 1)) {
                       jsonstring += "\"" + vehiclearray.get(i) + "\":\"" + imeiarray.get(i) + "\"}";
                   } else {
                       jsonstring += "\"" + vehiclearray.get(i) + "\":\"" + imeiarray.get(i) + "\",";
                   }
               }
               VehicleImei = new JSONObject(jsonstring);


           } catch (JSONException e) {
               e.printStackTrace();
           }
           livetrack = findViewById(R.id.map);
          final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                 (this, android.R.layout.select_dialog_item, arr);

//           Getting the instance of AutoCompleteTextView
          autoCompleteTextView.setThreshold(1);//will start working from first character
           autoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

           searchbtn.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {
                   networkcall();
               }
           });


           livetrack.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v)
               {
                   if (autoCompleteTextView.getText().toString().equals(""))
                       {
                           Toast.makeText(Vehicle_Details.this, "Enter Details", Toast.LENGTH_SHORT).show();
                       }
                   else
                       {
                           Intent intent = new Intent(Vehicle_Details.this, MapView.class);
                           intent.putExtra("vehicle_no",autoCompleteTextView.getText().toString());



                               try
                                   {
                                       intent.putExtra("imei", VehicleImei.getString(autoCompleteTextView.getText().toString()));
                                       System.out.println(VehicleImei.getString(autoCompleteTextView.getText().toString())+"....................................");
                                   }
                               catch (JSONException e)
                                   {
                                       e.printStackTrace();
                                   }
                           startActivity(intent);
                       }
               }
           });


       }

       @Override
       public void onMapReady(GoogleMap googleMap) {
           boolean success = googleMap.setMapStyle(
                   MapStyleOptions.loadRawResourceStyle(
                           this, R.raw.style_json));
           System.out.println((Lat+"----"+Lon));
           map = googleMap;
           Geocoder geocoder;
           geocoder = new Geocoder(this, Locale.getDefault());
           try {
               LatLng place = new LatLng(Lat,Lon);
               System.out.println(place);
                   m = map.addMarker(new MarkerOptions().position(place).title(""));
                   CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Lat,Lon));
                   CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
                   // Set listeners for click events.
                   map.moveCamera(center);
                   map.animateCamera(zoom);
                   googleMap.getUiSettings().setZoomControlsEnabled(true);

                   googleMap.getUiSettings().setRotateGesturesEnabled(true);
                   googleMap.getUiSettings().setScrollGesturesEnabled(true);
                   googleMap.getUiSettings().setTiltGesturesEnabled(false);


           } catch (Exception e) {


               e.printStackTrace();
           }

       }

       public void networkcall()
       {

           RequestQueue MyRequestQueue = Volley.newRequestQueue(Vehicle_Details.this);
           final StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
               @SuppressLint({"ResourceAsColor", "ResourceType"})
               @RequiresApi(api = Build.VERSION_CODES.KITKAT)
               @Override

               public void onResponse(String response) {
                   System.out.println(response);
                   Log.d("jishnu","request for live track successfull");
                   try {
                       JSONObject obj = new JSONObject(response);
                       Log.d("jishnu","request for live track successfull inside try");

                       Lat = Double.parseDouble(obj.getJSONObject("vehicle_live_track").getJSONArray(String.valueOf(autoCompleteTextView.getText())).get(0).toString());
                       Log.d("jishnu",Lat.toString());
                       Lon = Double.parseDouble(obj.getJSONObject("vehicle_live_track").getJSONArray(String.valueOf(autoCompleteTextView.getText())).get(1).toString());
                       Log.d("jishnu",Lon.toString());
                       ctime =  obj.getJSONObject("vehicle_live_track").getJSONArray(String.valueOf(autoCompleteTextView.getText())).get(2).toString();
                       Log.d("jishnu",ctime.toString());
                       speed = obj.getJSONObject("vehicle_live_track").getJSONArray(String.valueOf(autoCompleteTextView.getText())).get(3).toString();
                       Log.d("jishnu",speed.toString());
                       mode =  obj.getJSONObject("vehicle_live_track").getJSONArray(String.valueOf(autoCompleteTextView.getText())).get(4).toString();
                       Log.d("jishnu",mode.toString());
                       powerstatus =  obj.getJSONObject("vehicle_live_track").getJSONArray(String.valueOf(autoCompleteTextView.getText())).get(5).toString();

                    Log.d("jishnu",powerstatus.toString());



                       if (powerstatus.equals("1")) {

                           powerstatusbtn.setText("Main's Connected");
                           powerstatusbtn.setTextColor(Color.GREEN);

                       } else if (powerstatus.equals("0")) {


                           powerstatusbtn.setText("Main's Disconnected");
                           powerstatusbtn.setTextColor(Color.RED);
                       }

                       if (mode.equals("H")) {

                           statusbtn.setText("IDLE");
                       } else if (mode.equals("S")) {

                           statusbtn.setText("STOPPED");
                       } else if (mode.equals("M")) {
                           Log.d("jishnu","inside M");

                           statusbtn.setText("RUNNING");
                       }
                       speedtxt.setText(speed);
                       Log.d("jishnu","under speedtxt");
                       timetxt.setText(ctime);
                       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                       mapFragment.getMapAsync(Vehicle_Details.this);

                   } catch (JSONException e) {

                       e.printStackTrace();
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
                   MyData.put("vehicle_no", autoCompleteTextView.getText().toString());
                   return MyData;
               }
           };
           MyRequestQueue.add(MyStringRequest);

       }
   }







