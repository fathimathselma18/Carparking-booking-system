package com.example.parkxpert.ADMIN.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkxpert.ADAPTER.RequestAdapter;
import com.example.parkxpert.ADMIN.GenerateQRcode;
import com.example.parkxpert.COMMON.RequestPojo;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.R;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Requets extends Fragment {

    ListView listView;
    List<RequestPojo> list;
    ImageView nodata;
    AlertDialog.Builder builder;
    String status,paymentStatus,qrStatus;
    String requestid, userName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests, container, false);

        listView = root.findViewById(R.id.viewUserRequests);
        nodata = root.findViewById(R.id.no_data);
        builder = new AlertDialog.Builder(getActivity());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                status = list.get(position).getBookingStatus();
                paymentStatus=list.get(position).getPaymentStatus();
                qrStatus=list.get(position).getQrStatus();



                // Check if the status is "requested" before showing the dialog
                if ("requested".equals(status) && "not payed".equals(paymentStatus) && "not scanned".equals(qrStatus)) {
                    builder.setMessage("Generate QR ")
                            .setCancelable(false)
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Your approval logic here
                                    String Booking_id = list.get(position).getBooking_id();
                                    String User_id = list.get(position).getUser_id();
                                    String vehicleType = list.get(position).getVehicleType();
                                    String licenceNumber = list.get(position).getLicenceNumber();
                                    String slotNumber = list.get(position).getSlotNumber();
                                    String parkingTime = list.get(position).getParkingTime();
                                    String bookingStatus = list.get(position).getBookingStatus();
                                    String UserName = list.get(position).getUserName();
                                    String userAge = list.get(position).getUserAge();
                                    String userAddres = list.get(position).getUserAddres();
                                    String userMobile = list.get(position).getUserMobile();
                                    String userEmail = list.get(position).getUserEmail();

                                    Intent in = new Intent(getContext(), GenerateQRcode.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Booking_id", Booking_id);
                                    bundle.putString("User_id", User_id);
                                    bundle.putString("vehicleType", vehicleType);
                                    bundle.putString("licenceNumber", licenceNumber);
                                    bundle.putString("slotNumber", slotNumber);
                                    bundle.putString("parkingTime", parkingTime);
                                    bundle.putString("bookingStatus", bookingStatus);
                                    bundle.putString("UserName", UserName);
                                    bundle.putString("userAge", userAge);
                                    bundle.putString("userAddres", userAddres);
                                    bundle.putString("userMobile", userMobile);
                                    bundle.putString("userEmail", userEmail);

                                    in.putExtras(bundle);
                                    startActivity(in);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.setTitle("Delete or Approve Pass?");
                    alert.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1d1d1d"));
                            alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#94c3ab"));
                        }
                    });
                    alert.show();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                }
            }
        });

        getRequests();

        return root;
    }

    private void getRequests() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    RequestAdapter adapter = new RequestAdapter(getActivity(), list);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);
                } else {
                    Toast.makeText(getContext(), "NO_DATA", Toast.LENGTH_SHORT).show();
                    nodata.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String reg_id = prefs.getString("login_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "viewUserRequeststoAdmin");
                // map.put("id", reg_id);
                return map;
            }
        };
        queue.add(request);
    }
}
