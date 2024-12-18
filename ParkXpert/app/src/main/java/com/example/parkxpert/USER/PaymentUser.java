package com.example.parkxpert.USER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.R;

import java.util.HashMap;
import java.util.Map;

public class PaymentUser extends AppCompatActivity {


    Button payButton;
    String totalPrice;


    String price;


    EditText card_number, expiry_date, cvv, ac_number;
    TextView totalPriceTextView;


    String CARD_NUMBER, EXPIRYDATE, CVV, AC_NUMBER;

    String UserId,BookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_user);


        Intent in=getIntent();
        UserId=in.getExtras().getString("user_id");
        BookingId=in.getExtras().getString("Booking_id");

        getPrice();


        payButton = findViewById(R.id.orderButton);
        card_number = findViewById(R.id.cardNumberEditText);
        expiry_date = findViewById(R.id.expiryDateEditText);
        cvv = findViewById(R.id.cvvEditText);
        ac_number = findViewById(R.id.accountNumberEditText);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CARD_NUMBER = card_number.getText().toString();
                EXPIRYDATE = expiry_date.getText().toString();
                CVV = cvv.getText().toString();
                AC_NUMBER = ac_number.getText().toString();

                if (CARD_NUMBER.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "fill the field", Toast.LENGTH_SHORT).show();
                } else if (EXPIRYDATE.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "fill the fiele", Toast.LENGTH_SHORT).show();
                } else if (CVV.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "fill the field", Toast.LENGTH_SHORT).show();
                } else if (AC_NUMBER.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "fill the field", Toast.LENGTH_SHORT).show();
                } else {
                    doPayment();
                }
            }
        });
    }

    private void getPrice()
    {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {

                    String DATA = response;
                    String resArr[] = DATA.trim().split("#");
                    totalPrice=resArr[0];

                    System.out.println(resArr);
                    totalPriceTextView.setText("Price :"+totalPrice +" /-");

                } else {
                    Toast.makeText(getApplicationContext(), "NO_DATA", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String login_id = prefs.getString("login_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "getPrice");


                return map;
            }

        };
        queue.add(request);
    }

    private void doPayment() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Toast.makeText(getApplicationContext(), " Payment Done! Thanks", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), Bookings.class);
                    startActivity(in);

                } else {
                    Toast.makeText(getApplicationContext(), "Failed Payment", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String reg_id = prefs.getString("login_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "addbankpayment");
                map.put("user_id", reg_id);
                map.put("BookingId", BookingId);
                map.put("card_num", CARD_NUMBER);
                map.put("cvv", CVV);
                map.put("ac_num", AC_NUMBER);
                map.put("ex_date", EXPIRYDATE);
                map.put("price", totalPrice);


                return map;
            }

        };
        queue.add(request);
    }
}