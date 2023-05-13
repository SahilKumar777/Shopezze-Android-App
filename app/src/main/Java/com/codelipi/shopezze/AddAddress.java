package com.codelipi.ecomm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codelipi.ecomm.startup.UserDbHelper;


public class AddAddress extends AppCompatActivity {

    EditText address,street,city,state,pincode;
    Button addadress;
    UserDbHelper userDbHelper;
    SQLiteDatabase sqLiteDatabase;
    String loginemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaddress);
        initialize();
    }

    private void initialize() {
        SharedPreferences pref = getSharedPreferences("MyPref1", 0);
        String logintype = pref.getString("logintype",null);
         loginemail = pref.getString("loginemail",null);

        address= (EditText) findViewById(R.id.address);
        street= (EditText) findViewById(R.id.street);
        city= (EditText) findViewById(R.id.city);
        state= (EditText) findViewById(R.id.state);
        pincode= (EditText) findViewById(R.id.pincode);
        addadress= (Button) findViewById(R.id.addadress);
        listners();
    }

    private void listners() {
        addadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addresstext=address.getText().toString();
                String streettext=street.getText().toString();
                String citytext=city.getText().toString();
                String statetext=state.getText().toString();
                String pincodetext=pincode.getText().toString();
                if(addresstext.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please provide address", Toast.LENGTH_SHORT).show();
                }else if(streettext.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please provide street address", Toast.LENGTH_SHORT).show();
                }else if(citytext.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please provide city", Toast.LENGTH_SHORT).show();
                }else if(statetext.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please provide  state", Toast.LENGTH_SHORT).show();
                }else if(pincodetext.length()!=6)
                {
                    Toast.makeText(getApplicationContext(), "Please provide pincode", Toast.LENGTH_SHORT).show();
                }else
                {
                    userDbHelper=new UserDbHelper(getApplicationContext());
                    // 2.initialise the class
                    sqLiteDatabase=userDbHelper.getWritableDatabase();
                    userDbHelper.addAddress(loginemail,addresstext,streettext,citytext,statetext,pincodetext,sqLiteDatabase);
                    Toast.makeText(getBaseContext(),"Address Added", Toast.LENGTH_LONG).show();
                    userDbHelper.close();
                    Intent i=new Intent(AddAddress.this,ViewAddress.class);
                    startActivity(i);
                }
            }
        });
    }
}
