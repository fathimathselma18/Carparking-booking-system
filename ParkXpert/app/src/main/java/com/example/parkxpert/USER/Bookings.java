package com.example.parkxpert.USER;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkxpert.ADAPTER.RequestAdapter;
import com.example.parkxpert.COMMON.RequestPojo;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.R;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bookings extends AppCompatActivity {


    ListView listView;

    List<RequestPojo> list;

    ImageView nodata;

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        listView=findViewById(R.id.viewUserRequests);
        nodata=findViewById(R.id.no_data);
        builder=new AlertDialog.Builder(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                builder.setMessage("Heloo " + list.get(position).getUserName())
                        .setCancelable(false)
                        .setPositiveButton("Get Qrcode", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String req_id=list.get(position).getBooking_id();
                                getQrcode(req_id);

                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();

                alert.setTitle("Get Qrcode");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1d1d1d"));
                        alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#94c3ab"));
                        alert.getButton(android.app.AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#ff6600"));

                    }
                });
                alert.show();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

            }
        });

        getUserRequests();
    }

    private void getQrcode(String req_id)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));

                    RequestPojo requestPOJO = list.get(0);

                    String Qr=requestPOJO.getQrcode();
                    String QrId=requestPOJO.getBooking_id();

                    Intent in=new Intent(getApplicationContext(), ViewQr.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("qrcode", Qr);
                    bundle.putString("qrId", QrId);

                    in.putExtras(bundle);
                    startActivity(in);


                } else {
                    Toast.makeText(getApplicationContext(), "Request Pending", Toast.LENGTH_SHORT).show();

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
                map.put("key", "getQr");
                System.out.println(req_id);
                map.put("id", req_id);


                return map;
            }

        };
        queue.add(request);
    }

    private void deletePass(String req_id) {
    }

    private void getUserRequests()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    RequestAdapter adapter = new RequestAdapter(Bookings.this, list);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);

                } else {
                    Toast.makeText(getApplicationContext(), "NO_DATA", Toast.LENGTH_SHORT).show();
                    nodata.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
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
                map.put("key", "viewUserRequests");
                map.put("id", reg_id);


                return map;
            }

        };
        queue.add(request);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),UserHome.class));
    }
}