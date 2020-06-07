package com.example.projectx.ui.yourlibrary.ArtistFragment;

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

public class FetchArtistFragmentData extends AsyncTask {
    private ArtisttFragment artisttFragment;
    java.net.URL URL;
    private String data = "";
    private JSONObject dataObj;

    FetchArtistFragmentData(ArtisttFragment artisttFragment) {
        this.artisttFragment = artisttFragment;
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
            JSONArray artistsJsonArray = dataObj.getJSONArray("Artists");
            artisttFragment.onlineData = new ArrayList<>();
            for (int i = 0; i < artistsJsonArray.length(); i++) {
                JSONObject artist = artistsJsonArray.getJSONObject(i);
                artisttFragment.onlineData.add(new ArtistData(artist.getString("Name"), artist.getString("Photo"),artist.getString("NoOfFollowers"),artist.getString("Albums"),artist.getString("Bio")));
            }
            artisttFragment.updateArtist();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
