package com.example.projectx.UserActivity;

public class UserData {
    private String mUserPlaylistImageURL;
    private String mUserPlaylistName;
    private String mMadeBy;
    private String id;
    private int createImage;

    public UserData(String mUserPlaylistImageURL, String mUserPlaylistName,String mMadeBy,String id) {
        this.mUserPlaylistImageURL = mUserPlaylistImageURL;
        this.mUserPlaylistName = mUserPlaylistName;
        this.mMadeBy=mMadeBy;
        this.id=id;
    }

    public UserData(int createImage, String mUserPlaylistName,String mMadeBy,String id) {
        this.createImage = createImage;
        this.mUserPlaylistName = mUserPlaylistName;
        this.mMadeBy=mMadeBy;
        this.id=id;
    }

    public String getmUserPlaylistImageURL() {
        return mUserPlaylistImageURL;
    }

    public String getmUserPlaylistName() {
        return mUserPlaylistName;
    }

    public String getmMadeBy() {
        return mMadeBy;
    }

    public int getCreateImage() {
        return createImage;
    }

    public String getId() {
        return id;
    }
}
