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

public class PlayListFull extends AppCompatActivity implements SongAdapter.onSongListner  {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView playlistName;
    static ArrayList<Song> songArrayList;
    static String [] SongIDStringArray;
    static String ClickedSongId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FetchPlaylist fetchPlaylist = new FetchPlaylist();
        fetchPlaylist.execute();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_list_full);
        playlistName = findViewById(R.id.playlistName_tv);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager =  new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        for(int i=0;i<songArrayList.size();i++){
            SongIDStringArray[i]=songArrayList.get(i).id;
        }
        mAdapter = new SongAdapter(songArrayList,getApplicationContext(),this);
        mRecyclerView.setAdapter(mAdapter);

    }
    public void returnBack(View v){
        Intent intent = new Intent(this, PlaylistEmpty.class);
        startActivity(intent);
    }
    @Override
    public void onSongClick(int position) {

        ClickedSongId=songArrayList.get(position).id;

        Intent i=new Intent(this, MusicPlayer.class);
        Bundle extras=new Bundle();
        extras.putStringArray("songslistarray", SongIDStringArray);
        extras.putString("songid",ClickedSongId);
        i.putExtras(extras);
        startActivity(i);
    }

    private class FetchPlaylist extends AsyncTask<String, String, String>  {

         String mNamePlaylist;


        @Override
        protected String doInBackground(String... strings) {
            String s = Playlist(mNamePlaylist);
            return s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            playlistName.setText(s);
            mAdapter.notifyDataSetChanged();
        }

    }

    public String Playlist(String mNamePlaylist ) {
        songArrayList = new ArrayList<>();
        ArrayList<JSONObject> jsonObjectArray = new ArrayList<>();
        ArrayList<JSONObject> jsonObjectArray1 = new ArrayList<>();
        ArrayList<JSONObject> jsonObjectArray2 = new ArrayList<>();
        final String PlayList_URL = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/playlist/5e8741dadfdb0a35d429a128";
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PlayList_URL, null, future, future);
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(request);

        try {
            JSONObject response = future.get();
            JSONObject Data = response.getJSONObject("data");
            JSONObject Dplaylist = Data.getJSONObject("playlist");
            mNamePlaylist = Dplaylist.getString("name");
            JSONArray Tracks = Dplaylist.getJSONArray("tracks");
            for (int i = 0; i < Tracks.length(); i++) {
                JSONObject json = Tracks.getJSONObject(i);
                jsonObjectArray.add(json);
            }
            for (int i = 0; i < jsonObjectArray.size(); i++) {
                String Track_id = jsonObjectArray.get(i).getString("_id");
                String Track_description = jsonObjectArray.get(i).getString("description");
                String Track_name = jsonObjectArray.get(i).getString("name");
                String Track_playcount = jsonObjectArray.get(i).getString("playcount");
                String Track_url = jsonObjectArray.get(i).getString("url");
                String Track_duration = jsonObjectArray.get(i).getString("duration");
                String Track_imageUrl = jsonObjectArray.get(i).getString("imageUrl");
                JSONArray artists =  jsonObjectArray.get(i).getJSONArray("artists");
                for (int j = 0; j < artists.length(); j++) {
                    JSONObject json = artists.getJSONObject(j);
                    jsonObjectArray1.add(json);
                }
                ArrayList<String> ArtistIDArrayList = new ArrayList<>();
                ArrayList<String> ArtistNameArrayList = new ArrayList<>();
                for (int k=0; k <  jsonObjectArray1.size(); k++){
                    String Artist_id = jsonObjectArray1.get(k).getString("_id");
                    String Artist_name = jsonObjectArray1.get(k).getString("name");
                    ArtistIDArrayList.add(Artist_id);
                    ArtistNameArrayList.add(Artist_name);
                }
                JSONArray genres =  jsonObjectArray.get(i).getJSONArray("genres");
                for (int j = 0; j < genres.length(); j++) {
                    JSONObject json = genres.getJSONObject(j);
                    jsonObjectArray2.add(json);
                }
                ArrayList<String> GenresIDArrayList = new ArrayList<>();
                for (int k=0; k <  jsonObjectArray2.size(); k++){
                    String Genres_id = jsonObjectArray2.get(k).getString("_id");
                    GenresIDArrayList.add(Genres_id);
                }
                Song mSong = new Song();
                mSong.id=Track_id;
                mSong.description=Track_description;
                mSong.name=Track_name;
                mSong.playcount=Track_playcount;
                mSong.url=Track_url;
                mSong.duration=Track_duration;
                mSong.imageUrl=Track_imageUrl;
                mSong.artists=ArtistIDArrayList;
                mSong.genres=GenresIDArrayList;
                mSong.artist_name=ArtistNameArrayList;
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
