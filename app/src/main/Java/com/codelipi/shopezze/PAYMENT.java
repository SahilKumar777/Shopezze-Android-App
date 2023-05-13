package com.codelipi.ecomm;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codelipi.ecomm.startup.MainActivity;
import com.codelipi.ecomm.startup.UserDbHelper;

public class PAYMENT extends AppCompatActivity {
    TextView amount,Cancelled;
    EditText CardNo,Name,Cvv,ExpiryDate,Pass;
    Button payment;
    String Tamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amount=findViewById(R.id.totalAmount);
        payment=findViewById(R.id.payU);
        CardNo=findViewById(R.id.cardNo);
        Name=findViewById(R.id.name);
        Cvv=findViewById(R.id.cvv);
        ExpiryDate=findViewById(R.id.expiryDate);
        Pass=findViewById(R.id.pass);


        Tamount= getIntent().getStringExtra("Tamount");
        amount.setText(Tamount);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNo=CardNo.getText().toString();
                String name=Name.getText().toString();
                String cvv=Cvv.getText().toString();
                String expDate=ExpiryDate.getText().toString();
                String pass=Pass.getText().toString();

                if (cardNo.length()!=16)
                {
                    Toast.makeText(getApplicationContext(),"Enter 16 digit card No.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (name.length()==0||name.matches("[0-9]"))
                    {
                        Toast.makeText(getApplicationContext(),"Enter a valid name", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (cvv.length()!=3)
                        {
                            Toast.makeText(getApplicationContext(),"Enter valid cvv", Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (expDate.length()==0)
                            {
                                Toast.makeText(getApplicationContext(),"Enter valid Date", Toast.LENGTH_LONG).show();
                            }
                            else {
                                if (pass.length()!=4)
                                {
                                    Toast.makeText(getApplicationContext(),"Enter 4 digit card password", Toast.LENGTH_LONG).show();
                                }
                                else {
                                     otpDialog();
                                }
                            }
                        }
                    }
                }

            }
        });

        Cancelled=findViewById(R.id.cancelPay);
        Cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Payment Cancelled", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });




    }

    public void otpDialog()
    {
        final Dialog dialog = new Dialog(PAYMENT.this,android.R.style.Theme_Translucent_NoTitleBar);
        if(dialog.getWindow()!=null)
        {
            dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        dialog.setContentView(R.layout.forgot_password);
        dialog.setCancelable(false);
        final TextView title=(TextView) dialog.findViewById(R.id.title);
        final TextView cancel= (TextView) dialog.findViewById(R.id.forgot_cancel);
        final EditText email= (EditText) dialog.findViewById(R.id.forgot_email);
        final Button continue1= (Button) dialog.findViewById(R.id.forgot_button);

        title.setText("CONFIRM OTP");
        email.setHint("Enter OTP");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 =email.getText().toString();
                if(email1.length()==4)
                {
                    Toast.makeText(getApplicationContext(),"Order Placed", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid OTP ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}
