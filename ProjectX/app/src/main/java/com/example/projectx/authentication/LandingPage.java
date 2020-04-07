package com.example.projectx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.projectx.MainActivity;
import com.example.projectx.R;

public class LandingPage extends AppCompatActivity {
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Log.e("landing oncreate", "Landing is active");
        loginCredentials = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
        String loggedIn = loginCredentials.getString("id", null);
        if (loggedIn == null) {
            startActivity(new Intent(getBaseContext(), AuthenticationPage.class));
            finish();
        }
        else {

                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("landing",  "onResume of Landing activated");
    }
}
