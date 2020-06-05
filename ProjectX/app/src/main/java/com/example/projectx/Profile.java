package com.example.projectx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private final String MOCK_URL = "http://www.mocky.io/v2/5ec9d1943000007900a6ce88";
    private Button editProfile;
    TextView topUsername, middleUsername, playlistCount, followersCount, followingCount, activityStatus;
    RecyclerView playlistsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout headerLayout, playlistLayout, followersLayout, followingLayout;
    private ConstraintLayout backgroundGradient;
    private AppBarLayout appBarLayout;
    private Guideline guideline;
    private ImageButton backButton;
    ArrayList<ThreeDataItem> onlinePlaylistsList;

    private boolean testing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findObjects();
        setListeners();
    }

    /**
     *find UI elements by id and start fetch profile data
     */
    private void findObjects() {
        backButton = (ImageButton) findViewById(R.id.collapse_ib);
        editProfile = (Button) findViewById(R.id.edit_button_bt);
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
        FetchProfileData fetchProfileData = new FetchProfileData(this);
        fetchProfileData.setURL(MOCK_URL);
        fetchProfileData.execute();
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

        playlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: intent to open playlists page of current user
            }
        });

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
                Log.d("TAG", "onOffsetChanged: " + (-verticalOffset / x));

            }
        });
    }

    /**
     * open edit activity
     */
    private void openEditActivity() {
        Intent intent = new Intent(getBaseContext(), EditProfile.class);
        intent.putExtra("Name", middleUsername.getText());
        startActivity(intent);
    }

    /**
     * updates the playlist recyclerview from onlinePlaylistsList
     */
    void updatePlaylists() {
        if (testing) {
            ArrayList<ThreeDataItem> playlistsList = new ArrayList<>();
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_starboy, "Starboy", "The Weeknd"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_cosmic, "Beautiful", "Bazzi"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_mahraganat, "Mahragan Bent El Geran", "Hassan Shakosh"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_starboy, "Starboy", "The Weeknd"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_cosmic, "Beautiful", "Bazzi"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_mahraganat, "Mahragan Bent El Geran", "Hassan Shakosh"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_starboy, "Starboy", "The Weeknd"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_cosmic, "Beautiful", "Bazzi"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_mahraganat, "Mahragan Bent El Geran", "Hassan Shakosh"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_starboy, "Starboy", "The Weeknd"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_cosmic, "Beautiful", "Bazzi"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_mahraganat, "Mahragan Bent El Geran", "Hassan Shakosh"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_starboy, "Starboy", "The Weeknd"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_cosmic, "Beautiful", "Bazzi"));
            playlistsList.add(new ThreeDataItem(R.drawable.album_art_mahraganat, "Mahragan Bent El Geran", "Hassan Shakosh"));
            mAdapter = new ThreeDataItemAdapter(playlistsList);
            playlistsRecyclerView.setLayoutManager(mLayoutManager);
            playlistsRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter = new ThreeDataItemAdapter(onlinePlaylistsList);
            playlistsRecyclerView.setLayoutManager(mLayoutManager);
            playlistsRecyclerView.setAdapter(mAdapter);
        }
    }
}
