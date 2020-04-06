package com.example.projectx;

public class ThreeDataItem {
    private int ImageResource;
    private String Text1;
    private String Text2;

    public ThreeDataItem(int img, String Text1, String Text2) {
        this.ImageResource = img;
        this.Text1 = Text1;
        this.Text2 = Text2;
    }

    public int getImageResource() {
        return ImageResource;
    }

    public String getText1() {
        return Text1;
    }

    public String getText2() {
        return Text2;
    }
}
