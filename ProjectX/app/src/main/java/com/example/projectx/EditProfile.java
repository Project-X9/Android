package com.example.projectx;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {

    private ImageButton backButton;
    private TextView topName;
    private String Name, Email;
    private RequestQueue requestQueue;
    final String CREDENTIALS_FILE = "loginCreds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findObjects();
        initializeElements();
        setListeners();
    }

    private void initializeElements() {
        SettingsFragment.loginCredentials = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Name = extras.getString("Name");
            Email = extras.getString("Email");
        }
        topName.setText(Name);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        preferences.edit().putBoolean("NotificationsCheckbox", true).commit();
        preferences.edit().putString("EditNameTextPref", Name).commit();
        preferences.edit().putString("EditPasswordTextPref", "").commit();
        preferences.edit().putString("EditEmailTextPref", Email).commit();
//        preferences.edit().putString("IpAddressTextPref", Email).commit();
        getFragmentManager().beginTransaction().add(R.id.fragment_container, new SettingsFragment()).commit();
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
        backButton = findViewById(R.id.collapse_ib);
        topName = findViewById(R.id.top_username_tv);
    }


    public static class SettingsFragment extends PreferenceFragment {

        static SharedPreferences loginCredentials;

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
//                            Toast.makeText(getActivity(), , Toast.LENGTH_SHORT).show();
                            showToast("Name can't be empty!");
                        } else {
                            updateProfile(name, "name");
                        }
                    } else if (key.equals("EditPasswordTextPref")) {
                        String password = sharedPreferences.getString("EditPasswordTextPref", "def");
                        if (password.equals("")) {
//                            Toast.makeText(getActivity(), "Password can't be empty!", Toast.LENGTH_SHORT).show();
                            showToast("Password can't be empty!");
                        } else {
                            updateProfile(password, "password");
                        }
                        Log.d("TAG", "onSharedPreferenceChanged: " + sharedPreferences.getString("EditPasswordTextPref", "def"));
                    } else if (key.equals("EditEmailTextPref")) {
                        String email = sharedPreferences.getString("EditEmailTextPref", "def");
                        Log.d("TAG", "onSharedPreferenceChanged: " + email);
                        boolean emailIsValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); //checks if email is valid
                        if (email.equals("")) {
//                            Toast.makeText(getActivity(), "Email can't be empty!", Toast.LENGTH_SHORT).show();
                            showToast("Email can't be empty!");
                        } else if (!emailIsValid) {
//                            Toast.makeText(getActivity(), "Please Enter a valid Email format", Toast.LENGTH_SHORT).show();
                            showToast("Please Enter a valid Email format");
                        } else {
                            updateProfile(email, "email");
                        }

                    }
                }
            };

        }

        void showToast(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        private void updateProfile(final String value, final String key) {
            String userId = loginCredentials.getString("id", null);
            String url = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/users/";
            url = url + userId;
            JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.PATCH, url, null
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("status").equals("success"))
                            showToast("Updated Successfully");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showToast("Failed to Update " + error.toString());
                }
            }) {
                @Override
                public byte[] getBody() {
                    String body = "{\"" + key + "\":\"" + value + "\"}";
                    return body.getBytes();
                }
            };
            RequestQueue getUserQueue = Volley.newRequestQueue(getActivity());
            getUserQueue.add(updateRequest);
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
