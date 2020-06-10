package com.example.projectx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class PremiumLayout extends Fragment implements PremiumDialog.PremiumDialogListener {
    //Constants
    private final String PREMIUM_USER_OFFER = "Hope you're enjoying Premium, read more about the features below.";
    private final String FREE_USER_OFFER = "Join Premium Now, 3 months for EGP 49.99";
    private final String PREVIOUSLY_PREMIUM_USER_OFFER = "Come back to Premium, 3 months for EGP 49.99";

    //Views
    private ViewPager demoViewPager;
    public FreeVsPremiumAdapter adapter;
    private ImageButton settingsButton;
    private TextView premiumOffer, premiumStatus;
    private Button premiumButton1, premiumButton2, premiumButton3;

    //Variables
    private boolean isPremium;
    private boolean isPreviouslyPremium;

    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";

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
        View.OnClickListener premiumButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                premiumButtonPressed();
            }
        };
        premiumButton1.setOnClickListener(premiumButtonListener);
        premiumButton2.setOnClickListener(premiumButtonListener);
        premiumButton3.setOnClickListener(premiumButtonListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premium_layout, container, false);
        setViewPager(view);
        findElements(view);
        getPremiumStatus();
        initializeUI();
        setListeners();
        return view;
    }

    private void initializeUI() {
        if (isPremium) {
            premiumUI();
        } else {
            if (isPreviouslyPremium) {
                previouslyPremiumUI();
            } else {
                freeUI();
            }
        }
    }

    private void getPremiumStatus() {
        loginCredentials = getActivity().getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
        try {
            JSONObject user = new JSONObject(loginCredentials.
                    getString("UserObject", null))
                    .getJSONObject("data")
                    .getJSONObject("user");
            isPremium = user.getBoolean("premium");
            isPreviouslyPremium = user.getBoolean("previouslyPremium");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findElements(View view) {
        settingsButton = view.findViewById(R.id.settings_ib);
        premiumButton1 = view.findViewById(R.id.get_premium_button1_bt);
        premiumButton2 = view.findViewById(R.id.get_premium_button2_bt);
        premiumButton3 = view.findViewById(R.id.get_premium_button3_bt);
        premiumOffer = view.findViewById(R.id.premium_offer_tv);
        premiumStatus = view.findViewById(R.id.premium_status_tv);
    }

    void setViewPager(View view) {
        demoViewPager = view.findViewById(R.id.demo_vp);
        adapter = new FreeVsPremiumAdapter(getChildFragmentManager(), 5);
        demoViewPager.setAdapter(adapter);
        demoViewPager.setPageMargin(40);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_tl);
        tabLayout.setupWithViewPager(demoViewPager);
    }

    public void premiumButtonPressed() {
        openDialog();
    }

    private void openDialog() {
        String message;
        if (isPremium) {
            message = "Are you sure you want to cancel Premium subscription?";
        } else {
            message = "Are you sure you want to buy premium?";
        }
        PremiumDialog dialog = new PremiumDialog(message);
        dialog.show(getChildFragmentManager(), "premium dialog");
    }

    @Override
    public void OnYesClicked() {
        //TODO: call api when backend make it and put the following lines in OnResponseListener
        isPremium = !isPremium;
        initializeUI();
    }

    private void premiumUI() {
        premiumOffer.setText(PREMIUM_USER_OFFER);
        premiumStatus.setText("Spotify\nPremium");
        premiumButton1.setText("Cancel Premium");
        premiumButton2.setText("Cancel Premium");
        premiumButton3.setText("Cancel Premium");
    }

    private void previouslyPremiumUI() {
        premiumOffer.setText(PREVIOUSLY_PREMIUM_USER_OFFER);
        premiumStatus.setText("Spotify\nFree");
        premiumButton1.setText("Get Premium");
        premiumButton2.setText("Get Premium");
        premiumButton3.setText("Get Premium");
    }

    private void freeUI() {
        premiumOffer.setText(FREE_USER_OFFER);
        premiumStatus.setText("Spotify\nFree");
        premiumButton1.setText("Get Premium");
        premiumButton2.setText("Get Premium");
        premiumButton3.setText("Get Premium");
    }
}