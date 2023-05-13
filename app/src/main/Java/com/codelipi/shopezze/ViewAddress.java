package com.codelipi.ecomm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.codelipi.ecomm.startup.MainActivity;
import com.codelipi.ecomm.startup.UserDbHelper;

public class ViewAddress extends AppCompatActivity {
    Button addaddress;
    UserDbHelper userDbHelper;
    SQLiteDatabase sqLiteDatabase;
    String loginemail;
    ListDataAdapter listDataAdapter;
    ListView addresslist;
    String from;
    String total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_address);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        initialize();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ViewAddress.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void initialize() {
        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            total = getIntent().getStringExtra("Total");

        }
        SharedPreferences pref = getSharedPreferences("MyPref1", 0);
        String logintype = pref.getString("logintype",null);
        loginemail = pref.getString("loginemail",null);
        addaddress= (Button) findViewById(R.id.addaddress);

        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ViewAddress.this,AddAddress.class);
                startActivity(i);
            }
        });

        addresslist= (ListView) findViewById(R.id.list_view);
        userDbHelper=new UserDbHelper(getApplicationContext());
        sqLiteDatabase=userDbHelper.getReadableDatabase();
        listDataAdapter=new ListDataAdapter(getApplicationContext(),R.layout.addresslist,from,total);
        addresslist.setAdapter(listDataAdapter);
        Cursor cursor=userDbHelper.getAddress(loginemail,sqLiteDatabase);
        if(cursor.moveToFirst())
        {
            do
            {
                String pin,state,city,street,email,address;
                pin=cursor.getString(0);
                state=cursor.getString(1);
                city=cursor.getString(2);
                street=cursor.getString(3);
                email=cursor.getString(4);
                address=cursor.getString(5);
                DataProvider dataProvider=new DataProvider(pin,state,city,street,email,address);
                listDataAdapter.add(dataProvider);
            }
            while(cursor.moveToNext());
        }
        }
    }

