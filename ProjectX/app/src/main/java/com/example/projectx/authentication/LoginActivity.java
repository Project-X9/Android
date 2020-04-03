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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectx.MainActivity;
import com.example.projectx.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    ArrayList<JSONObject> Users = new ArrayList<JSONObject>();
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
                        LoginAsyncTask loginAgent = new LoginAsyncTask();
                        loginAgent.execute(stringify(emailEt), stringify(passwordEt));
                    }
                    else {
                        makeToast("Password can't be empty.");

                    }
                }
                else{
                    makeToast("Email can't be empty.");

                }

            }
        });
    }
    private class LoginAsyncTask extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String[] doInBackground(String... strings) {
            login(strings[0], strings[1]);
           return strings;
        }

        @Override
        protected void onPostExecute(String[] strings){
            super.onPostExecute(strings);
            boolean found = false;
            try {
                for (int i = 0; i < Users.size(); i++) {

                    if (Users.get(i).get("Email").equals(strings[0]) && Users.get(i).get("Password")
                            .equals(strings[1])) {
                        found = true;
                        storeCredentials(CREDENTIALS_FILE, strings[0]);
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        finish();

                    }
                }
                if (found == false) {
                    makeToast("Email or password incorrect.");
                }

            }
            catch(JSONException e){
                Log.e("JSONException", "Failed to get JSONObject.");
                e.printStackTrace();
            }
        }
    }
    /**
     *  This function checks if the user entered email and password that are present in the database
     * @param email
     * @param password
     * @return true if the email and password belong to an account in the database
     *         false if the email exists with a different password or the email doesn't exist at all
     */
    public void login(final String email, final String password) {
        final String LOGIN_URL = "http://192.168.1.15:3000/Users";
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, LOGIN_URL, null, future, future);
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(request);

        try {
             JSONArray response = future.get();
            for(int i = 0; i<response.length(); i++){
                Users.add(response.getJSONObject(i));
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
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
        editor.putString("email", email);
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
