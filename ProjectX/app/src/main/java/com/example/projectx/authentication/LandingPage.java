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
    final String credentialsFile = "loginCreds";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        loginCredentials = getSharedPreferences(credentialsFile, MODE_PRIVATE);
        if (loginCredentials.getString("email", null) == null) {
            Log.e("shared prefernces", loginCredentials.getString("email", null));
            startActivity(new Intent(getBaseContext(), AuthenticationPage.class));
        }
        else {
            startActivity(new Intent(getBaseContext(), MainActivity.class));

        }

    }
}
