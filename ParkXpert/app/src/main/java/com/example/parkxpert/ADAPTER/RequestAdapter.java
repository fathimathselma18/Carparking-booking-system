package com.example.parkxpert.ADAPTER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.parkxpert.COMMON.RequestPojo;
import com.example.parkxpert.R;
import com.example.parkxpert.USER.Feedback;
import com.example.parkxpert.USER.PaymentUser;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RequestAdapter extends ArrayAdapter<RequestPojo> {

    Activity context;
    List<RequestPojo> list;
    Fragment fragment;

    MaterialButton payment,feedback;



    public RequestAdapter(Activity context, List<RequestPojo> list, Fragment fragment) {
        super(context, R.layout.activity_request_adapter, list);
        this.context = context;
        this.list = list;
        this.fragment=fragment;


    }
    public RequestAdapter(Activity context, List<RequestPojo> list)
    {
        super(context, R.layout.activity_request_adapter, list);
        this.context = context;
        this.list = list;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_request_adapter, null, true);


        TextView UserName = view.findViewById(R.id.User_name);
        TextView parkinTime = view.findViewById(R.id.parkinTime);
        TextView SlotNumber = view.findViewById(R.id.SlotNumber);
        TextView status = view.findViewById(R.id.status);
        payment=view.findViewById(R.id.paymentButton);
        feedback=view.findViewById(R.id.feedBack);

        if (list.get(position).getRequestType().equals("viewAdmin"))
        {
            UserName.setText(list.get(position).getUserName());
            parkinTime.setText("Parking Time :"+list.get(position).getParkingTime());
            SlotNumber.setText("Slot:"+list.get(position).getSlotNumber());

            if(list.get(position).getBookingStatus().equals("requested"))
            {
                status.setVisibility(View.VISIBLE);
                status.setText("Pending");
            }

            if (list.get(position).getBookingStatus().equals("Accepted") && list.get(position).getPaymentStatus().equals("not payed")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Accpted");
                //payment.setVisibility(View.VISIBLE);
            }
            if (list.get(position).getPaymentStatus().equals("payed"))
            {
                status.setVisibility(View.VISIBLE);
                status.setText("Payed");
            }

            if (list.get(position).getBookingStatus().equals("Available") && list.get(position).getQrStatus().equals("Scanned")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Scanned");
            }

        } else if (list.get(position).getRequestType().equals("viewUser"))
        {
            UserName.setVisibility(View.GONE);
            parkinTime.setText("Parking Time :"+list.get(position).getParkingTime());
            SlotNumber.setText("Slot:"+list.get(position).getSlotNumber());
            if(list.get(position).getBookingStatus().equals("requested"))
            {
                status.setVisibility(View.VISIBLE);
                status.setText("Pending");
            }
            if (list.get(position).getBookingStatus().equals("Accepted") && list.get(position).getPaymentStatus().equals("not payed")) {
            //status.setVisibility(View.VISIBLE);
            status.setText("Accpted");
            payment.setVisibility(View.VISIBLE);

            payment.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    RequestPojo currentItem = list.get(position);
                    String user_id = currentItem.getUser_id();
                    String Booking_id=currentItem.getBooking_id();



                    Intent in=new Intent(getContext(), PaymentUser.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", user_id);
                    System.out.println(user_id);
                    bundle.putString("Booking_id", Booking_id);
                    in.putExtras(bundle);
                    getContext().startActivity(in);






                }
            });
            }
            if (list.get(position).getPaymentStatus().equals("payed"))
            {
                status.setVisibility(View.VISIBLE);
                status.setText("Payed");
            }

            if (list.get(position).getBookingStatus().equals("Available") && list.get(position).getQrStatus().equals("Scanned")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Scanned");
                feedback.setVisibility(View.VISIBLE);

                feedback.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        RequestPojo currentItem = list.get(position);
                        String user_id = currentItem.getUser_id();
                        String Booking_id=currentItem.getBooking_id();

                        Intent in=new Intent(getContext(), Feedback.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", user_id);
                        System.out.println(user_id);
                        bundle.putString("Booking_id", Booking_id);
                        in.putExtras(bundle);
                        getContext().startActivity(in);
                    }
                });
            }
        }


        return view;
    }

}