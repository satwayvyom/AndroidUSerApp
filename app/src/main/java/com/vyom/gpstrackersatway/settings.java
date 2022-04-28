package com.vyom.gpstrackersatway;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class settings extends AppCompatActivity {

    SwitchCompat switchCompat1;
    SwitchCompat switchCompat2;
    SwitchCompat switchCompat3;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

 switchCompat1 = findViewById(R.id.switchcolor1);
 switchCompat2 = findViewById(R.id.switchcolor2);
 switchCompat3 = findViewById(R.id.switchcolor3);

        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);

switchCompat1.setChecked(sharedPreferences.getBoolean("value1",true));

        switchCompat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if(switchCompat1.isChecked()){
                    SharedPreferences.Editor editor1 = getSharedPreferences("userdata",MODE_PRIVATE).edit();
                    editor1.putBoolean("value1",true);
                    editor1.apply();
                    switchCompat1.setChecked(true);
                }else {
                    SharedPreferences.Editor editor1 = getSharedPreferences("userdata",MODE_PRIVATE).edit();
                    editor1.putBoolean("value1",false);
                    editor1.apply();
                    switchCompat1.setChecked(false);
                }
            }
        });


        switchCompat2.setChecked(sharedPreferences.getBoolean("value2",true));
        switchCompat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                if(switchCompat2.isChecked()){
                    SharedPreferences.Editor editor2 = getSharedPreferences("userdata",MODE_PRIVATE).edit();
                    editor2.putBoolean("value2",true);
                    editor2.apply();
                    switchCompat2.setChecked(true);
                }else {
                    SharedPreferences.Editor editor2 = getSharedPreferences("userdata",MODE_PRIVATE).edit();
                    editor2.putBoolean("value2",false);
                    editor2.apply();
                    switchCompat2.setChecked(false);
                }
            }
        });
        switchCompat3.setChecked(sharedPreferences.getBoolean("value3",true));

        switchCompat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                if(switchCompat3.isChecked()){
                    SharedPreferences.Editor editor3 = getSharedPreferences("userdata",MODE_PRIVATE).edit();
                    editor3.putBoolean("value3",true);
                    editor3.apply();
                    switchCompat3.setChecked(true);
                }else {
                    SharedPreferences.Editor editor3 = getSharedPreferences("userdata",MODE_PRIVATE).edit();
                    editor3.putBoolean("value3",false);
                    editor3.apply();
                    switchCompat3.setChecked(false);
                }
            }
        });


    }
}