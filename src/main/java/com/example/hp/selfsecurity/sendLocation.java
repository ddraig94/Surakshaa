package com.example.hp.selfsecurity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class sendLocation extends ActionBarActivity {
    Button btnShowLocation;
    LoginDataBaseAdapter1 loginDataBaseAdapter1;
    GPSTracker gps;

    public String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_location);
        btnShowLocation=(Button)findViewById(R.id.btnShowLocation);
        loginDataBaseAdapter1=new LoginDataBaseAdapter1(this);
        loginDataBaseAdapter1.read();
        loginDataBaseAdapter1.GetMobileNumber();
        loginDataBaseAdapter1.close();


        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            public void showNum() {

            }


            @Override
            public void onClick(View v) {
                gps = new GPSTracker(sendLocation.this);
                if (gps.canGetlocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                 //Toast.makeText(getApplicationContext(), "Your location is: \n Lat: " +
                        //  latitude + " \n Long: " + longitude, Toast.LENGTH_LONG).show();
                    text="I am in danger. My current location is " +latitude+","+longitude;
                   // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    if (loginDataBaseAdapter1.Mobile_Number != null) {

                        for (int i = 0; i < loginDataBaseAdapter1.Mobile_Number.size(); i++) {

                            String tempMobileNumber = loginDataBaseAdapter1.Mobile_Number.get(i).toString();
                           // Toast.makeText(getApplicationContext(), tempMobileNumber, Toast.LENGTH_SHORT).show();
                           // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            sendSMS(tempMobileNumber,text);
                        }
                    }

                } else {
                    gps.showSettingsAlert();
                }
            }
        });

    }


    private void sendSMS(String phoneNumber, String message) {

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:

                        ContentValues values = new ContentValues();
                        for (int i = 0; i < loginDataBaseAdapter1.Mobile_Number.size() - 1; i++) {

                            values.put("address", loginDataBaseAdapter1.Mobile_Number.get(i).toString());
                            values.put("body", text);
                        }

                        getContentResolver().insert(Uri.parse("content://sms/sent"), values);

                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:

                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:

                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:

                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

}
