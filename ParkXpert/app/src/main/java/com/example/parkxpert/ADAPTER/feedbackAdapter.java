package com.example.parkxpert.ADAPTER;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parkxpert.COMMON.RequestPojo;
import com.example.parkxpert.R;

import java.util.List;

public class feedbackAdapter extends ArrayAdapter<RequestPojo> {

    Activity context;
    List<RequestPojo> rest_List;

    public feedbackAdapter(Activity context, List<RequestPojo> rest_List)
    {
        super(context, R.layout.activity_feedback_adapter, rest_List);
        this.context = context;
        this.rest_List = rest_List;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_feedback_adapter, null, true);


        TextView Name = (TextView) view.findViewById(R.id.USERNAME);
        TextView subject = (TextView) view.findViewById(R.id.SUBJECT);
        TextView description = (TextView) view.findViewById(R.id.DESCRIPTION);
        TextView ratings = (TextView) view.findViewById(R.id.RATINGS);
        TextView Slote=view.findViewById(R.id.SLOT_NUM);


        Name.setText(rest_List.get(position).getUserName());
        subject.setText(rest_List.get(position).getSubject());
        description.setText(rest_List.get(position).getDescription());
        ratings.setText(rest_List.get(position).getRating());
        Slote.setText(rest_List.get(position).getSlotNumber());







        return view;
    }

}