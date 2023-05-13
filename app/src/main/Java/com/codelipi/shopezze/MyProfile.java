package com.codelipi.ecomm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codelipi.ecomm.startup.MainActivity;
import com.codelipi.ecomm.startup.UserDbHelper;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class MyProfile extends AppCompatActivity {
    UserDbHelper userDbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    Button signup_button;

    EditText signup_email,signup_firstname,signup_lastname,signup_password,signup_number;
    String loginemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initialize();
    }

    private void initialize() {
        signup_email= (EditText) findViewById(R.id.signup_email);
        signup_firstname= (EditText) findViewById(R.id.signup_firstname);
        signup_lastname= (EditText) findViewById(R.id.signup_lastname);
        signup_password= (EditText) findViewById(R.id.signup_password);
        signup_button= (Button) findViewById(R.id.signup_button);
        signup_number= (EditText) findViewById(R.id.signup_mobile);
        SharedPreferences pref = getSharedPreferences("MyPref1", 0);
        String logintype = pref.getString("logintype",null);
        loginemail = pref.getString("loginemail",null);
        userDbHelper=new UserDbHelper(getApplicationContext());
        sqLiteDatabase=userDbHelper.getWritableDatabase();
        cursor =userDbHelper.getContact(loginemail,sqLiteDatabase);
        if(cursor.moveToFirst())
        {
            String name=cursor.getString(0);
            String mobile=cursor.getString(1);
            String email=cursor.getString(2);
            String lname=cursor.getString(3);
            String pass=cursor.getString(4);
            signup_email.setText(email);
            signup_number.setText(mobile+"");
            signup_firstname.setText(name);
            signup_lastname.setText(lname);
            signup_password.setText(pass);
        }
        else
        {

        }
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtext=signup_email.getText().toString();
                String passtext=signup_password.getText().toString();
                String lastnametext=signup_lastname.getText().toString();
                String firstnametext=signup_firstname.getText().toString();
                String mobtext=signup_number.getText().toString();
                String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(!emailtext.matches(emailpattern))
                {
                    Toast.makeText(getApplicationContext(), "Please provide valid email id", Toast.LENGTH_SHORT).show();
                }else if(firstnametext.length()<2){
                    Toast.makeText(getApplicationContext(), "Please provide first name", Toast.LENGTH_SHORT).show();
                }
                else if(lastnametext.length()<2){
                    Toast.makeText(getApplicationContext(), "Please provide last name", Toast.LENGTH_SHORT).show();
                }else if(mobtext.length()!=10)
                {
                    Toast.makeText(getApplicationContext(), "Please provide valid mobile number", Toast.LENGTH_SHORT).show();
                }
                else if(passtext.length()<6){
                    Toast.makeText(getApplicationContext(), "Please provide password of min 6 characters", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    UserDbHelper userDbHelper=new UserDbHelper(getApplicationContext());
                    sqLiteDatabase=userDbHelper.getWritableDatabase();
                    String name,email,mobile;
                    int count=userDbHelper.updateInformation(emailtext,passtext,lastnametext,mobtext,firstnametext,sqLiteDatabase);
                    Toast.makeText(getApplicationContext(),count+" Updated succesfully", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(MyProfile.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        });
    }
}
