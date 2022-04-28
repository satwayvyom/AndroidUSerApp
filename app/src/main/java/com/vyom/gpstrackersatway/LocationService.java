package com.vyom.gpstrackersatway;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
public class LocationService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        FusedLocationProviderClient   fusedLocationProviderClient
        return null;


    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
