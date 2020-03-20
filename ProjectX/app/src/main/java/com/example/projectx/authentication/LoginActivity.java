package com.example.projectx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectx.MainActivity;
import com.example.projectx.R;

public class LoginActivity extends AppCompatActivity {
    UsersDatabaseHelper myDb;
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDb = new UsersDatabaseHelper(this);
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
                        if(login(stringify(emailEt), stringify(passwordEt))) {   // retrieves account from database
                            makeToast("Successfully signed in.");
                            startActivity(new Intent(getBaseContext(), MainActivity.class)); //starts main activity
                            storeCredentials(CREDENTIALS_FILE, stringify(emailEt));
                            AuthenticationPage.authenticationPage.finish(); //destroys the authentication page so that the
                                                                            //back button does not return to it
                            finish();
                        }
                        else {
                            makeToast("Email or password incorrect.");

                        }
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

    /**
     *  This function checks if the user entered email and password that are present in the database
     * @param email
     * @param password
     * @return true if the email and password belong to an account in the database
     *         false if the email exists with a different password or the email doesn't exist at all
     */
    public boolean login(String email, String password) {

        String query = "SELECT * FROM " + myDb.getTableName() + " WHERE " + myDb.getEmail()
                + " = \"" + email + "\" AND " + myDb.getPassword() + " = \"" + password + "\";";

        SQLiteDatabase db = myDb.getWritableDatabase();
        Cursor queryResults = db.rawQuery(query, null);

        if (queryResults.getCount() == 0) {
            return false;
        }
        else {
                storeCredentials(CREDENTIALS_FILE, email);
            return true;
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
    protected void onResume () {
        super.onResume();
    }
}
