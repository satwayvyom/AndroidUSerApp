package com.vyom.gpstrackersatway;
//package com.android

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
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


public class Home extends FragmentActivity implements OnMapReadyCallback {



    private static final String URL_DATA = "http://68.183.92.233:8000/api/android_home_page";



    Button navbutton, navibutton, dashbutton, dashbutton2, dashbutton3, dashbutton4, dashbutton5, toogledash, nobuttn;
    DrawerLayout navigation;
    View b2;
    int i,x,y;
    JSONArray array3;
    GoogleMap map;
    View viewBlur;

    String VehicleNo;
    TextView Dashboard,allvehicles,livetracking,history,services,contact,logout;


    int tooglebtn = 0;
    ArrayList<String> vhno = new ArrayList<String>();
    ArrayList<String> imei_list = new ArrayList<String>();
    ArrayList<Float> lon = new ArrayList<>();
    ArrayList<Float> lat = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String username,password;
    List<String> testarray = new ArrayList<String>() ;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password","");
        PasswordChangeCheck();
        super.onCreate(savedInstanceState);
        Dashboard = findViewById(R.id.dashboardtext);
        allvehicles = findViewById(R.id.allvehicletext);
        livetracking = findViewById(R.id.livetrackingtext);
        history = findViewById(R.id.historytext);
        services = findViewById(R.id.servicestext);
        contact = findViewById(R.id.contactustext);
        logout = findViewById(R.id.logoutbtntext);
        setContentView(R.layout.activity_home);
        for(int i=0;i<9;i++)
        {
            testarray.add("a");
        }

        if(testarray.size() <= 5)
        {
            x=0;
            y=testarray.size();
        }
        else
        {
            x=0;
            y=5;

        }
        x=0;y=5;

        while(!(y+5>testarray.size()))
        {
            x+=5;
            y+=5;
            System.out.println(x+"-->"+y);

        }
        if(y!=testarray.size())
        System.out.println(y+"-->"+testarray.size());



        viewBlur = findViewById(R.id.blurview);
        appendLog(username,"home");
        loader_class loader_class = new loader_class(Home.this);
        loader_class.startdialogue();
        final FloatingActionButton logoutbtn = (FloatingActionButton) findViewById(R.id.logoutbtn);
        final FloatingActionButton menu = (FloatingActionButton) findViewById(R.id.menu);
        final FloatingActionButton dashboard = (FloatingActionButton) findViewById(R.id.dashboard);
        FloatingActionButton allvechicle = (FloatingActionButton) findViewById(R.id.allvehicle);
        final FloatingActionButton history = (FloatingActionButton) findViewById(R.id.history);
        FloatingActionButton services = (FloatingActionButton) findViewById(R.id.services);
        FloatingActionButton livetracking = (FloatingActionButton) findViewById(R.id.livetracking);
        FloatingActionButton contactus = (FloatingActionButton) findViewById(R.id.contactus);
        final LinearLayout logoutlayout = (LinearLayout) findViewById(R.id.logoutbtnlayout);
        final LinearLayout dashboardlayout = (LinearLayout) findViewById(R.id.dashboardlayout);
        final LinearLayout allvechiclelayout = (LinearLayout) findViewById(R.id.allvehiclelayout);
        final LinearLayout historylayout = (LinearLayout) findViewById(R.id.historylayout);
        final LinearLayout serviceslayout = (LinearLayout) findViewById(R.id.serviceslayout);
        final LinearLayout livetrackinglayout = (LinearLayout) findViewById(R.id.livetrackinglayout);
        final LinearLayout contactuslayout = (LinearLayout) findViewById(R.id.contactuslayout);
        final Animation showMenu = AnimationUtils.loadAnimation(Home.this, R.anim.show_menu_button);
        final Animation hideMenu = AnimationUtils.loadAnimation(Home.this, R.anim.hide_menu_button);
        final Animation showMenuLayout = AnimationUtils.loadAnimation(Home.this, R.anim.show_menu_layout);
        final Animation hideMenuLayout = AnimationUtils.loadAnimation(Home.this, R.anim.hide_menu_layout);

         menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dashboardlayout.getVisibility() == View.VISIBLE) {
                    viewBlur.setVisibility(View.GONE);
                    dashboardlayout.setVisibility(View.GONE);
                    allvechiclelayout.setVisibility(View.GONE);
                    historylayout.setVisibility(View.GONE);
                    serviceslayout.setVisibility(View.GONE);
                    livetrackinglayout.setVisibility(View.GONE);
                    contactuslayout.setVisibility(View.GONE);
                    logoutlayout.setVisibility(View.GONE);
                    dashboardlayout.startAnimation(hideMenuLayout);
                    allvechiclelayout.startAnimation(hideMenuLayout);
                    historylayout.startAnimation(hideMenuLayout);
                    serviceslayout.startAnimation(hideMenuLayout);
                    livetrackinglayout.startAnimation(hideMenuLayout);
                    contactuslayout.startAnimation(hideMenuLayout);
                    logoutlayout.startAnimation(hideMenuLayout);
                    menu.startAnimation(hideMenu);

                } else {
                   viewBlur.setVisibility(View.VISIBLE);
                    dashboardlayout.setVisibility(View.VISIBLE);
                    allvechiclelayout.setVisibility(View.VISIBLE);
                    historylayout.setVisibility(View.VISIBLE);
                    serviceslayout.setVisibility(View.VISIBLE);
                    livetrackinglayout.setVisibility(View.VISIBLE);
                    contactuslayout.setVisibility(View.VISIBLE);
                    logoutlayout.setVisibility(View.VISIBLE);
                    dashboardlayout.startAnimation(showMenuLayout);
                    allvechiclelayout.startAnimation(showMenuLayout);
                    historylayout.startAnimation(showMenuLayout);
                    serviceslayout.startAnimation(showMenuLayout);
                    livetrackinglayout.startAnimation(showMenuLayout);
                    contactuslayout.startAnimation(showMenuLayout);
                    logoutlayout.startAnimation(showMenuLayout);
                    menu.startAnimation(showMenu);
                }
            }
        });

        if (array3 == null) {


       loading_first_vehicle();


        }
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
    public void logout(View view) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);
        alertDialogBuilder.setTitle("Are you sure wants to logout?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                finishAndRemoveTask();
                                finish();
                                startActivity(new Intent(Home.this, MainActivity.class));
                            }
                        })
                 .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();

        alert.setTitle("Are you sure wants to logout?");
        alert.show();
    }
    public void switchactivity(View view) {
        PasswordChangeCheck();

        Intent intent = new Intent(this, recyclerview.class);
        intent.putExtra("imeiarray", imei_list);

        //System.out.println(imei_list);
        startActivity(intent);
    }

    public void vehiclechange(View view) {
        PasswordChangeCheck();
        Intent intent = new Intent(this, Vehicle_Details.class);
        startActivity(intent);
    }


    public void contactactivity(View view) {
        PasswordChangeCheck();
        Intent i = new Intent(this, Contact_us.class);
        startActivity(i);
    }

    public void livetracking(View view) {
        PasswordChangeCheck();
        Intent i = new Intent(this, LocationTracking.class);
        startActivity(i);
    }

    public void settingsactivity(View view) {
        PasswordChangeCheck();
        Intent i = new Intent(this, settings.class);
        startActivity(i);
    }

    public void alertactivity(View view) {
        PasswordChangeCheck();
        Intent i = new Intent(this, alerts.class);
        startActivity(i);
    }

    public void serviceactivity(View view) {
        PasswordChangeCheck();
        Intent i = new Intent(this, service.class);
        startActivity(i);
    }

    public void PasswordChangeCheck(){
        final String url = "http://68.183.92.233:8000/api/client_login";
        RequestQueue PassChangeReq = Volley.newRequestQueue(Home.this);
        StringRequest PassChangeReqString = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                JSONObject object = null;
                String value = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    value = object.getString("login");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (value.equals("1")) {
                    //Do nothing as the password is correct
                }
                else {
                    Toast.makeText(Home.this, "Your Password has been changed ,please login with new password", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    finishAndRemoveTask();
                    finish();
                    startActivity(new Intent(Home.this, MainActivity.class));
                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", username.toString());
                MyData.put("password", password.toString());
                return MyData;
            }
        };
        PassChangeReq.add(PassChangeReqString);
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
        if(lat.get(0)!=0 && lon.get(0)!=0)
        {
            LatLng place = new LatLng(lat.get(0), lon.get(0));
            map.addMarker(new MarkerOptions().position(place).title(VehicleNo));
            map.moveCamera(CameraUpdateFactory.newLatLng(place));
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            CameraUpdate center = CameraUpdateFactory.newLatLng(place);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
            map.moveCamera(center);
            map.animateCamera(zoom);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setTiltGesturesEnabled(false);
            loader_class.dismissdig();

        }
        else
        {
            Toast.makeText(this, "No Location Data", Toast.LENGTH_LONG).show();
        }

    }

    public void loading_first_vehicle()
    {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(Home.this);

        final StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                int count=0;
                try {
                    JSONObject object1 = new JSONObject(response);
                    System.out.println(response+".....");
                    JSONObject ImeiDetails = object1.getJSONObject("imei_dict");
                    System.out.println(ImeiDetails);
                    JSONObject Vehicledetails = object1.getJSONObject("vehicle_dict");
                    Iterator<String> ImeiIterator = ImeiDetails.keys();
                    JSONObject VehicleLocation = object1.getJSONObject("vehicle_location");
                    String imei = VehicleLocation.getString("imei");
                    while(ImeiIterator.hasNext()) {
                        count++;
                        if(ImeiDetails.getString(ImeiIterator.next()).equals(imei))
                        {
                            break;


                        }
                    }
                    VehicleNo=Vehicledetails.getString("vehicle"+count);


                    if(VehicleLocation.getString("latitude").equals("0") || VehicleLocation.getString("longitude").equals("0")
                            ||VehicleLocation.getString("latitude").equals("null") || VehicleLocation.getString("longitude").equals("null"))
                    {

                        lat.add((float) 0);
                        lon.add((float) 0);
                    }

                    else
                    {
                        lat.add(Float.valueOf(VehicleLocation.getString("latitude")));
                        lon.add(Float.valueOf(VehicleLocation.getString("longitude")));
                    }
                    //setting shared preference with all vehicle details
                    SharedPreferences .Editor editor = sharedPreferences.edit();
                    editor.putString("ImeiDetails",ImeiDetails.toString());


                    editor.putString("VehicleDetails",Vehicledetails.toString());
                    System.out.println(ImeiDetails.toString()+"''''''''''''''''''");
                    System.out.println(Vehicledetails.toString()+";;;;;;;;;;;;;;;;;;;;;;;;;");

                    editor.apply();


                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Home.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//            Text(LocationTracking.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
                        loading_first_vehicle();

                    }

                })
        {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", username);

                System.out.println(username + "hghgfh");
                return MyData;

            }
        };
        int socketTimeout = 70000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        MyStringRequest.setRetryPolicy(policy);
        MyRequestQueue.add(MyStringRequest);

    }

}