package com.example.projectx;

public class ThreeDataItem {


    private int ImageResourceId = 0;
    private String ImageSource;
    private String Text1;
    private String Text2;

    public ThreeDataItem(String img, String Text1, String Text2) {
        this.ImageSource = img;
        this.Text1 = Text1;
        this.Text2 = Text2;
    }

    public ThreeDataItem(int img, String Text1, String Text2) {
        this.ImageResourceId = img;
        this.Text1 = Text1;
        this.Text2 = Text2;
    }

    public int getImageResourceId() {
        return ImageResourceId;
    }

    public String getImageSource() {
        return ImageSource;
    }

    public String getText1() {
        return Text1;
    }

    public String getText2() {
        return Text2;
    }
}
