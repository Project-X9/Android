package com.example.projectx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projectx.ArtistFragment.ArtistFragment;
import com.example.projectx.authentication.AuthenticationPage;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends FragmentActivity {
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,
                R.id.artistfragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        Button logOut = (Button) findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
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
    public void storeCredentials(String credentialsFile, String email) {
        loginCredentials = getSharedPreferences(credentialsFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = loginCredentials.edit();
        editor.putString("email", email);
        editor.commit();
    }
}
