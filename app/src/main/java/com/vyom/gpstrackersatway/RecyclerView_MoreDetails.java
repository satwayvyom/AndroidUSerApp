package com.vyom.gpstrackersatway;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecyclerView_MoreDetails extends AppCompatActivity {
    private static final String URL_DATA = "http://satwayvyom.in:142/android_get_vehicle_details";
//    ArrayList<String> vhno = new ArrayList<String>();
//    ArrayList<Float> lon = new ArrayList<>();
//    ArrayList<Float> lat = new ArrayList<>();

    SharedPreferences sharedPreferences;
    String username;
    TextView textView;
    TextView speed;
    TextView timetxt;
    TextView mode;
    TextView powerstatusbtn;
    TextView vehiclenumber;
    TextView IMEInumber;
    TextView longitude;
    TextView lat;
    TextView ignition;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view__more_details);
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
//        TextView speed = findViewById(R.id.textView14);
       //  vehiclenumber = findViewById(R.id.textView15);

        vehiclenumber = (TextView)findViewById(R.id.textView14);
        IMEInumber = (TextView)findViewById(R.id.textView15);
        mode =(TextView)findViewById(R.id.textView16);
        speed = findViewById(R.id.textView15);
        longitude =(TextView)findViewById(R.id.textView18);
        timetxt =(TextView)findViewById(R.id.textView19);
        lat =(TextView)findViewById(R.id.textView20);
        ignition =(TextView)findViewById(R.id.textView21);


        vehiclenumber.setText(getIntent().getStringExtra("title"));
        IMEInumber .setText(getIntent().getStringExtra("imei"));
        RequestQueue MyRequestQueue = Volley.newRequestQueue(RecyclerView_MoreDetails.this);
        final StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_DATA, new com.android.volley.Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                try {
                    JSONObject obj= new JSONObject(response);
                    JSONObject obj1 = obj.getJSONObject("vehno");
                    

                }

                catch (Exception e){
                }
            }


        }, new com.android.volley.Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {


            }

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username",IMEInumber.getText().toString());
                return MyData;
            }
        };


        MyRequestQueue.add(MyStringRequest);





    }
}