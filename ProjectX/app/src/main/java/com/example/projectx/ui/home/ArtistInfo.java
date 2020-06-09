package com.example.projectx.ui.home;

public class ArtistInfo {
    private String name;
    private String imageUrl;
    private String id;

    public ArtistInfo(String artistName, String artistImage, String artistId){
        name = artistName;
        imageUrl = artistImage;
        id = artistId;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
