package com.example.projectx.playlist;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.projectx.MainActivity;
import com.example.projectx.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PlayListFullTest {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, true);


    @Test
    public void ViewPlayList1(){
        onView(withId(R.id.newReleases))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.three_dots_ibt))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.collapse_ib))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.three_dots_ibt))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ViewPlayList2(){
        onView(withId(R.id.likedTracks))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.three_dots_ibt))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.collapse_ib))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.three_dots_ibt))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ViewPlayList3(){
        onView(withId(R.id.popular))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.three_dots_ibt))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.collapse_ib))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.three_dots_ibt))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ViewPlayList4(){
        onView(withId(R.id.recommended))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.three_dots_ibt))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.collapse_ib))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.three_dots_ibt))
                .perform(click());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}