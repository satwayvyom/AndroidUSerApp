package com.vyom.gpstrackersatway;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class HistoryStop_map extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    Double longitude,latitude;
    Marker m;
    SharedPreferences sharedPreferences;
    String Vehiclenumber,username;
    Button normalbtn, satellitebtn, terrbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_stop_map);
        Vehiclenumber = getIntent().getStringExtra("VehicleNum");
        longitude = getIntent().getDoubleExtra("longitude",0);
        latitude = getIntent().getDoubleExtra("latitude",0);
        terrbtn = (Button) findViewById(R.id.terrainbtn);
        normalbtn = (Button) findViewById(R.id.normalbtn);
        satellitebtn= findViewById(R.id.abc);
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        satellitebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                satellitebtn.setBackgroundColor(getResources().getColor(R.color.Green));
                terrbtn.setBackgroundColor(Color.WHITE);
                normalbtn.setBackgroundColor(Color.WHITE);
                // Do something in response to button click
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        terrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                terrbtn.setBackgroundColor(getResources().getColor(R.color.Green));
                satellitebtn.setBackgroundColor(Color.WHITE);
                normalbtn.setBackgroundColor(Color.WHITE);
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


            }
        });

        normalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalbtn.setBackgroundColor(getResources().getColor(R.color.Green));
                satellitebtn.setBackgroundColor(Color.WHITE);
                terrbtn.setBackgroundColor(Color.WHITE);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(HistoryStop_map.this);
    }


    public void onMapReady(GoogleMap googleMap) {
        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        map = googleMap;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            LatLng place = new LatLng(latitude,longitude);
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String Known = addresses.get(0).getFeatureName();
            String city = addresses.get(0).getLocality();
            m = map.addMarker(new MarkerOptions().position(place).title(city));
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(50);
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
}