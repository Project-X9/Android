package com.example.projectx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectx.UserActivity.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfilesActivity extends AppCompatActivity {
    //Views
    RecyclerView profilesRecyclerView;

    private ThreeDataItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ThreeDataItem> users;

    String[] userIds;

    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        initialize();
        findViews();
        sendRequest();
    }

    private void initialize() {
        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(bundle.getString("Title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userIds = bundle.getStringArray("UserIds");
    }

    private void findViews() {
        profilesRecyclerView = findViewById(R.id.profiles_rv);
        users = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ThreeDataItemAdapter(users);
        profilesRecyclerView.setLayoutManager(mLayoutManager);
        profilesRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ThreeDataItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int Position) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }

    private void sendRequest() {
        loginCredentials = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
        for (int i = 0; i < userIds.length; i++) {
            String url = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/users/";
            url = url + userIds[i];
            JsonObjectRequest getUser = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject User = response.getJSONObject("data").getJSONObject("user");
                        updateUI(User);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    makeToast("Error downloading data, make sure you're connected to the internet.");
                }
            }) {
                @Override
                public HashMap<String, String> getHeaders() {
                    String token = loginCredentials.getString("token", null);
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", token);
                    return params;
                }
            };
            RequestQueue getUserQueue = Volley.newRequestQueue(this);
            getUserQueue.add(getUser);
        }
    }

    private void updateUI(JSONObject user) {
        try {
            String name = user.getString("name");
            int followers = user.getJSONArray("followers").length();
            users.add(new ThreeDataItem(R.drawable.profile_image_placeholder, name, Integer.toString(followers)));
            mAdapter = new ThreeDataItemAdapter(users);
            profilesRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}