package com.vyom.gpstrackersatway;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class service extends AppCompatActivity {
    private static final String TAG = "service";
    private Button savebtn;
private  Button btnnotification;
    private EditText editText1,editText3,editText2,editText4,editText5,editText6,editText7;
      ImageView img1,img2,img3,img4,img5;
    final String url2  ="http://68.183.92.233:8000/api/user_services";
     private int mDate,mMonth,mYear,mn;
    SharedPreferences sharedPreferences;
    String username;
    AutoCompleteTextView autoCompleteTextView;
    JSONObject VehicleData,ImeiDetails,VehicleImei;
    ArrayList<String> arr = new ArrayList<>();
    ArrayList <String>array2 = new ArrayList<>();
    ArrayList<String> imeiarray = new ArrayList<>();
    ArrayList<String> vehiclearray = new ArrayList<>();
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.vehicleno);

        editText2 =(EditText)findViewById(R.id.txt2);//driverno
        editText3 =(EditText)findViewById(R.id.txt3);//data re
        editText4 =(EditText)findViewById(R.id.txt4);//tax
        editText5 =(EditText)findViewById(R.id.txt5);//pollu
        editText6 =(EditText)findViewById(R.id.txt6);//fitness
        editText7 =(EditText)findViewById(R.id.txt7);//insu

        img1 =findViewById(R.id.dataimg);
        img2 = findViewById(R.id.insuranceimg);
        img3 = findViewById(R.id.fitnesimg);
        img4=findViewById(R.id.pollutionimg);
        img5=findViewById(R.id.taximg);

          savebtn =findViewById(R.id.save);

        try {
            VehicleData = new JSONObject(sharedPreferences.getString("VehicleDetails", ""));
            System.out.println(VehicleData);
//

            for(int i =1;i<VehicleData.length();i++) {

                arr.add((String)VehicleData.get("vehicle"+i));


            }
            System.out.println(arr);


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

            System.out.println(VehicleImei+"jhjhjhjh");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, arr);
//           Getting the instance of AutoCompleteTextView
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);


         img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);
                mYear = cal.get(Calendar.YEAR);




                DatePickerDialog datePickerDialog = new DatePickerDialog(service.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;


                         if (month<10)
                        {
                           editText3.setText(year + "-" +"0"+ month + "-" + dayOfMonth);

                        }



                         else if(dayOfMonth<10){

                             editText3.setText(year + "-" + month + "-" +"0"+ dayOfMonth);

                         }
                        else


                        editText3.setText(year + "-" + month + "-" + dayOfMonth);

                    }
                }, mYear, mn, mDate);




                datePickerDialog.show();


            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);

                mYear = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(service.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        if (month<10)
                        {
                            editText7.setText(year + "-"+"0" +month + "-" + dayOfMonth);

                        }

                        else if(dayOfMonth<10){

                            editText7.setText(year + "-" + month + "-" +"0"+ dayOfMonth);

                        }
                        else


                        editText7.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                }, mYear, mn, mDate);
                datePickerDialog.show();


            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);

                mYear = cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(service.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        if (month<10)
                        {
                            editText6.setText(year + "-"+"0" + month + "-" + dayOfMonth);

                        }
                        else if(dayOfMonth<10){

                            editText6.setText(year + "-" + month + "-" +"0"+ dayOfMonth);

                        }
                        else


                        editText6.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                }, mYear, mn, mDate);
                datePickerDialog.show();


            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);

                mYear = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(service.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;


                        if (month<10)
                        {
                            editText5.setText(year + "-" +"0"+ month + "-" + dayOfMonth);

                        }

                        else if(dayOfMonth<10){

                            editText5.setText(year + "-" + month + "-" +"0"+ dayOfMonth);

                        }
                        else

                        editText5.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                }, mYear, mn, mDate);
                datePickerDialog.show();


            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);

                mYear = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(service.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        if (month<10)
                        {
                            editText4.setText(year + "-"+"0" + month + "-" + dayOfMonth);

                        }

                        else if(dayOfMonth<10){

                            editText4.setText(year + "-" + month + "-" +"0"+ dayOfMonth);

                        }
                        else


                        editText4.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();


            }
        });





        savebtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          RequestQueue MyRequestQueue = Volley.newRequestQueue(service.this);

          final StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url2, new com.android.volley.Response.Listener<String>() {
              @Override

              public void onResponse(String response) {
                System.out.println(response);


              }


          }, new com.android.volley.Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {

              } //Create an error listener to handle errors appropriately.

          }) {

              protected Map<String, String> getParams() {
                  Map<String, String> MyData = new HashMap<String, String>();
                  if(editText3.getText().toString().equals(""))
                  {
                      MyData.put("data","0");
                  }
                  else
                  {
                  MyData.put("data",editText3.getText().toString());
//                  System.out.println(editText3.getText().toString());
                  }
                  System.out.println(MyData);
                  if(editText6.getText().toString().equals("")){
                      MyData.put("fitness","0");
                  }
                  else {
                      MyData.put("fitness",editText6.getText().toString() );
//                     System.out.println(editText6.getText().toString());
                  }
                  System.out.println(MyData);
                  if(editText2.getText().toString().equals("")){
                        MyData.put("driver_mobile","0");
                  }
                  else{
                        MyData.put("driver_mobile",editText2.getText().toString() );
//                        System.out.println(editText2.getText().toString());
                  }
if(editText5.getText().toString().equals("")){
    MyData.put("pollution","0");
}
else//editText2 =(EditText)findViewById(R.id.txt2);//driverno
//    editText3 =(EditText)findViewById(R.id.txt3);//data re
//                  editText4 =(EditText)findViewById(R.id.txt4);//tax
//                  editText5 =(EditText)findViewById(R.id.txt5);//pollu
//                  editText6 =(EditText)findViewById(R.id.txt6);//fitness
//                  editText7 =(EditText)findViewById(R.id.txt7);//insu
{
    MyData.put("pollution",editText5.getText().toString() );
//    System.out.println(editText5.getText().toString() );
}
if(editText4.getText().toString().equals(""))
{

    MyData.put("tax","0");
}
else {
    MyData.put("tax",editText4.getText().toString() );
//    System.out.println(editText4.getText().toString());
}
if(editText7.getText().toString().equals(""))
{
//    System.out.println("phone");
    MyData.put("insurance","0");


}
else
{

    MyData.put("insurance", editText7.getText().toString());
//    System.out.println(editText7.getText().toString());
}

                  MyData.put("vehicle_no", autoCompleteTextView.getText().toString());
//                  System.out.println(autoCompleteTextView.getText().toString()+"=================");

                  MyData.put("username",username);
                  System.out.println(MyData);
//                  System.out.println(username);
                  return MyData;
              }
          };


          MyRequestQueue.add(MyStringRequest);



      }

  });




    }



    }





