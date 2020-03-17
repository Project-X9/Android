package com.example.projectx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectx.MainActivity;
import com.example.projectx.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {
    UsersDatabaseHelper usersdb;
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        usersdb = new UsersDatabaseHelper(this);
        final Button SIGN_UP_BT = (Button) findViewById(R.id.createUser_bt);
        SIGN_UP_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameEt = (EditText) findViewById(R.id.signUpName_et);
                EditText ageEt = (EditText) findViewById(R.id.signUpAge_et);
                EditText emailEt = (EditText) findViewById(R.id.signUpEmail_et);
                EditText passwordEt = (EditText) findViewById(R.id.signUpPassword_et);
                RadioGroup genderRg = (RadioGroup) findViewById(R.id.signUpGender_rg);

                int id = genderRg.getCheckedRadioButtonId();
                String gender;
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
                    int age = Integer.parseInt(ageEt.getText().toString());
                    signUp( stringify(nameEt),
                            stringify(emailEt),
                            age,
                            gender,
                            stringify(passwordEt));
                    storeCredentials(CREDENTIALS_FILE, stringify(emailEt));
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    AuthenticationPage.authenticationPage.finish();
                    finish();
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
        EditText nameEt = (EditText) findViewById(R.id.signUpName_et);
        EditText ageEt = (EditText) findViewById(R.id.signUpAge_et);
        RadioGroup genderRg = (RadioGroup) findViewById(R.id.signUpGender_rg);
        EditText emailEt = (EditText) findViewById(R.id.signUpEmail_et);
        EditText passwordEt = (EditText) findViewById(R.id.signUpPassword_et);
        boolean errors = false;
        //checks if the email is valid
        if (emailEt.getError() != null ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEt.getText()).matches()){
            errors = true;
            emailEt.setError("This Email is not valid.");
        }

        String stringPattern = "[a-zA-Z\\s]+$"; //regex pattern to ensure name contains only letters
        if(nameEt.getError() != null || !stringify(nameEt).matches(stringPattern))
        {
            nameEt.setError("Name must only consist of alphabet characters.");
        }
        if (ageEt.getError() != null) {
            errors = true;
        }
        stringPattern = "[\\S]+$"; //checks the password does not contain any whitespace
        if (passwordEt.getError() != null || !stringify(passwordEt).matches(stringPattern)) {
            errors = true;
            passwordEt.setError("Password can't be empty or contain any spaces.");
        }
        if (Integer.parseInt(stringify(ageEt)) < 9){
            errors = true;
            makeToast("You must be 9 years or older to make an account on ProjectX");
        }
        if (Integer.parseInt(stringify(ageEt)) < 1) {
            makeToast("Age field is invalid");
        }
        if (ageEt.getError() != null || stringify(ageEt).equals("")){ //checks age field is not empty
            errors = true;
            ageEt.setError("Age field can't be left empty");
        }
        return errors;

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
    public void signUp(String name, String email, int age, String gender ,String password) {

        SQLiteDatabase db = usersdb.getWritableDatabase();
        String query = "SELECT * FROM " + usersdb.getTableName() + " WHERE " +
                usersdb.getEmail() + " = \"" + email + "\";";
        Cursor cursor = db.rawQuery( query, null);

        if (cursor.getCount() == 0) { //checks that there is no account associated with the email
            ContentValues content = new ContentValues();
            content.put(usersdb.getName(), name);
            content.put(usersdb.getAge(), age);
            content.put(usersdb.getGender(), gender);
            content.put(usersdb.getDateAdded(),"datetime()");
            content.put(usersdb.getEmail(), email);
            content.put(usersdb.getPassword(), password);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            content.put(usersdb.getDateAdded(), formatter.format(date));
            db.insert(usersdb.getTableName(), null, content);

            makeToast("Sign up successful");
        }
        else {
            makeToast("We already have an account associated with that email");
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
