package com.example.parkxpert.ADAPTER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.parkxpert.COMMON.RequestPojo;
import com.example.parkxpert.R;

import java.util.List;

public class AdapterUser extends ArrayAdapter<RequestPojo> {

    Activity context;
    List<RequestPojo> list;
    Fragment fragment;

    public AdapterUser(Activity context, List<RequestPojo> list, Fragment fragment) {
        super(context, R.layout.activity_adapter_user, list);
        this.context = context;
        this.list = list;
        this.fragment=fragment;


    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_adapter_user, null, true);


        TextView UserName = view.findViewById(R.id.User_name);
        TextView userAge = view.findViewById(R.id.Age);
        TextView UserAddress = view.findViewById(R.id.Address);
        TextView Userphone = view.findViewById(R.id.mobile);
        TextView Useremail = view.findViewById(R.id.Email);
        TextView date_of_join = view.findViewById(R.id.date_of_join);



            UserName.setText(list.get(position).getUserName());
            userAge.setText("Age :"+list.get(position).getUserAge());
            Userphone.setText("Phone :"+list.get(position).getUserMobile());
            UserAddress.setText("Address:"+list.get(position).getUserAddres());
            Useremail.setText("Email:"+list.get(position).getUserEmail());
            date_of_join.setText("Join Date :"+list.get(position).getUserJoindate());

        return view;
    }
}