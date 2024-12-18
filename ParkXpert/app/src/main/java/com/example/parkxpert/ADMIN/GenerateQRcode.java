package com.example.parkxpert.ADMIN;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateQRcode extends AppCompatActivity {

    String userName,employeeId,address,pass,phoneNUmber,mail;
    String reg_id;
    List<RequestPojo> list;
    TextView QRDrivingLicence,QRPhoneNumber,QRRegistrationNumber,QRName,QRaddress,QRslot,QRvehicleType,QrParkingTime;
    Button GenerateQrcode,sendToUser;

    //String Booking_ID,ADDRESS,PHONE_NUMBER,DRIVING_LICENCE,VEHICLE_TYPE,NAME,SLOT,PARKINGTIME;
    ImageView qrView;

    String bal;

    String booking_id,user_id,vehicleType,licenceNumber,slotNumber,parkingTime,bookingStatus,UserName,userAge,userAddres,userMobile,userEmail,userJoindate;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);


        Intent in=getIntent();
        booking_id=in.getExtras().getString("Booking_id");
        user_id=in.getExtras().getString("User_id");
        vehicleType=in.getExtras().getString("vehicleType");
        licenceNumber=in.getExtras().getString("licenceNumber");
        slotNumber=in.getExtras().getString("slotNumber");
        parkingTime=in.getExtras().getString("parkingTime");
        bookingStatus=in.getExtras().getString("bookingStatus");
        UserName=in.getExtras().getString("UserName");
        userAge=in.getExtras().getString("userAge");
        userAddres=in.getExtras().getString("userAddres");

        userMobile=in.getExtras().getString("userMobile");
        userEmail=in.getExtras().getString("userEmail");
        userJoindate=in.getExtras().getString("userJoindate");




        QRName=findViewById(R.id.QRName);
        QRaddress=findViewById(R.id.QRaddress);
        QRPhoneNumber=findViewById(R.id.QRphoneNumber);
        QRRegistrationNumber=findViewById(R.id.QrBookingId);
        QRDrivingLicence=findViewById(R.id.QrDrivingLicence);
        QRvehicleType=findViewById(R.id.QRvehicleType);
        GenerateQrcode=findViewById(R.id.GenerateQrcode);
        qrView=findViewById(R.id.Qrview);
        sendToUser=findViewById(R.id.sendToUser);
        QrParkingTime=findViewById(R.id.QrParkingTime);
        QRslot=findViewById(R.id.QRSlotNumber);




                    QRName.setText("Name :"+UserName);
                    QRaddress.setText("Address :"+userAddres);
                    QRPhoneNumber.setText("Phone :"+userMobile);
                    QRRegistrationNumber.setText("Qr Number :"+booking_id);
                    QRDrivingLicence.setText("licence Number :"+licenceNumber);
                    QRvehicleType.setText("Vechicle  :"+vehicleType);
                    QRslot.setText("Slot  :"+slotNumber);
                    QrParkingTime.setText("Parking Time :"+parkingTime);


        GenerateQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQr();
                sendToUser.setVisibility(View.VISIBLE);
                GenerateQrcode.setVisibility(View.GONE);

            }
        });

        sendToUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addQrCode();
            }
        });


    }

    private void addQrCode()
    {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {

                    Toast.makeText(getApplicationContext(), "Success ..!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), Admin.class);
                    startActivity(intent);

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
                map.put("key", "addQrCode");
                map.put("user_id",user_id );
               map.put("booking_id", booking_id);
                map.put("QrCode",bal);

                return map;
            }
        };
        queue.add(request);
    }


    private void generateQr()
    {
        QRCodeWriter writer = new QRCodeWriter();
        StringBuilder textTo = new StringBuilder();
       // System.out.println("PPIDDDD :" + pdtId);
        textTo.append(" Name :" +UserName + " Address :"  + userAddres + " Qr Number :" + booking_id + ":" + " Driving licence :" +  licenceNumber +  " Vehicle Type :" + vehicleType +" Slot Number :" + slotNumber + " Parking Time : "+ parkingTime + " Phone Number "+ userMobile );
        try {
            BitMatrix bitMatrix = writer.encode(textTo.toString(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrView.setImageBitmap(bmp);
            qrView.setVisibility(View.VISIBLE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
            byte[] b = baos.toByteArray();
            bal = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);
            System.out.println("Bar_QRCode :" + bal);
            //updatebarCode_pid(balqrcode, prd_id);

        } catch (WriterException e) {
            e.printStackTrace();
        }


    }




}