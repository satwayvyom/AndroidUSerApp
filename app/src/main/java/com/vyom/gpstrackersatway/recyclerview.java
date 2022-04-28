package com.vyom.gpstrackersatway;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class recyclerview<geocoder, addresses> extends AppCompatActivity {
    private static final String URL_DATA = "http://68.183.92.233:8000/api/android_vehicle_details";



    private RecyclerView recyclerView;
    private Adapter adapter;
    ArrayList<String> items;
    ImageView imageView;
    SharedPreferences sharedPreferences;
    String username;
    Button runbtn;
    Button stopbtn;
    int r = 0;
    int x = 0;
    int x1,y1,x2=0,y2=5;
    String Distance = "0";
    Double TotalDistance = 0.0;
    Button idlebtn;
    ImageButton locate,sharebtn;
    Button allbtn, editbtn;
    String fl = "vh";
    int z = 0;
    int r1=0,r2=0;
    String STOPPED = "STOPPED";
    String IDLE = "IDLE";
    String RUNNING = "RUNNING";
    JSONArray vehicleData;
    JSONObject VehicleData,VehicleImei,ImeiDetails;
    VehicleData[] vehicleMode;
    Handler handler = new Handler();
    VehicleDistance VD = new VehicleDistance();
    AutoCompleteTextView autoCompleteTextView;
    Runnable runnable;
Geocoder geocoder;
    public List<VehicleData> vhlist = new ArrayList<>();
    public List<VehicleData> runninglist = new ArrayList<>();
    public List<VehicleData> stoppedlist = new ArrayList<>();
    public List<VehicleData> idlelist = new ArrayList<>();
    List<String> imeiarray = new ArrayList<String>() ;
    List<String> imeiarrayToDisplay = new ArrayList<String>() ;
    List<String> testarray = new ArrayList<String>() ;
    List<String> vehiclearray=new ArrayList<>();
    List<String> datelist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader_class loader_class = new loader_class(recyclerview.this);
        setContentView(R.layout.activity_recyclerview);
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
//        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        username=sharedPreferences.getString("username", "");
        geocoder = new Geocoder(this, Locale.getDefault());





        adapter = new Adapter(vhlist);
        try {
            ImeiDetails = new JSONObject(sharedPreferences.getString("ImeiDetails", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView = findViewById(R.id.recyclerview);
        allbtn = findViewById(R.id.button11);
        locate = findViewById(R.id.imageButton2);
        sharebtn = findViewById(R.id.sharebtn);
        runbtn = findViewById(R.id.button13);
        stopbtn = findViewById(R.id.button14);
        idlebtn = findViewById(R.id.button15);
        loader_class.startdialogue();
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerview.this));
        try {
            VehicleData = new JSONObject(sharedPreferences.getString("VehicleDetails", ""));
            ImeiDetails = new JSONObject(sharedPreferences.getString("ImeiDetails", ""));
            for(int i=1;i<=ImeiDetails.length();i++)
            {
                imeiarray.add(ImeiDetails.getString("imei"+i));
                vehiclearray.add(VehicleData.getString("vehicle"+i));
            }
            String jsonstring = "{";
            for(int i=0;i<imeiarray.size();i++)
            {
                if(i==(imeiarray.size()-1))
                {
                    jsonstring+="\""+imeiarray.get(i)+"\":\""+vehiclearray.get(i)+"\"}";
                }
                else
                {
                    jsonstring +="\""+imeiarray.get(i)+"\":\""+vehiclearray.get(i)+"\",";
                }
            }
            VehicleImei = new JSONObject(jsonstring);

        } catch (JSONException e) {
            e.printStackTrace();
        }


       loadRecyclerViewData();
    }



    private void loadRecyclerViewData() {
        try
        {
            int h=0;
            if(h==0) {
                for (int i = 1; i <= ImeiDetails.length(); i++) {
                    imeiarrayToDisplay.add(ImeiDetails.getString("imei" + i));
                }
                h++;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }




        Button ViewMore = findViewById(R.id.button2);
        if(imeiarrayToDisplay.size() <= 5)
        {
            x1=0;
            y1=imeiarrayToDisplay.size();
            ReqVehicleData(imeiarrayToDisplay.subList(x1,y1));
            System.out.println("1"+imeiarray.subList(x1,y1));
        }
        else
        {
            x1=0;
            y1=5;
            ReqVehicleData(imeiarrayToDisplay.subList(x1,y1));
            System.out.println("2"+imeiarray.subList(x1,y1));
        }
        ViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(y2<imeiarrayToDisplay.size()) {
                    if (!(y2 + 5 > imeiarrayToDisplay.size())) {
                        x2 += 5;
                        y2 += 5;
                        ReqVehicleData(imeiarrayToDisplay.subList(x2, y2));
                        System.out.println("3" + imeiarray.subList(x2, y2));
                    } else if (y2 != imeiarrayToDisplay.size()) {
                        ReqVehicleData(imeiarrayToDisplay.subList(y2, imeiarrayToDisplay.size()));
                        System.out.println("4" + imeiarray.subList(y2, imeiarray.size()));
                        y2 = imeiarrayToDisplay.size();
                    }
                    else
                    {
                        Toast.makeText(recyclerview.this, "No More Vehicle Details", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
  public void ReqVehicleData(final List<String> imeiarray1) {
        if (imeiarray1 != null) {
            networkcall(imeiarray1);
        }

    }

    public void networkcall(final List<String> imeiarray1)
    {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(recyclerview.this);
        final StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_DATA, new com.android.volley.Response.Listener<String>() {
            ArrayList<String> VehicleArr = new ArrayList<>();
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object1 = new JSONObject(response);
                    JSONObject AllVehicleObj = object1.getJSONObject("android_vehicle_details");
                    loader_class.dismissdig();
                    Iterator<String> AllVehicleIterator = AllVehicleObj.keys();
                    while(AllVehicleIterator.hasNext())
                    {
                        String vehicleMode = "";
                        String key = AllVehicleIterator.next();
                        String modes = AllVehicleObj.getJSONArray(key).getString(2);
                        if (modes.equals("S")) {
                            vehicleMode = "Stopped";
                        }
                        else if (modes.equals("M")) {
                            vehicleMode = "Running";
                        }
                        else if (modes.equals("H")) {
                            vehicleMode = "Idle";
                        }
                        else
                        {
                            vehicleMode = "No Data";
                        }
                        String longitude = AllVehicleObj.getJSONArray(key).getString(1);
                        String latitude = AllVehicleObj.getJSONArray(key).getString(0);
                        String speed = AllVehicleObj.getJSONArray(key).getString(3);
                        String distance = AllVehicleObj.getJSONArray(key).getString(5);
                        String imei = key;
                        List<Address> addresses;

                        if(latitude.equals("null") || longitude.equals("null") || latitude.equals("0") || longitude.equals("0"))
                        {
                            addresses=null;
                        }
                        else {
                            addresses = geocoder.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1);
                        }

                        String address;
                        if(addresses == null || addresses.size()==0) {
                            address = "No Proper Address";
                        }
                        else
                        {
                            address = addresses.get(0).getAddressLine(0);
                        }
                        String vehicleNo = VehicleImei.getString(key);
                        //VehicleArr.add(vehicleNo);

                        VehicleData vhd = new VehicleData(vehicleNo,imei,vehicleMode,longitude,latitude,distance,speed,address);

                        vhlist.add(vhd);

                        if (vehicleMode.equals("Stopped"))
                        {
                            stoppedlist.add(vhd);
                        }
                        else if (vehicleMode.equals("Running"))
                        {
                            runninglist.add(vhd);
                        }
                        else if (vehicleMode.equals("Idle"))
                        {
                            idlelist.add(vhd);
                        }
                    }
                    runbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fl = "r";
                            adapter = new Adapter(runninglist);
                            recyclerView.setAdapter(adapter);
                            findViewById(R.id.button2).setVisibility(View.INVISIBLE);
                            runbtn.setBackgroundColor(getResources().getColor(R.color.yellow));
                            stopbtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                            allbtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                            idlebtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                        }
                    });
                    idlebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fl = "i";
                            adapter = new Adapter(idlelist);
                            recyclerView.setAdapter(adapter);
                            findViewById(R.id.button2).setVisibility(View.INVISIBLE);
                            idlebtn.setBackgroundColor(getResources().getColor(R.color.yellow));
                            runbtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                            stopbtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                            allbtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                        }
                    });
                    stopbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fl = "s";
                            adapter = new Adapter(stoppedlist);
                            recyclerView.setAdapter(adapter);
                            findViewById(R.id.button2).setVisibility(View.INVISIBLE);
                            stopbtn.setBackgroundColor(getResources().getColor(R.color.yellow));
                            allbtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                            idlebtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                            runbtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                        }
                    });
                    allbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fl = "vh";
                            adapter = new Adapter(vhlist);
                            recyclerView.setAdapter(adapter);
                            findViewById(R.id.button2).setVisibility(View.VISIBLE);
                            allbtn.setBackgroundColor(getResources().getColor(R.color.yellow));
                            runbtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                            stopbtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                            idlebtn.setBackgroundColor(getResources().getColor(R.color.Gray));
                        }
                    });
                    adapter = new Adapter(vhlist);
                    recyclerView.setAdapter(adapter);


                } catch (JSONException | IOException  e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                networkcall(imeiarray1);
            }
        }) {
            String imei_collection = "";
            //865006040738339,"861551045317864","861551045339348","861551047434543","861551047434543","861551047434543

            public Map<String, String> getParams() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("key1", username);

                    for (int i = 0; i < imeiarray1.size(); i++) {
                        if (i == (imeiarray1.size() - 1)) {
                            imei_collection = imei_collection  + imeiarray1.get(i) ;
                        } else {
                            imei_collection = imei_collection  + imeiarray1.get(i) + ",";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("imei_list", imei_collection);
                //
                String s = jsonObject.toString();
                return MyData;
            }
        };

        int socketTimeout = 90000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        MyStringRequest.setRetryPolicy(policy);
        MyRequestQueue.add(MyStringRequest);



    }






}






