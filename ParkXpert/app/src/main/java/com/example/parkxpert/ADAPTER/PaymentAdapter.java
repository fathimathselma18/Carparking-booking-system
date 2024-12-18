package com.example.parkxpert.ADAPTER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class PaymentAdapter extends ArrayAdapter<RequestPojo> {

    Activity context;
    List<RequestPojo> rest_List;

    public PaymentAdapter(Activity context, List<RequestPojo> rest_List) {
        super(context, R.layout.activity_payment_adapter, rest_List);
        this.context = context;
        this.rest_List = rest_List;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_payment_adapter, parent, false);

        TextView NAME = view.findViewById(R.id.USER_NAME);
        TextView PRICE = view.findViewById(R.id.PRICE);
        TextView CARD_NUMBER = view.findViewById(R.id.CARDNUMBER);
        TextView CVV = view.findViewById(R.id.CVV);
        TextView EXPIRYDATE = view.findViewById(R.id.EXPIRYDATE);
        TextView ACC_NUMBER = view.findViewById(R.id.ACC_NUMBER);
        TextView DATE = view.findViewById(R.id.payment_date);

        NAME.setText(rest_List.get(position).getUserName());
        PRICE.setText(rest_List.get(position).getPaymentPrice());
        CARD_NUMBER.setText(rest_List.get(position).getCardNumber());
        CVV.setText(rest_List.get(position).getCvv());
        EXPIRYDATE.setText(rest_List.get(position).getExpiryDate());
        ACC_NUMBER.setText(rest_List.get(position).getAccountNumber());
        DATE.setText(rest_List.get(position).getPaymentDate());



        return view;
}
}