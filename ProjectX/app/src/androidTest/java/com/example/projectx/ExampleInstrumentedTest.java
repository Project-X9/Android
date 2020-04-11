package com.example.projectx;

import android.content.Context;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.projectx.authentication.SignUpActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<SignUpActivity> menuActivityTestRule =
            new ActivityTestRule<>(SignUpActivity.class, true, true);

        @Test
    public void TestView()
    {
        onView(withId(R.id.createUser_bt)).check(ViewAssertions.matches(withText("Create User")));
        closeSoftKeyboard();

        onView(withId(R.id.signUpEmail_et)).perform(replaceText("Ahmad@gmail.com"));
        closeSoftKeyboard();

        onView(withId(R.id.signUpPassword_et)).perform(replaceText("testpassword"));
        closeSoftKeyboard();


        onView(withId(R.id.signUpAge_et)).perform(replaceText("22"));
        closeSoftKeyboard();

        onView(withId(R.id.signUpName_et)).perform(replaceText("Ahmad Nader Adel"));
        closeSoftKeyboard();
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.createUser_bt))
                .perform(click());



    }



}
