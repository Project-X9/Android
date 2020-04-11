package com.example.projectx;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;






/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class SignUpTest {
    @Rule
    public ActivityTestRule<SignUpActivity> menuActivityTestRule =
            new ActivityTestRule<>(SignUpActivity.class, true, true);

    @Test
    public void test1()
    {
        onView(withId(R.id.createUser_bt)).check(matches(withText("Create User")));
        closeSoftKeyboard();

        onView(withId(R.id.signUpEmail_et)).perform(replaceText("Ahmadgmail.com"));
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
        SignUpActivity activity = menuActivityTestRule.getActivity();
        onView(withText("Invalid field(s)")).inRoot(withDecorView(not(is(activity.getWindow()
                .getDecorView())))).check(matches(isDisplayed()));



    }


    @Test
    public void test2()
    {
        onView(withId(R.id.createUser_bt)).check(matches(withText("Create User")));
        closeSoftKeyboard();

        onView(withId(R.id.signUpEmail_et)).perform(replaceText("Ahmadgmail.com"));
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
        SignUpActivity activity = menuActivityTestRule.getActivity();
        onView(withText("Invalid field(s)")).inRoot(withDecorView(not(is(activity.getWindow()
                .getDecorView())))).check(matches(isDisplayed()));



    }

    @Test
    public void test3()
    {
        onView(withId(R.id.createUser_bt)).check(matches(withText("Create User")));
        closeSoftKeyboard();

        onView(withId(R.id.signUpEmail_et)).perform(replaceText("Ahmadgmail.com"));
        closeSoftKeyboard();

        onView(withId(R.id.signUpPassword_et)).perform(replaceText("testp asswo rd"));
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
        SignUpActivity activity = menuActivityTestRule.getActivity();
        onView(withText("Invalid field(s)")).inRoot(withDecorView(not(is(activity.getWindow()
                .getDecorView())))).check(matches(isDisplayed()));



    }

    @Test
    public void test4()
    {
        onView(withId(R.id.createUser_bt)).check(matches(withText("Create User")));
        closeSoftKeyboard();

        onView(withId(R.id.signUpEmail_et)).perform(replaceText("Ahmad@gmail.com"));
        closeSoftKeyboard();

        onView(withId(R.id.signUpPassword_et)).perform(replaceText("testpassword"));
        closeSoftKeyboard();


        onView(withId(R.id.signUpAge_et)).perform(replaceText("string"));
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
        SignUpActivity activity = menuActivityTestRule.getActivity();
        onView(withText("Invalid field(s)")).inRoot(withDecorView(not(is(activity.getWindow()
                .getDecorView())))).check(matches(isDisplayed()));
    }



}
