package com.vyom.gpstrackersatway;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class alerts extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    String username;
    SharedPreferences sharedPreferences;
    AutoCompleteTextView autoCompleteTextView;
    JSONObject VehicleData,ImeiDetails,VehicleImei;
    TextView textView1, textView2;
    Button button, button2, submit;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    ArrayList<String> mylist = new ArrayList<String>();
    Spinner dropdown;
   
    int x=0;
    String mn,hn,dn;


    ArrayList<String> arr = new ArrayList<>();
    ArrayList<String> vehiclearray = new ArrayList<>();

    List<String> imeiarray = new ArrayList<>();
    ArrayList <String>array2 = new ArrayList<>();
    //  final String url = "http://144.126.252.202:80/vehicle_history";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        textView1 = findViewById(R.id.textView);
        button = findViewById(R.id.btnPick);
        textView2 = findViewById(R.id.textView2);
        button2 = findViewById(R.id.btnPick2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(alerts.this, alerts.this, year, month, day);
            
                datePickerDialog.show();


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(alerts.this, alerts.this, year, month, day);

                datePickerDialog.show();
            }
        });



        dropdown = findViewById(R.id.spinner);


        String[] items = new String[]{"SELECT","STOPPED", "RUNNING"};


//        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//
//        dropdown.setAdapter(adapter1);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item);
        dropdown.setAdapter(adapter1);
        String text = dropdown.getSelectedItem().toString();
        System.out.println(text);


        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.vehicleno);


        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        textView1 = findViewById(R.id.textView);
        button = findViewById(R.id.btnPick);
        textView2 = findViewById(R.id.textView2);
        submit = findViewById(R.id.button12);



        try {
            VehicleData = new JSONObject(sharedPreferences.getString("VehicleDetails", ""));
            System.out.println(VehicleData);
//
            for(int i =0;i<VehicleData.length();i++) {


                array2.add((String)VehicleData.get("vehicle"+Integer.parseInt(String.valueOf(i+1))));
            }

            System.out.println(array2);



            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.select_dialog_item, array2);
            //Getting the instance of AutoCompleteTextView
            autoCompleteTextView.setThreshold(1);//will start working from first character
            autoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

            System.out.println(autoCompleteTextView);

            ImeiDetails = new JSONObject(sharedPreferences.getString("ImeiDetails", ""));

            for (int i = 1; i <= ImeiDetails.length(); i++) {
                imeiarray.add(ImeiDetails.getString("imei" + i));
                vehiclearray.add(VehicleData.getString("vehicle" + i));

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

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month + 1;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(alerts.this, alerts.this, hour, minute, DateFormat.is24HourFormat(this));

        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        if(myMonth < 10){
            mn = "0"+Integer.toString(myMonth);
        }
        else{

            mn = Integer.toString(myMonth) ;
        }
        if(myHour < 10) {

            hn = "0" +Integer.toString(myHour);

        }else{

            hn = Integer.toString(myHour) ;
        }
        if(myday < 10) {

            dn = "0" +Integer.toString(myday);

        }else{

            dn = Integer.toString(myday) ;
        }
        mylist.add(+myYear +
                "-" + mn +
                "-" + dn + " "
                +hn +
                ":" + myMinute);

        System.out.println(hn);
        try {
            textView1.setText(mylist.get(0));
            if(x!=0)
            {
                textView2.setText(mylist.get(1));
            }
            x++;

        } catch (Exception e) {
            e.printStackTrace();
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                Date date = null,d=null;
                Date currentTime = Calendar.getInstance().getTime();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                if(dropdown.getSelectedItem().toString().equals("RUNNING")) {
                    i = new Intent(alerts.this, activity_main1.class);
                }

                else
                {
                    i = new Intent(alerts.this, stopped_history.class);
                }
                

                i.putExtra("StartDate", textView1.getText().toString());
                i.putExtra("EndDate", textView2.getText().toString());
                i.putExtra("mode",dropdown.getSelectedItem().toString());
                if(dropdown.getSelectedItem().toString().equals("SELECT")){
                    i = new Intent(alerts.this,alerts.class);
                }
                System.out.println(textView1.getText().toString()+"--"+textView1.getText().toString()+"--");
                try {
                    i.putExtra("imei", VehicleImei.getString(autoCompleteTextView.getText().toString()));
                    System.out.println(VehicleImei.getString(autoCompleteTextView.getText().toString())+"jhhhhh,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(textView1.equals("")){
                    Toast.makeText(alerts.this, "EnterDetails", Toast.LENGTH_SHORT).show();
                }
                startActivity(i);

            }
        });

    }


}














