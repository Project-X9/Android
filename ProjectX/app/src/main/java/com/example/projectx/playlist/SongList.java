package com.example.projectx.playlist;

public class SongList  {

    private int mImageResoure;
    private String mText1;
    private String mText2;

    public SongList(int mImageResoure, String mText1, String mText2) {
        this.mImageResoure = mImageResoure;
        this.mText1 = mText1;
        this.mText2 = mText2;
    }

    public int getmImageResoure() {
        return mImageResoure;
    }

    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }
}
