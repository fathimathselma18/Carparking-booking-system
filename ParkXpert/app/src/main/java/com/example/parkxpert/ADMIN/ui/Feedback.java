package com.example.parkxpert.ADMIN.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkxpert.ADAPTER.feedbackAdapter;
import com.example.parkxpert.COMMON.RequestPojo;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.R;
import com.example.parkxpert.databinding.FragmentFeedbackBinding;
import com.example.parkxpert.databinding.FragmentPaymentBinding;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Feedback extends Fragment {

    FragmentFeedbackBinding binding;

    ListView listView;

    List<RequestPojo> feedbackList;
    ImageView nodataa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView=root.findViewById(R.id.ViewfeedbackList);
        nodataa=root.findViewById(R.id.no_data);


        getAllFeedback();



        return root;
    }

    private void getAllFeedback()
    {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    feedbackList =  Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    feedbackAdapter adapter = new feedbackAdapter(getActivity(), feedbackList);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);
                } else {
                    nodataa.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "No data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "my Error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String reg_id = prefs.getString("reg_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "getFeedback");

                return map;
            }
        };
        queue.add(request);
    }
}