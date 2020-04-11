package com.example.projectx.playlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.Artist.RecyclerTouchListener;
import com.example.projectx.MusicPlayer;
import com.example.projectx.R;
import com.example.projectx.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PlayListFull extends AppCompatActivity implements SongAdapter.onSongListner {

    String PLAYLIST_FETCH_SERVER = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/playlist/";
    String playlistId = "5e8741dadfdb0a35d429a128";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView playlistName;
    static ArrayList<Song> songArrayList=new ArrayList<>();
    static String[] SongIDStringArray;
    static String ClickedSongId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_full);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String temp = b.getString("PlaylistIDs");
        playlistId = temp;
        FetchPlaylist fetchPlaylist = new FetchPlaylist();
        fetchPlaylist.execute();

        playlistName = findViewById(R.id.playlistName_tv);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SongAdapter(songArrayList, getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * ReturnBack it close the playlist activity.
     * @param v
     */
    public void returnBack(View v) {
        finish();
//        Intent intent = new Intent(this, PlaylistEmpty.class);
//        startActivity(intent);
    }

    /**
     * OnSongClick it get the position of the clicked song in the recycler view and send it's
     * ID to music player activity beside that it also send an array of song ID.
     * @param position
     */
    @Override
    public void onSongClick(int position) {

        ClickedSongId = songArrayList.get(position).id;

        Intent i = new Intent(this, MusicPlayer.class);
        Bundle extras = new Bundle();
        extras.putStringArray("songslistarray", SongIDStringArray);
        extras.putString("songid", ClickedSongId);
        i.putExtras(extras);
        startActivity(i);
    }

    /**
     * FetchedPlayList is used an AsyncTask to fetch the data from the server of the backend
     */
    private class FetchPlaylist extends AsyncTask<String, String, String> {

        String mNamePlaylist;

        /**
         * doInBackground  call a function Playlist that fetch the name of playlist from backend server.
         * @param strings
         * @return the name of the fetched playlist.
         */
        @Override
        protected String doInBackground(String... strings) {
            String s = Playlist(mNamePlaylist);
            return s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * OnPostExecute take a the name of the fetched playlist and set it.
         *  and it add all the fetched song from the server  of this specific playlist
         *  to song array list and notify the adapter of the recycler view with the changes.
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            playlistName.setText(s);
            SongIDStringArray=new String[songArrayList.size()];
            for (int i = 0; i < songArrayList.size(); i++) {
                SongIDStringArray[i] = songArrayList.get(i).id;
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Playlist  it fetch that songs corresponding to this playlist
     * and set mNamePlaylist with the name of it and finally
     * fill the song array list with the songs after fill it's data
     * from the server.
     * @param mNamePlaylist the name that will be set to the playlist
     * @return
     */
    public String Playlist(String mNamePlaylist ) {
        songArrayList = new ArrayList<>();
        ArrayList<JSONObject> jsonObjectArray = new ArrayList<>();

        final String PlayList_URL = PLAYLIST_FETCH_SERVER + playlistId;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PlayList_URL, null, future, future);
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(request);

        try {
            JSONObject response = future.get();
            JSONObject data = response.getJSONObject("data");
            JSONObject dPlaylist = data.getJSONObject("playlist");
            mNamePlaylist = dPlaylist.getString("name");
            JSONArray Tracks = dPlaylist.getJSONArray("tracks");
            for (int i = 0; i < Tracks.length(); i++) {
                JSONObject json = Tracks.getJSONObject(i);
                jsonObjectArray.add(json);
            }
            for (int i = 0; i < jsonObjectArray.size(); i++) {
                String trackId = jsonObjectArray.get(i).getString("_id");
                String trackDescription = jsonObjectArray.get(i).getString("description");
                String trackNames = jsonObjectArray.get(i).getString("name");
                String trackPlaycount = jsonObjectArray.get(i).getString("playcount");
                String trackUrl = jsonObjectArray.get(i).getString("url");
                String trackDuration = jsonObjectArray.get(i).getString("duration");
                String trackImageUrl = jsonObjectArray.get(i).getString("imageUrl");
                JSONArray artists =  jsonObjectArray.get(i).getJSONArray("artists");

                ArrayList<JSONObject> jsonObjectArray1 = new ArrayList<>();
                for (int j = 0; j < artists.length(); j++) {
                    JSONObject json = artists.getJSONObject(j);
                    jsonObjectArray1.add(json);
                }
                ArrayList<String> artistIdArrayList = new ArrayList<>();
                ArrayList<String> artistNameArrayList = new ArrayList<>();
                for (int k=0; k <  jsonObjectArray1.size(); k++){
                    String Artist_id = jsonObjectArray1.get(k).getString("_id");
                    String Artist_name = jsonObjectArray1.get(k).getString("name");
                    artistIdArrayList.add(Artist_id);
                    artistNameArrayList.add(Artist_name);
                }
                JSONArray genres =  jsonObjectArray.get(i).getJSONArray("genres");

                ArrayList<JSONObject> jsonObjectArray2 = new ArrayList<>();
                for (int j = 0; j < genres.length(); j++) {
                    JSONObject json = genres.getJSONObject(j);
                    jsonObjectArray2.add(json);
                }
                ArrayList<String> genresIdarraylist = new ArrayList<>();
                for (int k=0; k <  jsonObjectArray2.size(); k++){
                    String Genres_id = jsonObjectArray2.get(k).getString("_id");
                    genresIdarraylist.add(Genres_id);
                }
                Song mSong = new Song();
                mSong.id=trackId;
                mSong.description=trackDescription;
                mSong.name=trackNames;
                mSong.playcount=trackPlaycount;
                mSong.url=trackUrl;
                mSong.duration=trackDuration;
                mSong.imageUrl=trackImageUrl;
                mSong.artists=artistIdArrayList;
                mSong.genres=genresIdarraylist;
                mSong.artist_name=artistNameArrayList;
                songArrayList.add(mSong);
            }
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }

        catch (ExecutionException e) {
            e.printStackTrace();
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        return mNamePlaylist;
    }

}
