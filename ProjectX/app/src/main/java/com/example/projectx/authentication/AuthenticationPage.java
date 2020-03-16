package com.example.projectx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projectx.MainActivity;
import com.example.projectx.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


public class AuthenticationPage extends AppCompatActivity {

    //private UsersDatabaseHelper usersDb;
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //FacebookSdk.setApplicationId("248449706186804");
        //FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_authentication_page);

        final Button signUpBt = (Button) findViewById(R.id.signUp_bt);

        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SignUpActivity.class));
            }

        });

        final Button signInBt = (Button) findViewById(R.id.signIn_bt);
        signInBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });

         callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        String userId = loginResult.getAccessToken().getUserId();

                        loginCredentials = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
                        SharedPreferences.Editor editor = loginCredentials.edit();
                        editor.putString("email", userId);
                        editor.commit();
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        String errorMessage = "Connection interrupted.";
                        Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_SHORT ).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        String errorMessage = "Couldn't connect to Facebook.";
                        Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_SHORT ).show();
                        Log.e("Facebook exception", exception.toString());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}