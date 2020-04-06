package com.example.projectx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private Button editProfile;
    private TextView playlistCount, followersCount, followingCount, activityStatus;
    private RecyclerView playlistsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout playlistLayout, followersLayout, followingLayout;
    private boolean testing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findObjects();
        setListeners();
        updateStats();
    }

    private void findObjects() {
        editProfile = (Button) findViewById(R.id.edit_button_bt);
        playlistCount = (TextView) findViewById(R.id.playlists_count_tv);
        followersCount = (TextView) findViewById(R.id.followers_count_tv);
        followingCount = (TextView) findViewById(R.id.following_count_tv);
        activityStatus = (TextView) findViewById(R.id.activity_status_tv);
        playlistLayout = (LinearLayout) findViewById(R.id.playlists_layout_ll);
        followersLayout = (LinearLayout) findViewById(R.id.followers_layout_ll);
        followingLayout = (LinearLayout) findViewById(R.id.following_layout_ll);
        playlistsRecyclerView = (RecyclerView) findViewById(R.id.playlists_list_rv);
        playlistsRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
    }

    private void setListeners() {

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

    }

    private void updateStats() {
        updateFollowers();
        updateFollowing();
        updatePlaylists();
    }

    private void updateFollowers() {
        //TODO: call api to get number of followers
    }

    private void updateFollowing() {
        //TODO: call api to get number of following
    }

    private void updatePlaylists() {
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
            //TODO: call api to actual server and get playlists of current logged in user
        }
    }
}
