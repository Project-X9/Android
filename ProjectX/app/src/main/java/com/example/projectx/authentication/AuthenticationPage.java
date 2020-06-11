package com.example.projectx.authentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectx.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


public class AuthenticationPage extends AppCompatActivity {

    SharedPreferences loginCredentials;
    SharedPreferences service;
    final String SERVICE_FILE = "serviceChoice";
    final String CREDENTIALS_FILE = "loginCreds";
    CallbackManager callbackManager;
    public static Activity authenticationPage;
    EditText mIp;

    boolean testing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        authenticationPage = this;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authentication_page);
        final Button signUpBt = (Button) findViewById(R.id.signUp_bt);
        mIp = findViewById(R.id.ip_et);
        final Switch mockSwitch = (Switch) findViewById(R.id.mock);
        mockSwitch.setClickable(false);
        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String mIP=mIp.getText().toString();
                preferences.edit().putString("IpAddressTextPref",mIP).commit();
                //starts the Sign up page if the user presses on the sign up button
                startActivity(new Intent(getBaseContext(), SignUpActivity.class));
                saveMockStatus(mockSwitch);
                signUpBt.setClickable(false);


            }

        });
        final Button signInBt = (Button) findViewById(R.id.signIn_bt);
        signInBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts the Sign in page if the user presses on the sign in button
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                signInBt.setClickable(false);
                saveMockStatus(mockSwitch);

            }
        });

        callbackManager = CallbackManager.Factory.create(); //Facebook code

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //if Facebook login succeeds, get the user's ID from the access token
                        String userId = loginResult.getAccessToken().getUserId();
                        storeCredentials(CREDENTIALS_FILE, userId);
                        //startActivity(new Intent(getBaseContext(), MainActivity.class));
                        //starts the Main Activity on success
                        saveMockStatus(mockSwitch);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        makeToast("Connection interrupted.");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        makeToast("Couldn't connect to Facebook.");
                        Log.e("Facebook exception", exception.toString());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * creates a toast message and displays it with duration Toast.LENGTH_SHORT
     *
     * @param message, string you want to show the user in a toast;
     */
    public void makeToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Stores User credentials by opening a file and storing user email or Facebook ID, so user does
     * not login every time
     *
     * @param credentialsFile the name of the file to store user credentials in
     * @param email           the user's email or Facebook ID
     */
    public void storeCredentials(String credentialsFile, String email) {
        loginCredentials = getSharedPreferences(credentialsFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = loginCredentials.edit();
        editor.putString("email", email);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Button signInBt = (Button) findViewById(R.id.signIn_bt);
        final Button signUpBt = (Button) findViewById(R.id.signUp_bt);
        signUpBt.setClickable(true);
        signInBt.setClickable(true);
    }

    public void saveMockStatus(Switch mockSwitch) {
        if (mockSwitch.isChecked()) {
            service = getSharedPreferences(SERVICE_FILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = service.edit();
            editor.putString("Mocked", "true");
            editor.commit();
        } else {
            service = getSharedPreferences(SERVICE_FILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = service.edit();
            editor.putString("Mocked", "false");
            editor.commit();
        }
    }
}