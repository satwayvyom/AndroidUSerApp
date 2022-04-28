package com.vyom.gpstrackersatway;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Contact_us extends AppCompatActivity {
    private static final String URL_DATA = "http://68.183.92.233:8000/api/adder_details";
    EditText etNumber;
    ImageButton btnph;
    SharedPreferences sharedPreferences;
    String username;
    TextView name;
   TextView mobile_no;
    TextView address;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        name =(TextView)findViewById(R.id.name);
        mobile_no =(TextView)findViewById(R.id.phoneno);
        address =(TextView)findViewById(R.id.address);

//
//        GifImageView carimgerun = (GifImageView) findViewById(R.id.carimgerun);
//
//        TranslateAnimation animation = new TranslateAnimation(-30,  700, 0, 0);     //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
//        animation.setDuration(5000);  // animation duration
//        animation.setRepeatCount(50);  // animation repeat count
//        animation.setRepeatMode(100);   // repeat animation (left to right, right to left )
//        //animation.setFillAfter(true);
//
//        carimgerun.startAnimation(animation);  // start animatio


        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

                //Text(MainActivity.this, "Enter username and password", Toast.LENGTH_SHORT).show();
                RequestQueue MyRequestQueue = Volley.newRequestQueue(Contact_us.this);

                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);


                        JSONObject object1 = null;
                        try {
                            object1 = new JSONObject(response);
                            String first_name = object1.getString("first_name");
                            String phoneno = object1.getString("phone");
                            String company = object1.getString("company_details");


                            name.setText(first_name);

                            address.setText(phoneno);
                           mobile_no.setText(company);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        //Text(MainActivity.this, response, Toast.LENGTH_SHORT).show();



                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Text(MainActivity.this, error.toString(),Toast.LENGTH_SHORT).show();




                    }
                })

                {



                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("username",username);

                        return MyData;



                    }
                };



                MyRequestQueue.add(MyStringRequest);






}


}

