package com.example.hp.selfsecurity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {


    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        session = new SessionManager(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        session.checkLogin();

        Intent intentSuccess = new Intent(getApplicationContext(), Success.class);
        startActivity(intentSuccess);


    }


    protected void onStart(){
        super.onStart();
        //startActivity(new Intent(MainActivity.this, Login.class));


    }







}
