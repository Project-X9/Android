package com.example.projectx.UserActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectx.R;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    private final String MOCK_URL = "http://www.mocky.io/v2/5edb7b303200009f7a5d2768";
    private static Context context;
    private Button follow;
    static TextView topUsername, middleUsername, playlistCount, followersCount, followingCount, activityStatus;
    private static RecyclerView playlistsRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;
    static String imageURL;
    static ImageView mUserImage;
    private LinearLayout playlistLayout, followersLayout, followingLayout,headerLayout;
    private AppBarLayout appBarLayout;
    private ConstraintLayout backgroundGradient;
    static ArrayList<UserData> onlineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activity);

        follow =  findViewById(R.id.follow_button_bt);
        playlistCount = findViewById(R.id.playlists_count_tv);
        followersCount = findViewById(R.id.followers_count_tv);
        followingCount =  findViewById(R.id.following_count_tv);
        activityStatus = findViewById(R.id.activity_status_tv);
        middleUsername = findViewById(R.id.middle_username_tv);
        topUsername = findViewById(R.id.top_username_tv);
        playlistLayout =  findViewById(R.id.playlists_layout_ll);
        followersLayout =  findViewById(R.id.followers_layout_ll);
        followingLayout =  findViewById(R.id.following_layout_ll);
        appBarLayout = findViewById(R.id.profile_appbarlayout);
        headerLayout =  findViewById(R.id.header_content_ll);
        playlistsRecyclerView =  findViewById(R.id.playlists_list_rv);
        backgroundGradient = findViewById(R.id.background_gradient_cl);
        mUserImage = findViewById(R.id.profile_image_iv);
        context=getApplicationContext();
        mLayoutManager = new LinearLayoutManager(this);
        setOnClickListeners();
        updateFollowers();
        updateFollowing();
        FetchUserData fetchUserData = new FetchUserData(this);
        fetchUserData.setURL(MOCK_URL);
        fetchUserData.execute();
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

    public static void setUserPicture(String imageURL){
        Picasso.with(context)
                .load(imageURL)
                .into(mUserImage);
    }

    public static void updatePlaylists() {
        setUserPicture(imageURL);
         mAdapter = new UserPlaylistAdapter(onlineData,context);
        playlistsRecyclerView.setLayoutManager(mLayoutManager);
        playlistsRecyclerView.setAdapter(mAdapter);
    }
}
