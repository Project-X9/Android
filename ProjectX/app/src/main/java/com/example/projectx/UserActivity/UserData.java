package com.example.projectx.UserActivity;

public class UserData {
    private String mUserPlaylistImageURL;
    private String mUserPlaylistName;

    public UserData(String mUserPlaylistImageURL, String mUserPlaylistName) {
        this.mUserPlaylistImageURL = mUserPlaylistImageURL;
        this.mUserPlaylistName = mUserPlaylistName;
    }

    public String getmUserPlaylistImageURL() {
        return mUserPlaylistImageURL;
    }

    public String getmUserPlaylistName() {
        return mUserPlaylistName;
    }
}
