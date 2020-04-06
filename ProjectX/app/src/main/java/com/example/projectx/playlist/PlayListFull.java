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
import com.example.projectx.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PlayListFull extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView playlistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FetchPlaylist fetchPlaylist = new FetchPlaylist();
        fetchPlaylist.execute();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_list_full);
        playlistName = findViewById(R.id.playlistName_tv);




        ArrayList<SongList> exampleList = new ArrayList<>();
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Venom","Eminem" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Angel","theweeknd" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"At my best ","MGK" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Stitches","Shawn Mends" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"macarena","Tyga" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Venom","Eminem" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Angel","theweeknd" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"At my best ","MGK" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Stitches","Shawn Mends" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"macarena","Tyga" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Venom","Eminem" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Angel","theweeknd" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"At my best ","MGK" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Stitches","Shawn Mends" ));
//        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"macarena","Tyga" ));

        mRecyclerView = findViewById(R.id.recyclerView);

        mLayoutManager =  new LinearLayoutManager(this);
        mAdapter = new SongAdapter(exampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void returenBack(View v){
        Intent intent = new Intent(this, PlaylistEmpty.class);
        startActivity(intent);
    }

    private class FetchPlaylist extends AsyncTask<String, String, String> {

         String mNamePlaylist;


        @Override
        protected String doInBackground(String... strings) {
            String s = PlaylistName(mNamePlaylist);
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
        }
    }

    public String PlaylistName(String mNamePlaylist ) {
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
//            int trackid = Dplaylist.getInt("trackIds");
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
