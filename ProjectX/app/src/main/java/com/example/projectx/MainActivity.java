package com.example.projectx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projectx.authentication.AuthenticationPage;

public class MainActivity extends AppCompatActivity {
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button LOG_OUT = (Button) findViewById(R.id.logOut_bt);
        LOG_OUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCredentials =  getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = loginCredentials.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getBaseContext(), AuthenticationPage.class));


            }
        });
    }
}
