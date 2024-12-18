package com.example.parkxpert.USER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkxpert.COMMON.RequestPojo;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.R;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSlot extends AppCompatActivity {

    private String selectedSlot = "";
    String Slot;

    List<RequestPojo> list;

    CardView[] cardViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_slot);

        cardViews = new CardView[]{
                findViewById(R.id.slot1), findViewById(R.id.slot2), findViewById(R.id.slot3), findViewById(R.id.slot4),
                findViewById(R.id.slot5), findViewById(R.id.slot6), findViewById(R.id.slot7), findViewById(R.id.slot8),
                findViewById(R.id.slot9), findViewById(R.id.slot10), findViewById(R.id.slot11), findViewById(R.id.slot12),
                findViewById(R.id.slot13), findViewById(R.id.slot14), findViewById(R.id.slot15), findViewById(R.id.slot16),
                findViewById(R.id.slot17), findViewById(R.id.slot18), findViewById(R.id.slot19), findViewById(R.id.slot20),
                findViewById(R.id.slot21), findViewById(R.id.slot22), findViewById(R.id.slot23), findViewById(R.id.slot24),
                findViewById(R.id.slot25), findViewById(R.id.slot26), findViewById(R.id.slot27), findViewById(R.id.slot28)
        };

        getSlotStatus();

        for (final CardView cardView : cardViews) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) cardView.getChildAt(0);
                    selectedSlot = textView.getText().toString();

                    Intent in = new Intent(SelectSlot.this, EnterDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("slotNumber", selectedSlot);
                    in.putExtras(bundle);
                    startActivity(in);
                }
            });
        }
    }

    private void getSlotStatus() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));

                    for (RequestPojo requestPOJO : list) {
                        String slot = requestPOJO.getSlotNumber();
                        String status = requestPOJO.getBookingStatus();


                        System.out.println("Slot: " + slot + ", Status: " + status);
                        Log.d("ParsedData", "Slot: " + slot + ", Status: " + status);

                        // Change card color based on status
                        int cardColor = getResources().getColor(R.color.white); // Set your default color here
                        if ("requested".equals(status)) {
                            cardColor = getResources().getColor(R.color.green); // Change to your requested color
                        } else if ("Accepted".equals(status)) {
                            cardColor = getResources().getColor(R.color.red); // Change to your accepted color
                        }


                        // Find the card view with the corresponding slot number
                        for (CardView cardView : cardViews) {
                            TextView textView = (TextView) cardView.getChildAt(0);
                            String cardSlot = textView.getText().toString();
                            if (slot != null && slot.equals(cardSlot)) {
                                cardView.setCardBackgroundColor(cardColor);

                                if (!"Available".equals(status) && !"requested".equals(status) ) {
                                    cardView.setEnabled(false);
                                    cardView.setClickable(false);
                                }
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Request Pending", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("VolleyError", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String login_id = prefs.getString("login_id", "No_id");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "getSlotStatus");
                // Add any additional parameters needed for your request
                return map;
            }
        };
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
     startActivity(new Intent(getApplicationContext(), UserHome.class));
    }
}
