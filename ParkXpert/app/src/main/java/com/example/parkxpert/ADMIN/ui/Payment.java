package com.example.parkxpert.ADMIN.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkxpert.ADAPTER.PaymentAdapter;
import com.example.parkxpert.COMMON.RequestPojo;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.R;
import com.example.parkxpert.databinding.FragmentAmountSloteBinding;
import com.example.parkxpert.databinding.FragmentPaymentBinding;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Payment extends Fragment {
 FragmentPaymentBinding binding;
    ListView listView;

    List<RequestPojo> paymentlist;
    ImageView nodataa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView=root.findViewById(R.id.viewBankDetails);
        nodataa=root.findViewById(R.id.no_data);

        getPaymentDetails();

            return root;
    }

    private void getPaymentDetails()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {

                    Gson gson = new Gson();
                    paymentlist = Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    PaymentAdapter adapter = new PaymentAdapter(getActivity(),paymentlist);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);

                } else {
                    listView.setVisibility(View.GONE);
                    nodataa.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "NO_DATA", Toast.LENGTH_SHORT).show();

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String reg_id = prefs.getString("reg_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "ViewBankingPaymentDetails");

                return map;
            }

        };
        queue.add(request);
    }
}