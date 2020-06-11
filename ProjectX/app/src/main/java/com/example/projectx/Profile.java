package com.example.projectx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectx.authentication.AuthenticationPage;
import com.example.projectx.playlist.PlayListFull;
import com.facebook.login.LoginManager;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Profile extends AppCompatActivity {
    //Constants
    private final String MOCK_URL = "http://www.mocky.io/v2/5ec9d1943000007900a6ce88";
    final String CREDENTIALS_FILE = "loginCreds";

    //Views
    private Button editProfile, logoutButton;
    TextView topUsername, middleUsername, playlistCount, followersCount, followingCount, activityStatus;
    RecyclerView playlistsRecyclerView;
    private ThreeDataItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout headerLayout, playlistLayout, followersLayout, followingLayout;
    private ConstraintLayout backgroundGradient;
    private AppBarLayout appBarLayout;
    private Guideline guideline;
    private ImageButton backButton;

    //Variables
    ArrayList<ThreeDataItem> onlinePlaylistsList;
    private String name, email;
    private int followers, following, playlistsCount;
    String[] playlistIds;
    String[] followersIds;
    String[] followingIds;
    private boolean testing = false;
    private boolean loading;
    SharedPreferences loginCredentials;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findObjects();
        setListeners();
        setUserData();
    }

    private void setUserData() {
        sendRequest();
    }

    /**
     * Send get request to server to get user data
     */
    private void sendRequest() {
        loginCredentials = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
        String userId = loginCredentials.getString("id", null);
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
                String arg = "Bearer " + token;
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
     * find UI elements by id and start fetch profile data
     */
    private void findObjects() {
        backButton = (ImageButton) findViewById(R.id.collapse_ib);
        editProfile = (Button) findViewById(R.id.edit_button_bt);
        logoutButton = (Button) findViewById(R.id.logout_bt);
        topUsername = (TextView) findViewById(R.id.top_username_tv);
        middleUsername = (TextView) findViewById(R.id.middle_username_tv);
        playlistCount = (TextView) findViewById(R.id.playlists_count_tv);
        followersCount = (TextView) findViewById(R.id.followers_count_tv);
        followingCount = (TextView) findViewById(R.id.following_count_tv);
        activityStatus = (TextView) findViewById(R.id.activity_status_tv);
        playlistLayout = (LinearLayout) findViewById(R.id.playlists_layout_ll);
        followersLayout = (LinearLayout) findViewById(R.id.followers_layout_ll);
        followingLayout = (LinearLayout) findViewById(R.id.following_layout_ll);
        playlistsRecyclerView = (RecyclerView) findViewById(R.id.playlists_list_rv);
        appBarLayout = (AppBarLayout) findViewById(R.id.profile_appbarlayout);
        headerLayout = (LinearLayout) findViewById(R.id.header_content_ll);
        backgroundGradient = (ConstraintLayout) findViewById(R.id.background_gradient_cl);
        guideline = (Guideline) findViewById(R.id.guideline3);

//        playlistsRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
    }

    /**
     * set listeners for UI elements
     */
    private void setListeners() {

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditActivity();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCredentials = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = loginCredentials.edit();
                editor.clear();
                editor.commit();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getBaseContext(), AuthenticationPage.class));
                finishAffinity();
            }
        });

        followersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loading | followers == 0) return;
                Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
                Bundle extras = new Bundle();
                extras.putStringArray("UserIds", followersIds);
                extras.putString("Title", "Followers");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loading | following == 0) return;
                Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
                Bundle extras = new Bundle();
                extras.putStringArray("UserIds", followingIds);
                extras.putString("Title", "Following");
                intent.putExtras(extras);
                startActivity(intent);
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
                Log.d("TAG", "onOffsetChanged: " + (-verticalOffset / x));
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
     * open edit activity
     */
    private void openEditActivity() {
        if (loading) return;
        Intent intent = new Intent(getBaseContext(), EditProfile.class);
        intent.putExtra("Name", name);
        intent.putExtra("Email", email);
        intent.putExtra("Email", email);
        startActivity(intent);
    }

    /**
     * updates the playlist recyclerview from onlinePlaylistsList
     */
    void updatePlaylists() {
        mAdapter = new ThreeDataItemAdapter(onlinePlaylistsList);
        playlistsRecyclerView.setLayoutManager(mLayoutManager);
        playlistsRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ThreeDataItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int Position) {
                Intent intent = new Intent(getApplicationContext(), PlayListFull.class);
                intent.putExtra("PlaylistIDs", playlistIds[Position]);
                startActivity(intent);
            }
        });
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
}
