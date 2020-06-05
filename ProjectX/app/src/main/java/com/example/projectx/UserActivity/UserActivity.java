package com.example.projectx.UserActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectx.R;
import com.google.android.material.appbar.AppBarLayout;

public class UserActivity extends AppCompatActivity {
    private Button follow;
    private TextView playlistCount, followersCount, followingCount, activityStatus,topUsername;
    private RecyclerView playlistsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout playlistLayout, followersLayout, followingLayout,headerLayout;
    private AppBarLayout appBarLayout;
    private ConstraintLayout backgroundGradient;
    private boolean testing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activity);

        follow =  findViewById(R.id.follow_button_bt);
        playlistCount = findViewById(R.id.playlists_count_tv);
        followersCount = findViewById(R.id.followers_count_tv);
        followingCount =  findViewById(R.id.following_count_tv);
        activityStatus = findViewById(R.id.activity_status_tv);
        topUsername = findViewById(R.id.top_username_tv);
        playlistLayout =  findViewById(R.id.playlists_layout_ll);
        followersLayout =  findViewById(R.id.followers_layout_ll);
        followingLayout =  findViewById(R.id.following_layout_ll);
        appBarLayout = findViewById(R.id.profile_appbarlayout);
        headerLayout =  findViewById(R.id.header_content_ll);
        playlistsRecyclerView =  findViewById(R.id.playlists_list_rv);
        backgroundGradient = findViewById(R.id.background_gradient_cl);
       // playlistsRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        setOnClickListeners();
        updateFollowers();
        updateFollowing();
        updatePlaylists();
    }

    private void setOnClickListeners() {

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

            }
        });
    }


    private void updateFollowers() {
        //TODO: call api to get number of followers
    }

    private void updateFollowing() {
        //TODO: call api to get number of following
    }

    private void updatePlaylists() {
        //TODO: call api to  get playlists of searched user

    }
}
