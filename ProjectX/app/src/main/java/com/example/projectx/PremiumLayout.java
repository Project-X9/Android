package com.example.projectx;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class PremiumLayout extends Fragment {

    private ViewPager demoViewPager;
    public FreeVsPremiumAdapter adapter;
    private ImageButton settingsButton;

    public PremiumLayout() {
        // Required empty public constructor
    }

    private void setListeners() {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Profile.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premium_layout, container, false);
        setViewPager(view);
        findElements(view);
        setListeners();
        return view;
    }

    private void findElements(View view) {
        settingsButton = view.findViewById(R.id.settings_ib);
    }

    void setViewPager(View view) {
        demoViewPager = view.findViewById(R.id.demo_vp);
        adapter = new FreeVsPremiumAdapter(getChildFragmentManager(), 5);
        demoViewPager.setAdapter(adapter);
        demoViewPager.setPageMargin(40);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_tl);
        tabLayout.setupWithViewPager(demoViewPager);
    }

}