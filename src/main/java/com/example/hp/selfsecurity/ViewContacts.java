package com.example.hp.selfsecurity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ViewContacts extends ActionBarActivity {
    LoginDataBaseAdapter1 loginDataBaseAdapter1;
    ListView Contact_listview;
    ArrayList<Contact> contact_data = new ArrayList<Contact>();
    Contact_Adapter cAdapter;
    String toast_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_view_contacts);
        loginDataBaseAdapter1 = new LoginDataBaseAdapter1(this);

        //loginDataBaseAdapter1.read();
        //loginDataBaseAdapter1.printTableData();
        //
        // loginDataBaseAdapter1.close();


        try {
            Contact_listview = (ListView) findViewById(R.id.list);
            Contact_listview.setItemsCanFocus(false);
            Set_Refresh_data();
        } catch (Exception e) {
            Log.e("Some Error", "" + e);
        }
    }


    public void Set_Refresh_data(){
        contact_data.clear();
        loginDataBaseAdapter1.read();
        ArrayList<Contact> contact_array_from_db=loginDataBaseAdapter1.Get_Contacts();
        for (int i = 0; i < contact_array_from_db.size(); i++) {

            int tidno = contact_array_from_db.get(i).getID();
            String name = contact_array_from_db.get(i).getName();
            String mobile = contact_array_from_db.get(i).getPhoneNumber();

            Contact cnt = new Contact();
            cnt.setID(tidno);
            cnt.setName(name);

            cnt.setPhoneNumber(mobile);

            contact_data.add(cnt);
        }
        loginDataBaseAdapter1.close();
        cAdapter = new Contact_Adapter(ViewContacts.this, R.layout.listview_row,
                contact_data);
        Contact_listview.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
    }
    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
       Set_Refresh_data();

    }
    public class Contact_Adapter extends ArrayAdapter<Contact> {
        Activity activity;
        int layoutResourceId;
        Contact user;
        ArrayList<Contact> data = new ArrayList<Contact>();

        public Contact_Adapter(Activity act, int layoutResourceId,
                               ArrayList<Contact> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            UserHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.name = (TextView) row.findViewById(R.id.user_name_txt);
                //holder.email = (TextView) row.findViewById(R.id.user_email_txt);
                holder.number = (TextView) row.findViewById(R.id.user_mob_txt);
                //holder.edit = (Button) row.findViewById(R.id.btn_update);
                holder.delete = (Button) row.findViewById(R.id.btn_delete);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            user = data.get(position);
           // holder.edit.setTag(user.getID());
            holder.delete.setTag(user.getID());
            holder.name.setText(user.getName());
           // holder.email.setText(user.getEmail());
            holder.number.setText(user.getPhoneNumber());

            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub

                    // show a message while loader is loading

                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete ");
                    final int user_id = Integer.parseInt(v.getTag().toString());
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // MyDataObject.remove(positionToRemove);
                                    LoginDataBaseAdapter1 loginDataBaseAdapter1 = new LoginDataBaseAdapter1(
                                            activity.getApplicationContext());
                                    loginDataBaseAdapter1.open();
                                    loginDataBaseAdapter1.Delete_Contact(user_id);
                                    ViewContacts.this.onResume();
                                    loginDataBaseAdapter1.close();

                                }
                            });
                    adb.show();
                }

            });
            return row;

        }

        class UserHolder {
            TextView name;

            TextView number;

            Button delete;
        }

    }

}