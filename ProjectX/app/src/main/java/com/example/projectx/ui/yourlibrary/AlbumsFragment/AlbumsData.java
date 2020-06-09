package com.example.projectx.ui.yourlibrary.AlbumsFragment;

public class AlbumsData {
    String name,photo,id;

    public AlbumsData(String name, String photo,String id) {
        this.name = name;
        this.photo = photo;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }
}
