package com.vyom.gpstrackersatway;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private ImageView imageView, imgl, imgr, splashtitle, imgfull;
    private TextView textView;
    private static int SPLASH_SCREEN_TIME_OUT = 4000;
ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_splash_screen);

        imgr = findViewById(R.id.imgr);
        imgl = findViewById(R.id.imgleft);
        imgfull = findViewById(R.id.imgfull);
        layout = findViewById(R.id.layout);

      // splashtitle.setVisibility(View.INVISIBLE);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

      //  imgfull.setVisibility(View.INVISIBLE);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        imgr.setAnimation(animation1);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade1);
        imgl.setAnimation(animation2);


        final Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);


        splashtitle = findViewById(R.id.imageView8);
        splashtitle.setVisibility(View.INVISIBLE);
        final Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.satway_animation);
//        //
//
//
//        splashtitle.setAnimation(animation3);
//
//
//        new Handler().postDelayed(new Runnable() {
//
//
//            @Override
//            public void run() {
//
//
//                String username = sharedPreferences.getString("username", "");
//                if (username.equals("")) {
//
//
//                    Intent i = new Intent(SplashScreen.this,
//                            MainActivity.class);
//                    startActivity(i);
//                    finish();
//                } else {
//
//                    Intent i = new Intent(SplashScreen.this,
//                            Home.class);
//                    startActivity(i);
//                    finish();
//                }
//
//
//            }
//
//
//        }, 3600);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        })
//    }
//
//
//
//
//
//}






        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);

                new Handler().postDelayed(new Runnable() {


                    @Override
                    public void run() {


                        String username = sharedPreferences.getString("username", "");
                        if (username.equals("")) {


                            Intent i = new Intent(SplashScreen.this,
                                    MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {

                            Intent i = new Intent(SplashScreen.this,
                                    Home.class);
                            startActivity(i);
                            finish();
                        }


                    }
                },3600);





        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                layout.setVisibility(View.INVISIBLE);
                imgfull.setAnimation(rotate);
                imgfull.setVisibility(View.VISIBLE);
                splashtitle.setAnimation(animation3);
                splashtitle.setVisibility(View.VISIBLE);

            }
        }, 1100);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                splashtitle.setVisibility(View.VISIBLE);
            }
        }, 2800);
    }






    }


