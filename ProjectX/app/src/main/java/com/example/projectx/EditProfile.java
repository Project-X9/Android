package com.example.projectx;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.preference.PreferenceFragment;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {

    private ImageButton backButton;
    private TextView topName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findObjects();
        setListeners();
        initializeElements();
    }

    private void initializeElements() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        preferences.edit().putBoolean("NotificationsCheckbox", true).commit();
        preferences.edit().putString("EditNameTextPref","").commit();
        preferences.edit().putString("EditPasswordTextPref","").commit();
        preferences.edit().putString("EditEmailTextPref","").commit();
        getFragmentManager().beginTransaction().add(R.id.fragment_container, new SettingsFragment()).commit();


        Bundle extras = getIntent().getExtras();
        String Name = "Username";
        if (extras != null)
            Name = extras.getString("Name");
        topName.setText(Name);
    }

    private void setListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findObjects() {
        backButton = (ImageButton) findViewById(R.id.collapse_ib);
        topName = (TextView) findViewById(R.id.top_username_tv);
    }

    public static class SettingsFragment extends PreferenceFragment {

        private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if (key.equals("EditNameTextPref")) {
                        String name = sharedPreferences.getString("EditNameTextPref", "def");
                        if (name.equals("")) {
                            Toast.makeText(getActivity(), "Name can't be empty!", Toast.LENGTH_SHORT).show();
                        } else {
                            updateProfile(name, 0);
                        }
                    } else if (key.equals("EditPasswordTextPref")) {
                        String password = sharedPreferences.getString("EditNameTextPref", "def");
                        if (password.equals("")) {
                            Toast.makeText(getActivity(), "Password can't be empty!", Toast.LENGTH_SHORT).show();
                        } else {
                            updateProfile(password, 1);
                        }
                        Log.d("TAG", "onSharedPreferenceChanged: " + sharedPreferences.getString("EditPasswordTextPref", "def"));
                    } else if (key.equals("EditEmailTextPref")) {
                        String email = sharedPreferences.getString("EditEmailTextPref", "def");
                        Log.d("TAG", "onSharedPreferenceChanged: "+email);
                        boolean emailIsValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); //checks if email is valid
                        if (email.equals("")){
                            Toast.makeText(getActivity(), "Email can't be empty!", Toast.LENGTH_SHORT).show();
                        }else if(!emailIsValid){
                            Toast.makeText(getActivity(), "Please Enter a valid Email format", Toast.LENGTH_SHORT).show();
                        }else {
                            updateProfile(email, 2);
                        }

                    }
                }
            };

        }

        private void updateProfile(String field, int sel) {
            EditProfileAsync editProfileAsync = new EditProfileAsync();
            editProfileAsync.execute();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        }
    }
}
