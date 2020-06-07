package com.example.projectx.ui.yourlibrary.ArtistFragment;

public class ArtistData {
    private String Name,Photo,NoOfFollowers,Albums,Bio;

    public ArtistData(String name, String photo, String noOfFollowers, String albums, String bio) {
        Name = name;
        Photo = photo;
        NoOfFollowers = noOfFollowers;
        Albums = albums;
        Bio = bio;
    }

    public String getName() {
        return Name;
    }

    public String getPhoto() {
        return Photo;
    }

    public String getNoOfFollowers() {
        return NoOfFollowers;
    }

    public String getAlbums() {
        return Albums;
    }

    public String getBio() {
        return Bio;
    }
}
