package com.example.parkxpert.USER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Locale;
import java.util.Map;

public class EnterDetails extends AppCompatActivity {

    Spinner vehicletype;
    Button uploadButton;
    TextInputLayout uploadDrivingLicence;
    String Slot;
    TextView SlotNumber , Todayprice;
    TextView parkingTimeEditText, selectedTimeTextView;

    String selectedTime;
    String slotStatus;

    String SLOTNUMBER, VEHICLETYPE, PARKTIME, DRIVINGLICENS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);

        vehicletype = findViewById(R.id.vehicleType);
        uploadDrivingLicence = findViewById(R.id.uploadDrivingLicence);
        uploadButton = findViewById(R.id.uploadButton);
        SlotNumber = findViewById(R.id.sloteNumber);
        Todayprice = findViewById(R.id.Todayprice);
        parkingTimeEditText = findViewById(R.id.parkingTimeEditText);
        selectedTimeTextView = findViewById(R.id.selectedTimeTextView);

        Intent in = getIntent();
        Slot = in.getExtras().getString("slotNumber");

        SlotNumber.setText("Your Slot is : " + Slot);

        getPriceVolley();

        String[] category = {"Vehicle type", "Cars", "Motorcycles and Scooters", "Bicycles", "Small Trucks", "Electric Vehicles (EVs)", "Car Rentals", "Emergency Vehicles"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, category) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.quicksand_medium);
                    textView.setTypeface(typeface);
                    textView.setTextColor(Color.GRAY);
                    textView.setBackgroundColor(Color.LTGRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.quicksand_medium);
                    textView.setTypeface(typeface);
                }
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.quicksand_medium);
                textView.setTypeface(typeface);
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        vehicletype.setAdapter(arrayAdapter);

        parkingTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EnterDetails.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String amPm;
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                    if (hourOfDay > 12) {
                                        hourOfDay -= 12;
                                    }
                                } else {
                                    amPm = "AM";
                                }

                                // Handle the selected time with AM/PM
                                selectedTime = String.format("%02d:%02d %s", hourOfDay, minute, amPm);
                                selectedTimeTextView.setText(selectedTime);
                                selectedTimeTextView.setVisibility(View.VISIBLE);
                            }
                        },
                        0,
                        0,
                        false
                );

                timePickerDialog.show();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLOTNUMBER = Slot;
                VEHICLETYPE = vehicletype.getSelectedItem().toString();
                DRIVINGLICENS = uploadDrivingLicence.getEditText().getText().toString();
                PARKTIME = selectedTime;

                if (VEHICLETYPE.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Select Vehicle type", Toast.LENGTH_SHORT).show();
                } else if (DRIVINGLICENS.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter DL Number", Toast.LENGTH_SHORT).show();
                } else if (PARKTIME.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Select time", Toast.LENGTH_SHORT).show();
                } else {
                    addParkingDetails();
                    getParkingDetails();
                }
            }
        });
    }

    private void getPriceVolley()
    {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {

                    String DATA = response;
                    String resArr[] = DATA.trim().split("#");
                    String price=resArr[0];

                    System.out.println(resArr);
                    Todayprice.setText("Price :"+price +" /-");

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


    private void addParkingDetails() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Toast.makeText(getApplicationContext(), "Success ..!", Toast.LENGTH_SHORT).show();
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
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String login_id = prefs.getString("login_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "addParkingRequest");
                map.put("login_id", login_id);
                map.put("SLOTNUMBER", SLOTNUMBER);
                map.put("VEHICLETYPE", VEHICLETYPE);
                map.put("DRIVINGLICENS", DRIVINGLICENS);
                map.put("PARKTIME", PARKTIME);
                return map;
            }
        };
        queue.add(request);
    }

    private void getParkingDetails() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    String DATA = response;
                    String resArr[] = DATA.trim().split("#");
                    slotStatus = resArr[0];
                    Intent in = new Intent(EnterDetails.this, SelectSlot.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("slotNumber", SLOTNUMBER);
                    bundle.putString("slotStatus", slotStatus);
                    in.putExtras(bundle);
                    startActivity(in);
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
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String login_id = prefs.getString("login_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "getParkingRequest");
                map.put("login_id", login_id);
                map.put("SLOTNUMBER", Slot);
                return map;
            }
        };
        queue.add(request);
    }
}
