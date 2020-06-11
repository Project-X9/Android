package com.example.projectx.UserActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectx.R;
import com.example.projectx.ThreeDataItem;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserActivity extends AppCompatActivity {
    private final String MOCK_URL = "http://www.mocky.io/v2/5edb7b303200009f7a5d2768";
    private Context context;
    private Button follow;
    private ImageButton backButton;
    TextView topUsername, middleUsername, playlistCount, followersCount, followingCount, activityStatus;
    private RecyclerView playlistsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String imageURL;
    ImageView mUserImage;
    private LinearLayout playlistLayout, followersLayout, followingLayout, headerLayout;
    private AppBarLayout appBarLayout;
    private ConstraintLayout backgroundGradient;
    static ArrayList<UserData> onlineData;
    ArrayList<ThreeDataItem> onlinePlaylistsList;

    private String name, email;
    private int followers, following, playlistsCount;
    String[] followersIds;
    String[] followingIds;
    String[] playlistIds;
    private boolean loading;

    final String CREDENTIALS_FILE = "loginCreds";
    SharedPreferences loginCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activity);

        follow = findViewById(R.id.follow_button_bt);
        playlistCount = findViewById(R.id.playlists_count_tv);
        followersCount = findViewById(R.id.followers_count_tv);
        followingCount = findViewById(R.id.following_count_tv);
        activityStatus = findViewById(R.id.activity_status_tv);
        middleUsername = findViewById(R.id.middle_username_tv);
        topUsername = findViewById(R.id.top_username_tv);
        playlistLayout = findViewById(R.id.playlists_layout_ll);
        followersLayout = findViewById(R.id.followers_layout_ll);
        followingLayout = findViewById(R.id.following_layout_ll);
        appBarLayout = findViewById(R.id.profile_appbarlayout);
        headerLayout = findViewById(R.id.header_content_ll);
        playlistsRecyclerView = findViewById(R.id.playlists_list_rv);
        backgroundGradient = findViewById(R.id.background_gradient_cl);
        mUserImage = findViewById(R.id.profile_image_iv);
        backButton = findViewById(R.id.collapse_ib);
        context = getApplicationContext();
        mLayoutManager = new LinearLayoutManager(this);
        setOnClickListeners();
//        FetchUserData fetchUserData = new FetchUserData(this);
//        fetchUserData.setURL(MOCK_URL);
//        fetchUserData.execute();
        sendRequest();
    }

    private void setOnClickListeners() {

        followersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: intent to open followers page of current user
            }
        });

        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: intent to open following page of current user
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float x = headerLayout.getHeight();
                topUsername.setAlpha((-verticalOffset / x));
                int[] y = new int[2];
                playlistCount.getLocationInWindow(y);
                ViewGroup.LayoutParams params = backgroundGradient.getLayoutParams();
                params.height = y[1];
                backgroundGradient.setLayoutParams(params);

                backgroundGradient.setY(verticalOffset);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Send get request to server to get user data
     */
    private void sendRequest() {
        String userId = getIntent().getStringExtra("UserId");
        Log.e("userId", userId);
        String url = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/users/";
        url = url + userId;
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
                loading = true;
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
        loading = true;
        makeToast("Loading data please wait.");
    }

    void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Update UI elements from the passed JSONObject
     *
     * @param user JSONObject to update the UI from
     */
    private void updateUI(JSONObject user) {
        try {
            name = user.getString("name");

            JSONArray followersJsonArray = user.getJSONArray("followers");
            followers = followersJsonArray.length();
            followersIds = new String[followers];
            for (int i = 0; i < followers; i++) {
                followersIds[i] = (String) followersJsonArray.get(i);
            }

            JSONArray followingJsonArray = user.getJSONArray("following");
            following = followingJsonArray.length();
            followingIds = new String[following];
            for (int i = 0; i < following; i++) {
                followingIds[i] = (String) followingJsonArray.get(i);
            }
            playlistsCount = user.getJSONArray("playlists").length();
            email = user.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        topUsername.setText(name);
        middleUsername.setText(name);
        followersCount.setText(Integer.toString(followers));
        followingCount.setText(Integer.toString(following));
        playlistCount.setText(Integer.toString(playlistsCount));
        fillRecyclerView(user);
        loading = false;
    }

    /**
     * fill recyclerView from the passed JSONObject
     *
     * @param jsonObject JSONObject to fill the recyclerView from
     */
    private void fillRecyclerView(JSONObject jsonObject) {
        try {
            JSONArray playlistsJson = jsonObject.getJSONArray("playlists");
            onlinePlaylistsList = new ArrayList<ThreeDataItem>();
            playlistIds = new String[playlistsJson.length()];
            for (int i = 0; i < playlistsJson.length(); i++) {
                JSONObject playlist = playlistsJson.getJSONObject(i);
                String image = playlist.getString("image");
                String name = playlist.getString("name");
                String description = playlist.getString("description");
                playlistIds[i] = playlist.getString("_id");
                onlinePlaylistsList.add(new ThreeDataItem(image, name, description));
            }
            updatePlaylists();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updatePlaylists() {
        mAdapter = new UserPlaylistAdapter(onlineData, context);
        playlistsRecyclerView.setLayoutManager(mLayoutManager);
        playlistsRecyclerView.setAdapter(mAdapter);
    }
}
