package com.example.projectx.Artist;

public class ArtistTracksList {
    private int mArtistImageResoure;
    private String mArtistImageURL;
    private String mArtistName;
    private String mArtistSongName;



    public ArtistTracksList(String mArtistImageURL, String mArtistName, String mArtistSongName) {
        this.mArtistImageURL = mArtistImageURL;
        this.mArtistName = mArtistName;
        this.mArtistSongName = mArtistSongName;
    }

    public ArtistTracksList(int mArtistImageResoure, String mArtistName, String mArtistSongName) {
        this.mArtistImageResoure = mArtistImageResoure;
        this.mArtistName = mArtistName;
        this.mArtistSongName = mArtistSongName;
    }

    public int getmArtistImageResoure() {
        return mArtistImageResoure;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public String getmArtistSongName() {
        return mArtistSongName;
    }

    public void setmArtistSongName(String mArtistSongName) {
        this.mArtistSongName = mArtistSongName;
    }

    public String getmArtistImageURL() {
        return mArtistImageURL;
    }
}
