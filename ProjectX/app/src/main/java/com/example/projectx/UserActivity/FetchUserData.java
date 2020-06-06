package com.example.projectx.UserActivity;

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

public class FetchUserData extends AsyncTask {
    private UserActivity user;
    java.net.URL URL;
    private String data = "";
    private JSONObject dataObj;

    FetchUserData(UserActivity user) {
        this.user = user;
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
            int count = dataObj.getJSONArray("Playlists").length();
            UserActivity.topUsername.setText(name);
            UserActivity.middleUsername.setText(name);
            UserActivity.imageURL=picture;
            UserActivity.followersCount.setText(Integer.toString(dataObj.getInt("Followers")));
            UserActivity.followingCount.setText(Integer.toString(dataObj.getInt("Following")));
            UserActivity.playlistCount.setText(Integer.toString(count));
            fillRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillRecyclerView() {
        try {
            JSONArray playlistsJson = dataObj.getJSONArray("Playlists");
            UserActivity.onlineData = new ArrayList<UserData>();
            for (int i = 0; i < playlistsJson.length(); i++) {
                JSONObject playlist = playlistsJson.getJSONObject(i);
                UserActivity.onlineData.add(new UserData(playlist.getString("Picture"), playlist.getString("Name")));
            }
            UserActivity.updatePlaylists();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
