package com.example.projectx;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FreeVsPremiumAdapter extends FragmentPagerAdapter {


    public FreeVsPremiumAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        FreeVsPremium freeVsPremium = new FreeVsPremium();
        Bundle bundle = new Bundle();

        String Free = "", Premium = "";
        if (position == 0) {
            Free = "Ad breaks";
            Premium = "Ad-free music";
        } else if (position == 1) {
            Free = "Play in shuffle";
            Premium = "Play any song";
        } else if (position == 2) {
            Free = "6 skips per hour";
            Premium = "Unlimited skips";
        } else if (position == 3) {
            Free = "Streaming only";
            Premium = "Offline listening";
        } else if (position == 4) {
            Free = "Basic audio quality";
            Premium = "High audio quality";
        }
        bundle.putString("Free", Free);
        bundle.putString("Premium", Premium);
        freeVsPremium.setArguments(bundle);

        return freeVsPremium;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
