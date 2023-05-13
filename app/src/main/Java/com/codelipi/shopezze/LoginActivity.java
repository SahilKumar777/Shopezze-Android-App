package com.codelipi.ecomm;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codelipi.ecomm.startup.MainActivity;
import com.codelipi.ecomm.startup.UserDbHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText login_email,login_password;
    TextView login_forgot;
    Button login_button,facebook_button,google_button;
    UserDbHelper userDbHelper;
    SQLiteDatabase sqLiteDatabase;
    RelativeLayout facebook_buttonlayout;
    String loginemail;
    private FirebaseAuth mAuth;
    Cursor cursor;
    private GoogleSignInClient mGoogleSignInClient;
    TextView login_create;
    private static final int RC_SIGN_IN = 9001;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initialize();
    }

    private void initialize() {
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences pref = getSharedPreferences("MyPref1", 0);
        String logintype = pref.getString("logintype",null);
        loginemail = pref.getString("loginemail",null);

        login_email= (EditText) findViewById(R.id.login_email);
        login_password= (EditText) findViewById(R.id.login_password);
        login_forgot= (TextView) findViewById(R.id.login_forgot);
        login_button= (Button) findViewById(R.id.login_button);
        login_create= (TextView) findViewById(R.id.login_create);
        facebook_button=findViewById(R.id.facebook_button);
        google_button=findViewById(R.id.google_button);
        listeners();

    }

    private void listeners() {
        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Login","Here");
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile","user_photos"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Login","Here");

                        getFacebookResult(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Login","cancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        Log.d("Login","Error"+exception);

                    }
                });
            }
        });


        login_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,SignUp.class);
                startActivity(i);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtext=login_email.getText().toString();
                String psstext=login_password.getText().toString();
                String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(!emailtext.matches(emailpattern))
                {
                    Toast.makeText(getApplicationContext(), "Please provide valid email id", Toast.LENGTH_SHORT).show();
                }
                else if(psstext.length()<2){
                    Toast.makeText(getApplicationContext(), "Please provide valid password", Toast.LENGTH_SHORT).show();
                }else
                {
                    userDbHelper=new UserDbHelper(getApplicationContext());
                    sqLiteDatabase=userDbHelper.getWritableDatabase();
                    cursor =userDbHelper.checkInformations(emailtext,psstext,sqLiteDatabase);
                    if(cursor.moveToFirst())
                    {
                        String name=cursor.getString(2);
                        SharedPreferences pref = getSharedPreferences("MyPref1", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("logintype","Userlogin");
                        editor.putString("loginname",name);
                        editor.putString("loginemail",emailtext);
                        editor.commit();
                        Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"EMail and password not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        login_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotDialog();
            }
        });
    }
    private void getFacebookResult(LoginResult loginResult)
    {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {

                        AccessToken accessToken=AccessToken.getCurrentAccessToken();
                        Log.d("access  ",accessToken.getUserId()+"   "+accessToken.getApplicationId()+"  "+accessToken.getToken());
                        //accesstoken=accessToken.getToken();
                        //callFbPicApi(accessToken.getUserId());
                       // callSocialLogin(accessToken.getToken(),"facebook");

                        Log.d("example  ","Json    "+json_object+"    "+response);
                        if(json_object!=null)
                        {
                            String email = json_object.optString("email");
                            Log.d("Login status",email+"  "+json_object);
                            String fname = json_object.optString("first_name");
                            String lname=json_object.optString("last_name");
                            userDbHelper=new UserDbHelper(getApplicationContext());
                            sqLiteDatabase=userDbHelper.getReadableDatabase();
                            Cursor cursor=userDbHelper.getContact(email,sqLiteDatabase);
                            if(cursor.moveToFirst())
                            {
                                SharedPreferences pref = getSharedPreferences("MyPref1", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("logintype","Userlogin");
                                editor.putString("loginname",fname);
                                editor.putString("loginemail",email);
                                editor.commit();
                                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }else
                            {
                                userDbHelper=new UserDbHelper(getApplicationContext());
                                // 2.initialise the class
                                sqLiteDatabase=userDbHelper.getWritableDatabase();
                                userDbHelper.addInformations(fname,"",email,"facebook",lname,sqLiteDatabase);
                                SharedPreferences pref = getSharedPreferences("MyPref1", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("logintype","Userlogin");
                                editor.putString("loginname",fname);
                                editor.putString("loginemail",email);
                                editor.commit();
                                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }

                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,email,first_name,last_name,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google", "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("Google", "firebaseAuthWithGoogle:" + acct.getId()+"    "+acct.getIdToken());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //callSocialLogin(acct.getIdToken(),"google");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Google","signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userDbHelper=new UserDbHelper(getApplicationContext());
                            sqLiteDatabase=userDbHelper.getReadableDatabase();
                            Cursor cursor=userDbHelper.getContact(user.getEmail(),sqLiteDatabase);
                            if(cursor.moveToFirst())
                            {
                                SharedPreferences pref = getSharedPreferences("MyPref1", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("logintype","Userlogin");
                                editor.putString("loginname",user.getDisplayName());
                                editor.putString("loginemail",user.getEmail());
                                editor.commit();
                                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }else
                            {
                                userDbHelper=new UserDbHelper(getApplicationContext());
                                // 2.initialise the class
                                sqLiteDatabase=userDbHelper.getWritableDatabase();
                                userDbHelper.addInformations(user.getDisplayName(),"",user.getEmail(),"google","",sqLiteDatabase);
                                SharedPreferences pref = getSharedPreferences("MyPref1", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("logintype","Userlogin");
                                editor.putString("loginname",user.getDisplayName());
                                editor.putString("loginemail",user.getEmail());
                                editor.commit();
                                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            //  Toast.makeText(getApplicationContext(),user.getProviderId()+" "+user.getDisplayName()+" "+user.getEmail()+" "+user.getPhotoUrl(),Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Google", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                    }
                });
    }

    private void ForgotDialog() {
        final Dialog dialog = new Dialog(LoginActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
        if(dialog.getWindow()!=null)
        {
            dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        dialog.setContentView(R.layout.forgot_password);
        dialog.setCancelable(false);
        final TextView cancel= (TextView) dialog.findViewById(R.id.forgot_cancel);
        final EditText email= (EditText) dialog.findViewById(R.id.forgot_email);
        final Button continue1= (Button) dialog.findViewById(R.id.forgot_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1=email.getText().toString().trim();
                String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(email1.matches(emailpattern))
                {
                    userDbHelper=new UserDbHelper(getApplicationContext());
                    sqLiteDatabase=userDbHelper.getReadableDatabase();
                    Cursor cursor=userDbHelper.getContact(email1,sqLiteDatabase);
                    if(cursor.moveToFirst())
                    {
                        String MOB=cursor.getString(4);
                        Toast.makeText(getApplicationContext(),"Password is "+MOB, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Email not exist "+email1, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
