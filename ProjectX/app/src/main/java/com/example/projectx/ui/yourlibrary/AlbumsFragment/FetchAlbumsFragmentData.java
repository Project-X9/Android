package com.example.projectx.ui.yourlibrary.AlbumsFragment;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FetchAlbumsFragmentData extends AsyncTask {
    private AlbumsFragment albumsFragment;
    java.net.URL URL;
    private String data = "";
    private JSONObject dataObj;

    FetchAlbumsFragmentData(AlbumsFragment albumsFragment) {
        this.albumsFragment = albumsFragment;
    }

    public void setURL(String url) {
        try {
            URL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) URL.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dataObj = new JSONObject(data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (dataObj == null) return;
        fillRecyclerView();
    }

    private void fillRecyclerView() {
        try {
            JSONArray albumsJsonArray = dataObj.getJSONArray("Albums");
            albumsFragment.onlineData = new ArrayList<>();
            for (int i = 0; i < albumsJsonArray.length(); i++) {
                JSONObject album = albumsJsonArray.getJSONObject(i);
                albumsFragment.onlineData.add(new AlbumsData(album.getString("name"), album.getString("photo")));
            }
            albumsFragment.updateAlbums();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
