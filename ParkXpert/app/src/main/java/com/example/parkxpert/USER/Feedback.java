package com.example.parkxpert.USER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {


    String SUB,DES,RATING="0";

    TextInputLayout subject,description;

    Button send;

    RatingBar rt;

    String User_id,booking_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        subject=findViewById(R.id.userfeedback_subject);
        description=findViewById(R.id.userfeedback_details);
        send=findViewById(R.id.userfeedback_btnsend);
        rt=findViewById(R.id.userfeedback_rating);

        Intent intent = getIntent();
        User_id= intent.getExtras().getString("user_id");
        booking_id = intent.getExtras().getString("Booking_id");


        rt.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                RATING= String.valueOf(rt.getRating());

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SUB=subject.getEditText().getText().toString().trim();
                DES=description.getEditText().getText().toString().trim();


                if (SUB.isEmpty()) {
                    Toast.makeText(Feedback.this, "ENTER SUBJECT", Toast.LENGTH_SHORT).show();
                } else if (DES.isEmpty()) {
                    Toast.makeText(Feedback.this, "ENTER VALID DETAILS", Toast.LENGTH_SHORT).show();
                } else if(RATING.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Rate US", Toast.LENGTH_SHORT).show();
                }else{
                    addFeedBack(SUB,DES,RATING);
                }
            }
        });
    }

    private void addFeedBack(String sub, String des, String rating)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******",response);
                if(!response.trim().equals("failed"))
                {
                    Toast.makeText(getApplicationContext(), "Feedback Added ..!", Toast.LENGTH_LONG).show();
                    Intent in=new Intent(getApplicationContext(), UserHome.class );
                    startActivity(in);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "eroor in register", Toast.LENGTH_LONG).show();
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
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String uid = prefs.getString("login_id", "No logid");
                map.put("key","addFeedBack");
                map.put("uid",uid);
                map.put("booking_id",booking_id);
                map.put("subject",sub);
                map.put("description",des);
                map.put("rating",rating);
                return map;
            }
        };
        queue.add(request);
    }
}