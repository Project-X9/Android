package com.example.projectx;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;

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

public class FetchProfileData extends AsyncTask {

    private Profile profile;
    java.net.URL URL;
    private String data = "";
    private JSONObject dataObj;

    FetchProfileData(Profile profile) {
        this.profile = profile;
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
            Log.d("TAG", "onPostExecute: 2323423");
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
            Log.d("TAG", "onPostExecute: " + name);
            Log.d("TAG", "onPostExecute: " + dataObj);
            int count = dataObj.getJSONArray("Playlists").length();
            profile.topUsername.setText(name);
            profile.middleUsername.setText(name);
            profile.followersCount.setText(Integer.toString(dataObj.getInt("Followers")));
            profile.followingCount.setText(Integer.toString(dataObj.getInt("Following")));
            profile.playlistCount.setText(Integer.toString(count));
            fillRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillRecyclerView() {
        try {
            JSONArray playlistsJson = dataObj.getJSONArray("Playlists");
            profile.onlinePlaylistsList = new ArrayList<ThreeDataItem>();
            for (int i = 0; i < playlistsJson.length(); i++) {
                JSONObject playlist = playlistsJson.getJSONObject(i);
                profile.onlinePlaylistsList.add(new ThreeDataItem(playlist.getString("Picture"), playlist.getString("Name"), ""));
            }
            profile.updatePlaylists();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
