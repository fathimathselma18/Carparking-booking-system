package com.example.parkxpert.ADMIN.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.parkxpert.ADAPTER.AdapterUser;
import com.example.parkxpert.COMMON.RequestPojo;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.R;
import com.example.parkxpert.databinding.FragmentUsersBinding;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User extends Fragment {

    ListView listView;

    List<RequestPojo> list;

    ImageView nodata;

    AlertDialog.Builder builder;

    String status;

    private FragmentUsersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView=root.findViewById(R.id.viewUserRequests);
        nodata=root.findViewById(R.id.no_data);
        builder=new AlertDialog.Builder(getActivity());

        getUser();


        return root;
    }


    private void getUser()
    {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    AdapterUser adapter = new AdapterUser(getActivity(), list, User.this);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);
                    RequestPojo requestPOJO = list.get(0);
                    status = requestPOJO.getBookingStatus();


                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + status);

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
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String reg_id = prefs.getString("login_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "viewUsertoAdmin");
                //map.put("id", reg_id);
                return map;
            }

        };
        queue.add(request);
    }



}