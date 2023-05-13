package com.codelipi.ecomm;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codelipi.ecomm.startup.UserDbHelper;


public class SignUp extends AppCompatActivity {

    EditText signup_email,signup_firstname,signup_lastname,signup_password,signup_cpass,signup_number;
    Button signup_button;
    UserDbHelper userDbHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView signuptext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        initialize();
    }

    private void initialize() {
        signup_email= (EditText) findViewById(R.id.signup_email);
        signup_firstname= (EditText) findViewById(R.id.signup_firstname);
        signup_lastname= (EditText) findViewById(R.id.signup_lastname);
        signup_password= (EditText) findViewById(R.id.signup_password);
        signup_cpass= (EditText) findViewById(R.id.signup_cpass);
        signup_button= (Button) findViewById(R.id.signup_button);
        signup_number= (EditText) findViewById(R.id.signup_mobile);
        signuptext= (TextView) findViewById(R.id.signuptext);
        listeners();
        
        
    }

    private void listeners() {
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=signup_email.getText().toString();
                String pass=signup_password.getText().toString();
                String lastname=signup_lastname.getText().toString();
                String firstname=signup_firstname.getText().toString();
                String cpass=signup_cpass.getText().toString();
                String mob=signup_number.getText().toString();
                String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(!email.matches(emailpattern))
                {
                    Toast.makeText(getApplicationContext(), "Please provide valid email id", Toast.LENGTH_SHORT).show();
                }else if(firstname.length()<2){
                    Toast.makeText(getApplicationContext(), "Please provide first name", Toast.LENGTH_SHORT).show();
                }
                else if(lastname.length()<2){
                    Toast.makeText(getApplicationContext(), "Please provide last name", Toast.LENGTH_SHORT).show();
                }else if(mob.length()!=10)
                {
                    Toast.makeText(getApplicationContext(), "Please provide valid mobile number", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<6){
                    Toast.makeText(getApplicationContext(), "Please provide password of min 6 characters", Toast.LENGTH_SHORT).show();
                }
                else if(!pass.equals(cpass)){
                    Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT).show();
                }else
                {
                    userDbHelper=new UserDbHelper(getApplicationContext());
                    // 2.initialise the class
                    sqLiteDatabase=userDbHelper.getWritableDatabase();
                    userDbHelper.addInformations(firstname,mob,email,pass,lastname,sqLiteDatabase);
                    Toast.makeText(getBaseContext(),"Registered Succesfully", Toast.LENGTH_LONG).show();
                    userDbHelper.close();
                    Intent i=new Intent(SignUp.this,LoginActivity.class);
                    startActivity(i);
                }
            }
        });
        signuptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SignUp.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
