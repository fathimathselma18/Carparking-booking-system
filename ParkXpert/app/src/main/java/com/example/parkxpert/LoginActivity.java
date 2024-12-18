package com.example.parkxpert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.parkxpert.ADMIN.Admin;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.USER.UserHome;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView signUpPage;
    TextInputLayout LoginUserName,LoginPassWord;
    Button login;

    AlertDialog.Builder builder;
    String EMAIL,PASSWORD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_avtivity);

        signUpPage=findViewById(R.id.signUpPage);
 
        login=findViewById(R.id.LoginButton);
        LoginUserName=findViewById(R.id.LoginUserName);
        LoginPassWord=findViewById(R.id.LoginPassWord);

        int PERMISSION_ALL = 1;


        signUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserRegister.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EMAIL = LoginUserName.getEditText().getText().toString();
                PASSWORD = LoginPassWord.getEditText().getText().toString();
                if (EMAIL.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Employee_id", Toast.LENGTH_SHORT).show();
                } else if (PASSWORD.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    login_volly();
                }

            }
        });
    }

    private void login_volly()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {

                    String DATA = response;
                    String resArr[] = DATA.trim().split("#");

                    SharedPreferences.Editor editor = getSharedPreferences("sharedData", MODE_PRIVATE).edit();
                    editor.putString("login_id", "" + resArr[0]);
                    editor.putString("type", "" + resArr[1]);
                    System.out.println(resArr[0]);
                    editor.commit();

                    if (resArr[1].equals("ADMIN")) {
                        startActivity(new Intent(LoginActivity.this, Admin.class));
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                    }

                    if (resArr[1].equals("USER")) {
                        startActivity(new Intent(LoginActivity.this, UserHome.class));
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed!!", Toast.LENGTH_SHORT).show();

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
                map.put("key", "login");
                map.put("email", EMAIL);
                map.put("pass", PASSWORD);
                return map;
            }
        };
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Exit!! Are You Sure?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finishAffinity();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.setTitle("Exitt");
        alert.show();
    }

}