package com.example.projectx.playlist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.R;
import com.example.projectx.RecyclerTouchListener;
import com.example.projectx.Song;
import com.example.projectx.SongAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PlaylistEdit extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView playlistName,topPlaylistName;
    static ArrayList<Song> songArrayList=new ArrayList<>();
    static String[] SongIDStringArray;
    static String ClickedSongId,mPlaylistName,mPlaylistServerURL,mPlaylistId;
    static ImageView mPlaylistImage,mRenamePlaylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_edit);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String temp1 = b.getString("PlaylistName");
        String temp2 = b.getString("URL");
        String temp3 = b.getString("PlaylistId");
        mPlaylistName = temp1;
        mPlaylistServerURL = temp2;
        mPlaylistId=temp3;

        playlistName = findViewById(R.id.playlistName_tv);
        mRecyclerView = findViewById(R.id.songlists_list_rv);
        mRenamePlaylist = findViewById(R.id.editPlaylist_ibt);
        topPlaylistName=findViewById(R.id.top_playlistName_tv);

        FetchPlaylist fetchPlaylist = new FetchPlaylist();
        fetchPlaylist.execute();

        Listeners();

    }


    public void addsongs (View view){

    }

    public void renamePlaylist(View view){

        Intent i = new Intent(getApplicationContext(), NameRenamePlaylist.class);
        Bundle extras = new Bundle();
        extras.putString("PlaylistId",mPlaylistId);
        extras.putString("Create", "0");
        i.putExtras(extras);
        startActivity(i);

    }

    public void returnBack(View v) {
        finish();
    }

    private void Listeners() {
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Delete Track"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    deleteTrack(position);
                }
            }
        });
        builder.show();
    }

    private void deleteTrack(int position) {

    }

    private void  setupRecyclerView(ArrayList<Song> songArrayList) {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PlaylistEditAdapter(songArrayList, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
    }



    private class FetchPlaylist extends AsyncTask<String, String, String > {

        String mNamePlaylist;


        @Override
        protected String  doInBackground(String... strings) {
            String string = Playlist(mNamePlaylist);
            return string;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            playlistName.setText(s);
            topPlaylistName.setText(s);
            SongIDStringArray=new String[songArrayList.size()];
            for (int i = 0; i < songArrayList.size(); i++) {
                SongIDStringArray[i] = songArrayList.get(i).id;
            }
            setupRecyclerView(songArrayList);
            mRenamePlaylist.setImageResource(R.drawable.edit_playlist);
        }

    }

    public String Playlist(String mNamePlaylist) {
        mRenamePlaylist.setImageResource(R.drawable.edit_playlist_pressed); //disable the like button until data is fetched.
        songArrayList = new ArrayList<>();
        ArrayList<JSONObject> jsonObjectArray = new ArrayList<>();

        final String PlayList_URL = mPlaylistServerURL;
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


        return  mNamePlaylist ;

    }

}
