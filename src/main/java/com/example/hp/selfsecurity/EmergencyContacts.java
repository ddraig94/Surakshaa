package com.example.hp.selfsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class EmergencyContacts extends ActionBarActivity implements View.OnClickListener {
    Button bAddContacts,bViewContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_emergency_contacts);
        bAddContacts=(Button)findViewById(R.id.bAddcontacts);
        bAddContacts.setOnClickListener(this);

        bViewContacts=(Button)findViewById(R.id.bViewContacts);
        bViewContacts.setOnClickListener(this);

    }



    public void onClick(View v)
    { switch (v.getId())
    {
        case R.id.bAddcontacts:
            Intent Intent5 = new Intent(this, AddContacts.class);
            startActivity(Intent5);
            break;
        case R.id.bViewContacts:
            Intent Intent6 = new Intent(this, ViewContacts.class);
            startActivity(Intent6);
            break;


    }}




}
