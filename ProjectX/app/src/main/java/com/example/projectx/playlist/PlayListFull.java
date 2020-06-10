package com.example.projectx.playlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.MusicPlayer;
import com.example.projectx.R;
import com.example.projectx.Song;
import com.example.projectx.SongAdapter;
import com.example.projectx.ui.yourlibrary.PlaylistFragment.FetchPlaylistFragmentData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PlayListFull extends AppCompatActivity implements SongAdapter.onSongListner{

    public static String stringPlaylistName;
    private boolean playlistLiked;
    private ImageButton likePlaylistButton,shareButton;
    String PLAYLIST_FETCH_SERVER = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/playlist/";
    //http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com
    String playlistId = "5e8741dadfdb0a35d429a128";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView playlistName,topPlaylistName;
    static ArrayList<Song> songArrayList=new ArrayList<>();
    static String[] SongIDStringArray;
    static String ClickedSongId;
    static ImageView mPlaylistImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_full);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String temp = b.getString("PlaylistIDs");
        playlistId = temp;
        playlistLiked=false;

        FetchPlaylist fetchPlaylist = new FetchPlaylist();
        fetchPlaylist.execute();

        playlistName = findViewById(R.id.playlistName_tv);
        mRecyclerView = findViewById(R.id.songlists_list_rv);
        likePlaylistButton = findViewById(R.id.likeButton2_ib);
        shareButton = findViewById(R.id.sharePlaylist_ibt);
        topPlaylistName=findViewById(R.id.top_playlistName_tv);
        mPlaylistImage = findViewById(R.id.playlist_image);


    }




    /* this is used for sharing playlist link*/
    public void shareButtonPressed(View V) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String playlistTitle = this.playlistName.getText().toString();
        String shareMessage = "Here's a playlist for you... " + playlistTitle;
        String PlayList_URL = PLAYLIST_FETCH_SERVER + playlistId;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage + "\n" + PlayList_URL);
        startActivity(Intent.createChooser(shareIntent, "Share Using..."));
    }

    public void likeButtonPressed(View V) {

        if (playlistLiked)
        {
            likePlaylistButton.setImageResource(R.drawable.like_song);
            playlistLiked = false;

        } else {
            likePlaylistButton.setImageResource(R.drawable.dislike_song);
            playlistLiked = true;

        }
    }



    public void returnBack(View v) {
        finish();
    }

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

    public void openEditPlaylist(View view){
        Intent i = new Intent(this, PlaylistEdit.class);
        Bundle extras = new Bundle();
        extras.putString("PlaylistName",playlistName.toString());
        extras.putString("URL",PLAYLIST_FETCH_SERVER+playlistId);
        extras.putString("PlaylistId",playlistId);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    private void  setupRecyclerView(ArrayList<Song> songArrayList) {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SongAdapter(songArrayList, getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }



    private class FetchPlaylist extends AsyncTask<String, String, String []> {

        String mNamePlaylist,mPlaylistImageURL;


        @Override
        protected String [] doInBackground(String... strings) {
            String [] string = Playlist(mNamePlaylist,mPlaylistImageURL);
            return string;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String [] s) {
            super.onPostExecute(s);
            playlistName.setText(s[0]);
            topPlaylistName.setText(s[0]);
            mPlaylistImageURL=s[1];
            Picasso.with(getApplicationContext())
                    .load(mPlaylistImageURL)
                    .into(mPlaylistImage);
            SongIDStringArray=new String[songArrayList.size()];
            for (int i = 0; i < songArrayList.size(); i++) {
                SongIDStringArray[i] = songArrayList.get(i).id;
            }
//            mAdapter.notifyDataSetChanged();
            setupRecyclerView(songArrayList);
            likePlaylistButton.setImageResource(R.drawable.like_song);
            shareButton.setImageResource(R.drawable.share_song);
        }

    }

    public String [] Playlist(String mNamePlaylist,String mPlaylistImageURL ) {
//        likePlaylistButton.setImageResource(R.drawable.like_song_loading); //disable the like button until data is fetched.
//        shareButton.setImageResource(R.drawable.share_song_pressed); //disable the share button until data is fetched.
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
            mPlaylistImageURL = dPlaylist.getString("image");
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

        String [] result={mNamePlaylist,mPlaylistImageURL};
        return result ;

    }

}
