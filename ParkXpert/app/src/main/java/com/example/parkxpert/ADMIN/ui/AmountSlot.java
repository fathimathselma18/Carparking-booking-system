package com.example.parkxpert.ADMIN.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkxpert.COMMON.Utility;
import com.example.parkxpert.LoginActivity;
import com.example.parkxpert.R;
import com.example.parkxpert.databinding.FragmentAmountSloteBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class AmountSlot extends Fragment {

    private FragmentAmountSloteBinding binding;

    TextInputLayout Amount;
    String AMOUNT;
    Button submitButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAmountSloteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Amount=root.findViewById(R.id.Amount);
        submitButton=root.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAmount();
            }
        });


        return root;
    }

    private void uploadAmount()
    {
        AMOUNT=Amount.getEditText().getText().toString();
        UploadAmountVolly(AMOUNT);
    }

    private void UploadAmountVolly(String amount)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Already Exist")) {

                    Toast.makeText(getContext(), " Already Exists", Toast.LENGTH_SHORT).show();

                } else if (!response.trim().equals("failed")) {

                    Toast.makeText(getContext(), "Success ..!", Toast.LENGTH_SHORT).show();

                    getActivity().recreate();

                } else {
                    Toast.makeText(getContext(), " Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "my error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "AddAmount");
                map.put("amount", amount);

                return map;
            }
        };
        queue.add(request);
    }


}