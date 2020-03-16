package com.example.projectx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectx.R;

public class LoginActivity extends AppCompatActivity {
    UsersDatabaseHelper myDb;
    SharedPreferences loginCredentials;
    final String credentialsFile = "loginCreds";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDb = new UsersDatabaseHelper(this);
        final EditText emailEt = (EditText) findViewById(R.id.emailet);
        final EditText passwordEt = (EditText) findViewById(R.id.passwordet);
        Button loginBt = (Button) findViewById(R.id.loginbt);
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.e("button", "onclickListener activated.");
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailEt.getText()).matches() &&
                        !emailEt.getText().toString().isEmpty()){
                    Log.e("passed email test", "no errors in email");
                    if(!passwordEt.getText().toString().isEmpty()) {
                        Log.e("passed password test", "no errors in password");
                        if(login(emailEt.getText().toString(), passwordEt.getText().toString())) {
                            Log.e("reached login", "reached login");
                            Toast.makeText(getBaseContext(), "Successfully signed in.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Email or password incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Password can't be empty.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(), "Email is invalid.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public boolean login(String email, String password) {
        String query = "SELECT * FROM " + myDb.getTableName() + " WHERE " + myDb.getEmail() + " = \""
                + email + "\" AND " + myDb.getPassword() + " = \"" + password + "\";";
        SQLiteDatabase db = myDb.getWritableDatabase();
        Cursor queryResults = db.rawQuery(query, null);
        if (queryResults.getCount() == 0) {
            return false;
        }
        else{
            loginCredentials = getSharedPreferences(credentialsFile, MODE_PRIVATE);
            SharedPreferences.Editor editor = loginCredentials.edit();
            editor.putString("email", email);
            editor.commit();
            return true;
        }
    }
}
