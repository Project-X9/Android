package com.example.projectx;

import android.os.AsyncTask;
import android.util.Log;

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

public class fetchMusicData extends AsyncTask<Void, Void, Void> {

    URL URL;
    private String data = "";
    private JSONObject dataObj;

    public void setURL(String url) {
        try {
            URL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
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
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (dataObj == null) {
            MusicPlayer.loading = false;
            return;
        }

        try {
            setArtistsId();

            setArtistNames();
//            MusicPlayer.currentSong.likers = dataObj.getJSONObject("data").getJSONObject("track")
//                    .getString("url");

//            MusicPlayer.currentSong.genres = dataObj.getJSONObject("data").getJSONObject("track")
//                    .getString("url");
            MusicPlayer.currentSong.id = dataObj.getJSONObject("data").getJSONObject("track")
                    .getString("_id");

            MusicPlayer.currentSong.description = dataObj.getJSONObject("data").getJSONObject("track")
                    .getString("description");

            MusicPlayer.currentSong.name = dataObj.getJSONObject("data").getJSONObject("track")
                    .getString("name");

            MusicPlayer.currentSong.playcount = dataObj.getJSONObject("data").getJSONObject("track")
                    .getString("playcount");

            MusicPlayer.currentSong.url = dataObj.getJSONObject("data").getJSONObject("track")
                    .getString("url");

            MusicPlayer.currentSong.duration = dataObj.getJSONObject("data").getJSONObject("track")
                    .getString("duration");

            MusicPlayer.currentSong.imageUrl = dataObj.getJSONObject("data").getJSONObject("track")
                    .getString("imageUrl");

            MusicPlayer.currentSong.albumName = dataObj.getJSONObject("data").getJSONObject("track")
                    .getJSONObject("album").getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Log.d("TAG", "onPostExecute: " + MusicPlayer.currentSong.url);
            if (MusicPlayer.currentSong.url == null) {
                MusicPlayer.loading = false;
                return;
            }

//            MusicPlayer.mediaPlayer.reset();

            MusicPlayer.mediaPlayer.setDataSource(MusicPlayer.currentSong.url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MusicPlayer.mediaPlayer.prepareAsync();
    }

    private void setArtistNames() {
        try {
            if (dataObj == null) return;
            JSONArray artistsJSonArray = dataObj.getJSONObject("data").getJSONObject("track")
                    .getJSONArray("artists");
            MusicPlayer.currentSong.artistsNames = new String[artistsJSonArray.length()];

            for (int i = 0; i < artistsJSonArray.length(); i++) {
                MusicPlayer.currentSong.artistsNames[i] = artistsJSonArray.getJSONObject(i).getString("name");
            }
            Log.d("TAG", "setArtistsId: artistsis" + MusicPlayer.currentSong.artistsNames[0]);
            Log.d("TAG", "setArtistsId: artistsis" + artistsJSonArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setArtistsId() {
        try {
            if (dataObj == null) return;
            JSONArray artistsJSonArray = dataObj.getJSONObject("data").getJSONObject("track")
                    .getJSONArray("artists");
            MusicPlayer.currentSong.artistsIds = new String[artistsJSonArray.length()];

            for (int i = 0; i < artistsJSonArray.length(); i++) {
                MusicPlayer.currentSong.artistsIds[i] = artistsJSonArray.getJSONObject(i).getString("_id");
            }
            Log.d("TAG", "setArtistsId: artistsis" + MusicPlayer.currentSong.artistsIds[0]);
            Log.d("TAG", "setArtistsId: artistsis" + artistsJSonArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
