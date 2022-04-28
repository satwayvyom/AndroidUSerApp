package com.vyom.gpstrackersatway;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class reset_pass extends AppCompatActivity {
    private EditText password;
    private EditText confirmPass;
    private Button reset;
    SharedPreferences sharedPreferences;
    String username;
    private static final String URL_DATA = "http://68.183.92.233:8000/api/changepassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_pass);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        confirmPass = (EditText) findViewById(R.id.editTextTextPassword2);
        reset = (Button) findViewById(R.id.button3);
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        System.out.println(username);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().length()>=4 && confirmPass.getText().length() >=4) {
                    System.out.println(password.getText()+"inside check");
                    System.out.println(confirmPass.getText());

                    if (password.getText().toString().equals(confirmPass.getText().toString())) {

                        System.out.println(password.getText()+"inside compare");
                        System.out.println(confirmPass.getText());

//           sennd requst

                        RequestQueue MyRequestQueue = Volley.newRequestQueue(reset_pass.this);

                        final StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_DATA, new com.android.volley.Response.Listener<String>() {
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
                                MyData.put("username",username);
                                System.out.println(username);
                                MyData.put("password",confirmPass.getText().toString() );
                                 System.out.println(password);


                                return MyData;
                            }
                        };


                        MyRequestQueue.add(MyStringRequest);


                    } else {
                        Toast.makeText(reset_pass.this, "Both passwords are not same ,Please re-enter", Toast.LENGTH_SHORT).show();
                        password.setText("");
                        confirmPass.setText("");
                    }
                }
                else
                {
                    Toast.makeText(reset_pass.this, "Password should be min 8 charaters", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}