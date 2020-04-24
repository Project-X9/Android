package com.example.projectx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.projectx.MainActivity;
import com.example.projectx.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
     SharedPreferences loginCredentials;
     final String CREDENTIALS_FILE = "loginCreds";
     SharedPreferences lastActivity;
     final String ACTIVITY_FILE = "lastActivity";
     boolean userAlreadyExists = false;
     SharedPreferences service;
     final String SERVICE_FILE = "serviceChoice";
     String mockState;
     EditText nameEt;
     EditText ageEt;
     RadioGroup genderRg;
     EditText emailEt;
     EditText passwordEt ;
     String age;
     String gender;
     ProgressDialog progDialog;
     JSONObject result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final Button SIGN_UP_BT = (Button) findViewById(R.id.createUser_bt);
        SharedPreferences mocking = getSharedPreferences(SERVICE_FILE, MODE_PRIVATE);
        mockState = mocking.getString("Mocked", null);
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

                    if (checkNetwork()) {
                        //EmailAsyncTask emailAgent = new EmailAsyncTask();
                        //emailAgent.execute(stringify(emailEt));
                        progDialog = new ProgressDialog(SignUpActivity.this);
                        progDialog.show();
                        progDialog.setContentView(R.layout.progressbar);
                        SignUpAsyncTask signUpAgent = new SignUpAsyncTask();
                        signUpAgent.execute(stringify(nameEt),
                                stringify(emailEt),
                                age,
                                gender,
                                stringify(passwordEt));
                    }
                    else{
                        makeToast("There is no internet connection");
                    }


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
            correct = false;
            nameEt.setError("Name must only consist of alphabet characters.");
        }
        if (ageEt.getError() != null || stringify(ageEt).equals("")) {
            correct = false;
            ageEt.setError("You must enter your age.");
        }
        stringPattern = "[\\S]+$"; //checks the password does not contain any whitespace
        if (passwordEt.getError() != null || !stringify(passwordEt).matches(stringPattern)) {
            correct = false;
            passwordEt.setError("Password can't be empty or contain any spaces.");
        }
        if (stringify(ageEt).equals("")){
            ageEt.setError("Age Field can't be empty");
            return false;
        }
        if (Integer.parseInt(stringify(ageEt)) < 9){
            correct = false;
            makeToast("You must be 9 years or older to make an account on ProjectX");
        }
        if (Integer.parseInt(stringify(ageEt)) < 1) {
            correct = false;
            makeToast("Age field is invalid");
        }
        if (Integer.parseInt(stringify(ageEt)) > 150){
            correct = false;
            makeToast("You can't be older than 150 years.");
        }
        if (ageEt.getError() != null || stringify(ageEt).equals("")){ //checks age field is not empty
            correct = false;
            ageEt.setError("Age field can't be left empty");
        }
        return correct;

    }

    private class SignUpAsyncTask extends AsyncTask<String, String, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(String... strings) {
            if (mockState.equals("true")) {
                SignUpManager signUpManager = new SignUpManager(getBaseContext());
                result = signUpManager.signUp(true, strings[0],strings[1], strings[2],
                        strings[3], strings[4]);
            }
            else {
                SignUpManager signUpManager = new SignUpManager(getBaseContext());
                result = signUpManager.signUp(false, strings[0], strings[1], strings[2]
                        , strings[3], strings[4]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            try {
                //.
                if (progDialog.isShowing()){
                    progDialog.dismiss();
                }
                //Log.e("reached", "postexecute");
                Log.e("result of email", result.toString());
                if (result.getString("status").equals("success")) {
                    storeCredentials(CREDENTIALS_FILE, result.getJSONObject("data").
                            getJSONObject("user").getString("_id"));
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                }
                else if (result.getString("status").equals("email already in use")){
                    makeToast("The email you entered is already associated with another account");
                }
                else
                {
                    makeToast("Error receiving response from server.");
                }
            } catch (JSONException e) {
                makeToast(result.toString());
                Log.e("tag", result.toString());
                e.printStackTrace();
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
    public boolean checkNetwork(){
        ConnectivityManager conMan = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //assert conMan != null;
        NetworkInfo activeInfo = conMan.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected();
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
