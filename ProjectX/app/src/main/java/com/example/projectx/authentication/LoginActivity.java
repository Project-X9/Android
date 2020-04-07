package com.example.projectx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.MainActivity;
import com.example.projectx.R;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    SharedPreferences service;
    final String SERVICE_FILE = "serviceChoice";
    JSONObject result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBt = (Button) findViewById(R.id.login_bt);

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                EditText emailEt = (EditText) findViewById(R.id.email_et);
                EditText passwordEt = (EditText) findViewById(R.id.password_et);
                boolean emailIsValid = android.util.Patterns.EMAIL_ADDRESS
                        .matcher(emailEt.getText()).matches(); //checks if email is valid

                if( emailIsValid && !stringify(emailEt).isEmpty() ) {  //checks if email is valid and not empty
                    if(!stringify(passwordEt).isEmpty()) {   //checks if password is not empty
                            LoginAsyncTask loginAgent = new LoginAsyncTask(false);
                            loginAgent.execute(stringify(emailEt), stringify(passwordEt));
                        }
                    else {
                        makeToast("Password can't be empty.");

                    }
                }

                else{
                    makeToast("Email is empty or invalid.");

                }

            }
        });

}
    private class LoginAsyncTask extends AsyncTask<String, Integer, JSONObject> {
        Boolean mockState;
        Boolean signedIn;

        public LoginAsyncTask(Boolean mockState) {
            this.mockState = mockState;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            SignInManager signInAgent = new SignInManager(getBaseContext());
            if (mockState) {
                result = signInAgent.login(strings[0], strings[1], true);
            } else {
                result = signInAgent.login(strings[0], strings[1], false);
            }

            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (mockState) {
                try {
                    if (result.getString("status").equals("success")) {
                        storeCredentials(CREDENTIALS_FILE, result.getString("id"));
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        finish();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    if (!result.getString("status").equals("failure")) {

                        storeCredentials(CREDENTIALS_FILE, result.getJSONObject("user").getString("_id"));
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * creates a toast message and displays it with duration Toast.LENGTH_SHORT
     * @param message, string you want to show the user in a toast;
     *
     *
     */
    public void makeToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Stores User credentials by opening a file and storing user email or Facebook ID, so user does
     * not login every time
     * @param credentialsFile  the name of the file to store user credentials in
     * @param email  the user's email or Facebook ID
     */
    public void storeCredentials(String credentialsFile, String email) {
        loginCredentials = getSharedPreferences(credentialsFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = loginCredentials.edit();
        editor.putString("id", email);
        editor.commit();
    }

    /**
     * extracts a string that contains the text of an EditView
     * @param view
     * @return text of an EditText formatted as a string
     */
    public String stringify(EditText view) {
        return view.getText().toString();
    }
}
