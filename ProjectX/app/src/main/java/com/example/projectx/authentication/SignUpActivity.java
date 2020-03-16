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
import android.widget.Toast;

import com.example.projectx.MainActivity;
import com.example.projectx.R;

public class SignUpActivity extends AppCompatActivity {
    UsersDatabaseHelper usersdb;
    SharedPreferences loginCredentials;
    final String credentialsFile = "loginCreds";
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
                RadioGroup genderRg = (RadioGroup) findViewById(R.id.signUpGender_rg);

                int id = genderRg.getCheckedRadioButtonId();
                String gender;
                if (id == R.id.signUpMale_rb) {
                    gender = "Male";
                }
                else {
                    gender = "Female";
                }
                EditText emailEt = (EditText) findViewById(R.id.signUpEmail_et);
                EditText passwordEt = (EditText) findViewById(R.id.signUpPassword_et);
                boolean errors = false;
                if (emailEt.getError() != null ||
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEt.getText()).matches()){
                    errors = true;
                    emailEt.setError("This Email is not valid.");
                }

                String stringPattern = "[a-zA-Z\\s]+$";
                if(nameEt.getError() != null || !nameEt.getText().toString().matches(stringPattern))
                {
                    nameEt.setError("Name must only consist of alphabet characters.");
                }
                if (ageEt.getError() != null) {
                    errors = true;
                }
                stringPattern = "[\\S]+$";
                if (passwordEt.getError() != null ||
                        !passwordEt.getText().toString().matches(stringPattern)) {
                    errors = true;
                    passwordEt.setError("Password can't be empty or contain any spaces.");
                }
                if (ageEt.getError() != null || ageEt.getText().toString().equals("")){
                    errors = true;
                    ageEt.setError("Age field can't be left empty");
                }
                if (errors) {
                    Toast.makeText(getBaseContext(), "Invalid field(s)", Toast.LENGTH_SHORT).show();
                    //Log.e("tag", "errors found");
                }
                else {
                    int age = Integer.parseInt(ageEt.getText().toString());
                    //Log.e("tag", "errors not found");
                    signup( nameEt.getText().toString(),
                            emailEt.getText().toString(),
                            age,
                            gender,
                            passwordEt.getText().toString());
                }
        }
    });
    }


    public void signup(String name, String email, int age, String gender ,String password) {
        SQLiteDatabase db = usersdb.getWritableDatabase();
        if (db !=  null){ Log.e("tah","db opened");}
        String quey = "SELECT * FROM " + usersdb.getTableName() + " WHERE " +
                usersdb.getEmail() +
                " = \"" + email + "\";";
        Log.e("query", quey);
        Cursor cursor = db.rawQuery( quey, null);
        if (cursor.getCount() == 0) {

            ContentValues content = new ContentValues();
            content.put(usersdb.getName(), name);
            content.put(usersdb.getAge(), age);
            content.put(usersdb.getGender(), gender);
            content.put(usersdb.getDateAdded(),"datetime()");
            content.put(usersdb.getEmail(), email);
            content.put(usersdb.getPassword(), password);
            db.insert(usersdb.getTableName(), null, content);
            Toast.makeText(getBaseContext(), "Sign up successful", Toast.LENGTH_SHORT).show();
            loginCredentials = getSharedPreferences(credentialsFile, MODE_PRIVATE);
            SharedPreferences.Editor editor = loginCredentials.edit();
            editor.putString("email", email);
            editor.commit();
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();

        }
        else {
            String error = "We already have an account associated with that email";
            Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
        }
    }
}
