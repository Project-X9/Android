package com.example.projectx.ui.yourlibrary;

import android.os.AsyncTask;

import com.example.projectx.UserActivity.UserData;

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

public class FetchPlaylistFragmentData extends AsyncTask {
    private PlaylistFragment playlistFragment;
    java.net.URL URL;
    private String data = "";
    private JSONObject dataObj;

    FetchPlaylistFragmentData(PlaylistFragment playlistFragment) {
        this.playlistFragment = playlistFragment;
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
            JSONArray playlistsJson = dataObj.getJSONObject("data").getJSONArray("playlists");
            for (int i = 0; i < playlistsJson.length(); i++) {
                JSONObject playlist = playlistsJson.getJSONObject(i);
                playlistFragment.onlineData.add(new UserData(playlist.getString("image"), playlist.getString("name"),playlist.getString("author"),playlist.getString("_id")));
            }
            playlistFragment.updatePlaylists();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
