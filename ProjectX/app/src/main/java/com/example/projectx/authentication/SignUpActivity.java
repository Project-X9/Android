package com.example.projectx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.MainActivity;
import com.example.projectx.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SignUpActivity extends AppCompatActivity {
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    SharedPreferences lastActivity;
    final String ACTIVITY_FILE = "lastActivity";
     boolean userAlreadyExists = false;
     EditText nameEt;
     EditText ageEt;
     RadioGroup genderRg;
     EditText emailEt;
     EditText passwordEt ;
     String age;
     String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.e("signup oncreate", "reached oncreate in sign up");
        final Button SIGN_UP_BT = (Button) findViewById(R.id.createUser_bt);
        SIGN_UP_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 nameEt = (EditText) findViewById(R.id.signUpName_et);
                 ageEt = (EditText) findViewById(R.id.signUpAge_et);
                 emailEt = (EditText) findViewById(R.id.signUpEmail_et);
                 passwordEt = (EditText) findViewById(R.id.signUpPassword_et);
                 RadioGroup genderRg = (RadioGroup) findViewById(R.id.signUpGender_rg);
                 age = stringify(ageEt);
                int id = genderRg.getCheckedRadioButtonId();

                if (id == R.id.signUpMale_rb) {
                    gender = "Male";
                }
                else {
                    gender = "Female";
                }

                if (!validateSignUpForm()) {
                    makeToast("Invalid field(s)");
                }
                else {
                    EmailAsyncTask emailAgent = new EmailAsyncTask();
                    emailAgent.execute(stringify(emailEt));
                }
        }
    });
    }

    /**
     * validates all the Edit Texts in the Sign up
     * @return true if all the EditTexts have no errors and abide by the checks,
     *         false if at least one EditText is errored
     */
    public boolean validateSignUpForm() {

        boolean correct = true;
        //checks if the email is valid
        if (emailEt.getError() != null ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEt.getText()).matches()){
            correct = false;
            emailEt.setError("This Email is not valid.");
        }

        String stringPattern = "[a-zA-Z\\s]+$"; //regex pattern to ensure name contains only letters
        if(nameEt.getError() != null || !stringify(nameEt).matches(stringPattern))
        {
            nameEt.setError("Name must only consist of alphabet characters.");
        }
        if (ageEt.getError() != null) {
            correct = false;
        }
        stringPattern = "[\\S]+$"; //checks the password does not contain any whitespace
        if (passwordEt.getError() != null || !stringify(passwordEt).matches(stringPattern)) {
            correct = false;
            passwordEt.setError("Password can't be empty or contain any spaces.");
        }
        if (Integer.parseInt(stringify(ageEt)) < 9){
            correct = false;
            makeToast("You must be 9 years or older to make an account on ProjectX");
        }
        if (Integer.parseInt(stringify(ageEt)) < 1) {
            makeToast("Age field is invalid");
        }
        if (ageEt.getError() != null || stringify(ageEt).equals("")){ //checks age field is not empty
            correct = false;
            ageEt.setError("Age field can't be left empty");
        }
        return correct;

    }
    /**
     * checks if the email entered is already associated with an account in the database, if the
     * email was not found, it creates a new account with the parameters in addition to the date of
     * the day the account was created.
     * @param name Name of the profile the user enters
     * @param email email user enters
     * @param age age of the user
     * @param gender gender of user
     * @param password password user enters
     */
    public void signUp(String name, String email, String age, String gender ,String password) {
        final String SIGNUP_URL = "http://192.168.1.15:3000/Users";
        userAlreadyExists = false;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        Log.e("after return", "this shouldn't be here");
        Map<String, String> userInfo = new HashMap();
        userInfo.put("Name", name);
        userInfo.put("Email", email);
        userInfo.put("Age", age);
        userInfo.put("Gender", gender);
        userInfo.put("Password", password);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        userInfo.put("Date_Added", formatter.format(date));
        JSONObject parameters = new JSONObject(userInfo);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SIGNUP_URL,
                parameters, future, future);
        RequestQueue singUpRq = Volley.newRequestQueue(this);
        singUpRq.add(request);

        try {
            JSONObject response = future.get();
            Log.e("post result", response.toString());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    private class SignUpAsyncTask extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String[] doInBackground(String... strings) {
            signUp(strings[0],strings[1], strings[2], strings[3], strings[4]);
            return strings;
        }

        @Override
        protected void onPostExecute(String[] strings){
            super.onPostExecute(strings);
            if (!userAlreadyExists) {
                Log.e("onpost", "this also shouldn't be here");

                storeCredentials(CREDENTIALS_FILE, stringify(emailEt));
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                AuthenticationPage.authenticationPage.finish();
                finish();
            }

        }
    }

    private class EmailAsyncTask extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String[] doInBackground(String... strings) {
            userAlreadyExists = false;
            final String SIGNUP_URL = "http://192.168.1.15:3000/Users";
            RequestFuture<JSONArray> futureArr = RequestFuture.newFuture();
            JsonArrayRequest requestArr = new JsonArrayRequest(Request.Method.GET, SIGNUP_URL,
                    null, futureArr, futureArr);

            RequestQueue lookForEmailRq = Volley.newRequestQueue(getBaseContext());
            lookForEmailRq.add(requestArr);
            try {
                JSONArray response = futureArr.get();
                for(int i = 0; i<response.length(); i++){
                    if(response.getJSONObject(i).getString("Email").equals(strings[0])) {
                        userAlreadyExists = true;
                        Log.e("User", "found in database");
                    }
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
            return strings;
        }

        @Override
        protected void onPostExecute(String[] strings){
            super.onPostExecute(strings);
            if(!userAlreadyExists) {
                SignUpAsyncTask signUpAgent = new SignUpAsyncTask();
                signUpAgent.execute(stringify(nameEt),
                        stringify(emailEt),
                        age,
                        gender,
                        stringify(passwordEt));
            }
            else {
                makeToast("This email is associated with another account.");
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
