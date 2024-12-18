package com.example.parkxpert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkxpert.COMMON.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {


    TextView loginPage;
    TextInputLayout name,age,address,mobile,email,password;
    String  NAME,AGE,ADDRESS,MOBILE,EMAIL,PASSWORD;
    Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        loginPage=findViewById(R.id.loginPage);
        name=findViewById(R.id.UserName);
        age=findViewById(R.id.UserAge);
        address=findViewById(R.id.userAddress);
        mobile=findViewById(R.id.UserPhoneNumber);
        email=findViewById(R.id.UserEmail);
        password=findViewById(R.id.Userpassrord);
        signUp=findViewById(R.id.UserRegisterButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                validate();
            }
        });







        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void validate()
    {
        NAME=name.getEditText().getText().toString();
        ADDRESS=address.getEditText().getText().toString();
        EMAIL=email.getEditText().getText().toString();
        MOBILE=mobile.getEditText().getText().toString();
        PASSWORD=password.getEditText().getText().toString();
        AGE=age.getEditText().getText().toString();

        if (NAME.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if (AGE.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Enter AGE", Toast.LENGTH_SHORT).show();
        }

        else if(ADDRESS.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Enter ADDRESS", Toast.LENGTH_SHORT).show();
        }
        else if (MOBILE.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
        }
        else if(!EMAIL.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            System.out.println("hello email error");
            Snackbar.make(email, "Fill Correct Format Mail Id", Snackbar.LENGTH_LONG)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();        }
        else if (PASSWORD.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
        }


        else
        {
            VolyRegisterUser();
        }

    }

    private void VolyRegisterUser()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Already Exist")) {

                    Toast.makeText(getApplicationContext(), " Already Exists", Toast.LENGTH_SHORT).show();

                } else if (!response.trim().equals("failed")) {

                    Toast.makeText(getApplicationContext(), "Success ..!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), " Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "my error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "userRegister");
                map.put("name", NAME);
                map.put("address", ADDRESS);
                map.put("phone",MOBILE);
                map.put("email", EMAIL);
                map.put("pass", PASSWORD);
                map.put("age", AGE);
                return map;
            }
        };
        queue.add(request);
    }
}