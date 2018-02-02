package com.example.hp.selfsecurity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class Success extends ActionBarActivity implements View.OnClickListener {
    Button bLogout,bEmerContacts,bRecord,bSendlocation,bAlarm;
    private MediaPlayer mp;
    //UserLocalStore userLocalStore;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManager session;



    LoginDataBaseAdapter1 loginDataBaseAdapter1;
    GPSTracker gps;

    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        loginDataBaseAdapter1=new LoginDataBaseAdapter1(this);
        loginDataBaseAdapter1.read();
        loginDataBaseAdapter1.GetMobileNumber();
        loginDataBaseAdapter1.close();



        // Session class instance
       session = new SessionManager(getApplicationContext());

        TextView lblName = (TextView) findViewById(R.id.lblName);
      // TextView lblEmail = (TextView) findViewById(R.id.lblEmail);


      // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
      //  String email = user.get(SessionManager.KEY_EMAIL);

        // displaying user data
        lblName.setText(Html.fromHtml("Hello  <b>" + name + "</b><br><br>"));
      //  lblEmail.setText(Html.fromHtml("Email: <b>" + email + "</b>"));




        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        bAlarm=(Button)findViewById(R.id.bAlarm);
        bAlarm.setOnClickListener(this);

        bLogout=(Button)findViewById(R.id.bLogout);
        //bLogout.setOnClickListener(this);

        /*bEmerContacts=(Button)findViewById(R.id.bEmerContacts);
        bEmerContacts.setOnClickListener(this);*/

        bRecord=(Button)findViewById(R.id.bRecord);
        //bRecord.setOnClickListener(this);

        bSendlocation=(Button)findViewById(R.id.bSendlocation);
        //bSendlocation.setOnClickListener(this);

        bSendlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(Success.this);
                if (gps.canGetlocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    //Toast.makeText(getApplicationContext(), "Your location is: \n Lat: " +
                    //  latitude + " \n Long: " + longitude, Toast.LENGTH_LONG).show();
                    text="I am in danger. My current location is " +
                            "http://maps.google.com/maps?q=" +latitude+","+longitude;
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    if (loginDataBaseAdapter1.Mobile_Number != null) {

                        for (int i = 0; i < loginDataBaseAdapter1.Mobile_Number.size(); i++) {

                            String tempMobileNumber = loginDataBaseAdapter1.Mobile_Number.get(i).toString();
                            //Toast.makeText(getApplicationContext(), tempMobileNumber, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            sendSMS(tempMobileNumber,text);
                        }
                    }

                } else {
                    gps.showSettingsAlert();
                }
            }
        });

       // userLocalStore = new UserLocalStore(this);
    }


    public void onClick(View v) {
        int resId = 0;
        switch (v.getId()) {

           /* case R.id.bEmerContacts:
                Intent Intent1 = new Intent(this, EmergencyContacts.class);
                startActivity(Intent1);
                break;
            case R.id.bSendlocation:
                Intent Intent2 = new Intent(this, sendLocation.class);
                startActivity(Intent2);
                break;
            case R.id.bRecord:
                Intent Intent3 = new Intent(this, Record.class);
                startActivity(Intent3);
                break;*/
            case R.id.bAlarm:
                resId=R.raw.a;
                break;
          /*  case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLogedin(false);

                startActivity(new Intent(this, Login.class));
                break;*/


        }

        if(mp!=null)
            mp.release();

        mp= MediaPlayer.create(this,resId);
        mp.start();
    }

    public void onDestroy(){
        if(null!=mp)
            mp.release();
        super.onDestroy();
    }
  /*  public void openSendLoc(View v){
        Intent Intent1 = new Intent(this, sendLocation.class);
        startActivity(Intent1);
    }*/
    public void openEmerContacts(View v){
        Intent Intent1 = new Intent(this, EmergencyContacts.class);
        startActivity(Intent1);
    }
    public void openRecord(View v){
        Intent Intent1 = new Intent(this, Record.class);
        startActivity(Intent1);
    }
    public void openLogout(View v){
        session.logoutUser();
        Intent Intent1 = new Intent(this, Login.class);
        startActivity(Intent1);
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
