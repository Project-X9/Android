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

        final EditText EMAIL_ET = (EditText) findViewById(R.id.email_et);
        final EditText PASSWORD_ET = (EditText) findViewById(R.id.password_et);
        Button loginBt = (Button) findViewById(R.id.login_bt);

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean emailIsValid = android.util.Patterns.EMAIL_ADDRESS
                        .matcher(EMAIL_ET.getText()).matches();

                if( emailIsValid && !EMAIL_ET.getText().toString().isEmpty()){
                    if(!PASSWORD_ET.getText().toString().isEmpty()) {
                        if(login(EMAIL_ET.getText().toString(), PASSWORD_ET.getText().toString())) {
                            String successMessage = "Successfully signed in.";
                            Toast.makeText(getBaseContext(), successMessage, Toast.LENGTH_SHORT)
                                    .show();
                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                            finish();
                        }
                        else {
                            String errorMessage = "Email or password incorrect.";
                            Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    else {
                        String errorMessage = "Password can't be empty.";
                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                else{
                    String errorMessage = "Password can't be empty.";
                    Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
    }
    public boolean login(String email, String password) {

        String query = "SELECT * FROM " + myDb.getTableName() + " WHERE " + myDb.getEmail()
                + " = \"" + email + "\" AND " + myDb.getPassword() + " = \"" + password + "\";";

        SQLiteDatabase db = myDb.getWritableDatabase();
        Cursor queryResults = db.rawQuery(query, null);

        if (queryResults.getCount() == 0) {
            return false;
        }
        else {
            loginCredentials = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = loginCredentials.edit();
            editor.putString("email", email);
            editor.commit();
            return true;
        }
    }
}
