package com.example.projectx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projectx.authentication.AuthenticationPage;
import com.example.projectx.playlist.PlayListFull;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity {
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this,PlaylistEmpty.class);
//        startActivity(intent);
        final Button playMusic = (Button) findViewById(R.id.playMusic);
        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MusicPlayer.class));
            }
        });
        final Button playList = (Button) findViewById(R.id.openPlaylist);
        playList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), PlayListFull.class));

            }
        });
        final Button LOG_OUT = (Button) findViewById(R.id.logOut_bt);
        LOG_OUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCredentials =  getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = loginCredentials.edit();
                editor.clear();
                editor.commit();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getBaseContext(), AuthenticationPage.class));
                finish();


            }
        });
    }
}
