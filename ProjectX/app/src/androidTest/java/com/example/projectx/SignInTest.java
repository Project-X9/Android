package com.example.projectx;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.projectx.authentication.LoginActivity;
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
@RunWith(AndroidJUnit4.class)
public class SignInTest {
    @Rule
    public ActivityTestRule<LoginActivity> menuActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class, true, true);


    @Test
    public void test1(){
        onView(withId(R.id.email_et)).perform(replaceText("Ahmadgmail.com"));
        closeSoftKeyboard();

        onView(withId(R.id.password_et)).perform(replaceText("testpassword"));
        closeSoftKeyboard();
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login_bt))
                .perform(click());
        LoginActivity activity = menuActivityTestRule.getActivity();
        onView(withText("Email is empty or invalid.")).inRoot(withDecorView(not(is(activity.getWindow()
                .getDecorView())))).check(matches(isDisplayed()));

    }

    @Test
    public void test2(){
        onView(withId(R.id.email_et)).perform(replaceText("Ahmad@gmail.com"));
        closeSoftKeyboard();

        onView(withId(R.id.password_et)).perform(replaceText(""));
        closeSoftKeyboard();
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login_bt))
                .perform(click());
        LoginActivity activity = menuActivityTestRule.getActivity();
        onView(withText("Password can't be empty.")).inRoot(withDecorView(not(is(activity.getWindow()
                .getDecorView())))).check(matches(isDisplayed()));

    }


}
