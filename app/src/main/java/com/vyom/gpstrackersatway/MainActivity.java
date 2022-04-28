package com.vyom.gpstrackersatway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    private EditText uname;
    private EditText pass;
    // private TextView info;
    private Button Login, Reset;
    private int counter = 5;
    final String url = "http://68.183.92.233:8000/api/client_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);

        uname = (EditText) findViewById(R.id.usertxt);
        pass = (EditText) findViewById(R.id.passtxt);
        Login = (Button) findViewById(R.id.btnlogin);
        // Reset =(Button)findViewById(R.id.button18);



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logincall();


            }


        });
    }

    public void hidekeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

    public void logincall()
    {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String value = null;
                try {
                    value = object.getString("login");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (value.equals("1")) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", uname.getText().toString());
                    editor.putString("password",pass.getText().toString());
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect Username and password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                logincall();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", uname.getText().toString());
                //  System.out.println(uname.getText().toString());
                MyData.put("password", pass.getText().toString());

                return MyData;



            }
        };
        MyRequestQueue.add(MyStringRequest);
    }


}
