package com.example.projectx;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.projectx.authentication.SignUpActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void validating(){
        SignUpActivity act = Robolectric.buildActivity(SignUpActivity.class)
                .create()
                .resume()
                .get();
        if (act == null) {
            System.out.println("yaba a7la null 3alek");
        }
        else{


        EditText nameEt = (EditText) act.findViewById(R.id.signUpName_et);
        nameEt.setText("Ahmad");
        EditText ageEt = (EditText) act.findViewById(R.id.signUpAge_et);
        ageEt.setText("23");
        EditText emailEt = (EditText) act.findViewById(R.id.signUpEmail_et);
        emailEt.setText("ahmadnader98@gmail.com");
        EditText passwordEt = (EditText) act.findViewById(R.id.signUpPassword_et);
        passwordEt.setText("testPassword");
        RadioGroup genderRg = (RadioGroup) act.findViewById(R.id.signUpGender_rg);
        //String age = stringify(ageEt);
        assertTrue(act.validateSignUpForm());
    }}
}