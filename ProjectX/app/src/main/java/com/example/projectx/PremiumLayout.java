package com.example.projectx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class PremiumLayout extends Fragment implements PremiumDialog.PremiumDialogListener {
    //Constants
    private final String PREMIUM_USER_OFFER = "Hope you're enjoying Premium, read more about the features below.";
    private final String FREE_USER_OFFER = "Join Premium Now, 3 months for EGP 49.99";
    private final String PREVIOUSLY_PREMIUM_USER_OFFER = "Come back to Premium, 3 months for EGP 49.99";
    final String CREDENTIALS_FILE = "loginCreds";

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
    private boolean loading = false;


    public PremiumLayout() {
        // Required empty public constructor
    }

    /**
     * Sets listeners for UI elements
     */
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
        updateUI();
        setListeners();
        return view;
    }

    /**
     * Updates UI based on isPremium and isPreviouslyPremium
     */
    private void updateUI() {
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

    /**
     * Gets isPremium and isPreviouslyPremium from sharedPreferences
     */
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

    /**
     * findViews
     *
     * @param view Fragment View
     */
    private void findElements(View view) {
        settingsButton = view.findViewById(R.id.settings_ib);
        premiumButton1 = view.findViewById(R.id.get_premium_button1_bt);
        premiumButton2 = view.findViewById(R.id.get_premium_button2_bt);
        premiumButton3 = view.findViewById(R.id.get_premium_button3_bt);
        premiumOffer = view.findViewById(R.id.premium_offer_tv);
        premiumStatus = view.findViewById(R.id.premium_status_tv);
    }

    /**
     * Initializes demo ViewPager
     *
     * @param view
     */
    void setViewPager(View view) {
        demoViewPager = view.findViewById(R.id.demo_vp);
        adapter = new FreeVsPremiumAdapter(getChildFragmentManager(), 5);
        demoViewPager.setAdapter(adapter);
        demoViewPager.setPageMargin(40);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_tl);
        tabLayout.setupWithViewPager(demoViewPager);
    }

    /**
     * Handles when the premium button is pressed
     */
    public void premiumButtonPressed() {
        if (loading) {
            Toast.makeText(getActivity(), "Loading, please wait", Toast.LENGTH_SHORT).show();
            return;
        }
        openDialog();
    }

    /**
     * Opens premium dialog
     */
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

    /**
     * Handles when the user presses YES on the dialog
     */
    @Override
    public void OnYesClicked() {
        sendRequest();
    }

    /**
     * Sends request to the server to change premium status and updates UI on response
     */
    private void sendRequest() {
        loginCredentials = getActivity().getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
        final String userId = loginCredentials.getString("id", null);
        String url = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/users/";
        url = url + userId;
        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.PATCH, url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                isPremium = !isPremium;
                updateUI();
                loading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error.toString());
                ;
                Toast.makeText(getActivity(), "Error, please make sure you're connected to the internet", Toast.LENGTH_SHORT).show();
                loading = false;
            }
        }) {
            @Override
            public byte[] getBody() {
                String body = "{\"" + "premium" + "\":" + !isPremium + "}";
                return body.getBytes();
            }
        };
        RequestQueue getUserQueue = Volley.newRequestQueue(getActivity());
        getUserQueue.add(updateRequest);
        loading = true;
    }

    /**
     * Changes to premium user UI
     */
    private void premiumUI() {
        premiumOffer.setText(PREMIUM_USER_OFFER);
        premiumStatus.setText("Spotify\nPremium");
        premiumButton1.setText("Cancel Premium");
        premiumButton2.setText("Cancel Premium");
        premiumButton3.setText("Cancel Premium");
    }

    /**
     * Changes to previously premium user UI
     */
    private void previouslyPremiumUI() {
        premiumOffer.setText(PREVIOUSLY_PREMIUM_USER_OFFER);
        premiumStatus.setText("Spotify\nFree");
        premiumButton1.setText("Get Premium");
        premiumButton2.setText("Get Premium");
        premiumButton3.setText("Get Premium");
    }

    /**
     * Changes to free user UI
     */
    private void freeUI() {
        premiumOffer.setText(FREE_USER_OFFER);
        premiumStatus.setText("Spotify\nFree");
        premiumButton1.setText("Get Premium");
        premiumButton2.setText("Get Premium");
        premiumButton3.setText("Get Premium");
    }
}