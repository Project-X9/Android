package com.example.projectx.Artist;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class FetchArtistData extends AsyncTask {
    private ArtistInterface artist;
    java.net.URL URL;
    private String data = "";
    private JSONObject dataObj;

    FetchArtistData(ArtistInterface artist) {
        this.artist = artist;
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
        try {
            String name = dataObj.getString("Name");
            String picture = dataObj.getString("Picture");
//            int count = dataObj.getJSONArray("Songs").length();
            ArtistInterface.topArtistName.setText(name);
            ArtistInterface.mArtistName.setText(name);
            ArtistInterface.imageURL=picture;
            fillRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillRecyclerView() {
        try {
            JSONArray songsJson = dataObj.getJSONArray("Songs");
            ArtistInterface.onlineData = new ArrayList<ArtistTracksList>();
            for (int i = 0; i < songsJson.length(); i++) {
                JSONObject song = songsJson.getJSONObject(i);
                ArtistInterface.onlineData.add(new ArtistTracksList(song.getString("Picture"), song.getString("Artist"),song.getString("Name")));
            }
            ArtistInterface.updatePlaylists();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
