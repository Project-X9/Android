package com.example.projectx.playlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.R;
import com.example.projectx.RecyclerTouchListener;
import com.example.projectx.Song;
import com.example.projectx.SongAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddSong extends AppCompatActivity {

    String TRACKS_FETCH_SERVER = "http://192.168.43.253:3000/api/v1/track";
    String PLAYLIST_SERVER = "http://192.168.43.253:3000/api/v1/playlist";
    private RecyclerView mRecyclerView;
    private AddSongAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView mSearchView;
    private Context context;
    ArrayList<Song> songArrayList=new ArrayList<>();
    static String[] SongIDStringArray;
    static String ClickedSongId;
    JSONObject result;
    String PlaylistName,PlaylistId,mPlaylistURL,Tracks="tracks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        mRecyclerView = findViewById(R.id.addSong_list_rv);
        mSearchView = findViewById(R.id.search_field);
        context=getApplicationContext();
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String PlaylistNameR = b.getString("PlaylistName");
        String mPLAYLISTidR = b.getString("PlaylistId");
        String playlistURLR = b.getString("URL");
        PlaylistName = PlaylistNameR;
        PlaylistId = mPLAYLISTidR;
        mPlaylistURL = playlistURLR;
        FetchTracks fetchTracks = new FetchTracks();
        fetchTracks.execute();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                String trackId = songArrayList.get(position).id;
                addTrackAsyncTask addTrackAsyncTask = new addTrackAsyncTask();
                addTrackAsyncTask.execute(trackId);
                Toast.makeText(AddSong.this,"Track added to Playlist", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    public void returnBack(View v) {
        Intent i = new Intent(this, PlaylistEdit.class);
        Bundle extras = new Bundle();
        extras.putString("PlaylistId",PlaylistId);
        extras.putString("URL",mPlaylistURL);
        extras.putString("PlaylistName", PlaylistName);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    private void  setupRecyclerView(ArrayList<Song> songArrayList) {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AddSongAdapter(songArrayList, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    private class FetchTracks extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Tracks();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            SongIDStringArray=new String[songArrayList.size()];
            for (int i = 0; i < songArrayList.size(); i++) {
                SongIDStringArray[i] = songArrayList.get(i).id;
            }
            setupRecyclerView(songArrayList);

        }

    }

    public void Tracks() {
        songArrayList = new ArrayList<>();
        ArrayList<JSONObject> jsonObjectArray = new ArrayList<>();

        final String PlayList_URL = TRACKS_FETCH_SERVER;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PlayList_URL, null, future, future);
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(request);

        try {
            JSONObject response = future.get();
            JSONObject data = response.getJSONObject("data");
            JSONArray Tracks = data.getJSONArray("tracks");
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

                Song mSong = new Song();
                mSong.id=trackId;
                mSong.description=trackDescription;
                mSong.name=trackNames;
                mSong.playcount=trackPlaycount;
                mSong.url=trackUrl;
                mSong.duration=trackDuration;
                mSong.imageUrl=trackImageUrl;
                mSong.CreateImage=R.drawable.ic_add;
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
    }

    private class addTrackAsyncTask extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(String... strings) {
            try {
                result = onSubmitAdd(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            try {
                Log.e("reached", "postexecute");
                Log.e("result", result.toString());
                if (result.getString("status").equals("success")) {
                }
                else
                {
                    Log.e("Post Data ","Response Failed");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private JSONObject onSubmitAdd(String data) throws JSONException {
        String URL=PLAYLIST_SERVER+"/"+Tracks+"/"+PlaylistId+"/"+data;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject addJsonInfo = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, URL,
                addJsonInfo, future, future);
        RequestQueue createPlaylistReq = Volley.newRequestQueue(this.context);
        createPlaylistReq.add(request);

        try {
            JSONObject response = future.get();
            Log.e("post result", response.toString());
            if (response == null) {
                Log.e("Response","error");
                response = new JSONObject();
                response.put("status", "Couldn't reach server");
                return response;
            }else {
                response.put("status", "success");
                return response;
            }

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}