package com.example.projectx.ui.yourlibrary.AlbumsFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

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
import java.util.concurrent.ExecutionException;

public class AlbumActivity extends AppCompatActivity implements SongAdapter.onSongListner {

    String albumNameKey,Album_URL;
    private final String MOCK_URL = "http://www.mocky.io/v2/5edbde6d32000009a05d2822";
    private final String SERVER ="http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/album/";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView albumName, topAlbumName;
    static ArrayList<Song> songArrayList=new ArrayList<>();
    static String[] SongIDStringArray;
    static String ClickedSongId;
    static ImageView mAlbumImage;
    private ImageButton likeAlbumButton,shareButton;
    private boolean albumLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String temp = b.getString("AlbumID");
        albumNameKey = temp;
        Album_URL = SERVER + albumNameKey;
        albumLiked =false;

        albumName = findViewById(R.id.albumName_tv);
        mRecyclerView = findViewById(R.id.songlists_list_rv);
        likeAlbumButton = findViewById(R.id.likeButton2_ib);
        shareButton = findViewById(R.id.sharePlaylist_ibt);
        topAlbumName=findViewById(R.id.top_albumName_tv);
        mAlbumImage = findViewById(R.id.album_image);
        likeAlbumButton.setImageResource(R.drawable.like_song_loading); //disable the like button until data is fetched.
        shareButton.setImageResource(R.drawable.share_song_pressed); //disable the share button until data is fetched.
        FetchAlbumList fetchAlbumsFragmentData = new FetchAlbumList();
        fetchAlbumsFragmentData.execute();

    }


    public void returnBack(View v) {
        finish();
    }

    /* this is used for sharing playlist link*/
    public void shareButtonPressed(View V) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String albumTitle = this.albumName.getText().toString();
        String shareMessage = "Here's a Album for you... " + albumTitle;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage + "\n" + Album_URL);
        startActivity(Intent.createChooser(shareIntent, "Share Using..."));
    }

    public void likeButtonPressed(View V) {

//        if (albumLiked)
//        {
//            likeAlbumButton.setImageResource(R.drawable.like_song);
//            albumLiked = false;
//
//        } else {
//            likeAlbumButton.setImageResource(R.drawable.dislike_song);
//            albumLiked = true;
//
//        }
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

    private class FetchAlbumList_Mock extends AsyncTask{
        AlbumActivity albumActivity;
        java.net.URL URL;
        private String data = "";
        private JSONObject dataObj;

        FetchAlbumList_Mock(AlbumActivity albumActivity) {
            this.albumActivity = albumActivity;
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
            fillRecyclerView(albumActivity.albumNameKey);
            likeAlbumButton.setImageResource(R.drawable.dislike_song);
        }

        private void fillRecyclerView(String albumID) {
            try {
                String albumPhoto = "",albumName="";
                JSONArray albumsJsonArray = dataObj.getJSONArray("Albums");
//                songArrayList = new ArrayList<>();
                for (int i = 0; i < albumsJsonArray.length(); i++) {
                    JSONObject album = albumsJsonArray.getJSONObject(i);
                    if(album.getString("name").equals(albumID)) {
                         albumPhoto = album.getString("photo");
                         albumName = album.getString("name");
                        JSONArray tracksJsonArray = album.getJSONArray("tracks");
                        for (int s=0;s< tracksJsonArray.length();s++) {
                            JSONObject track = tracksJsonArray.getJSONObject(s);
                            Song mSong = new Song();
                            ArrayList<String> artistNameArrayList = new ArrayList<>();
                            artistNameArrayList.add("");
                            mSong.name=track.getString("name");
                            mSong.imageUrl=track.getString("photo");
                            mSong.url=track.getString("url");
                            mSong.artist_name=artistNameArrayList;
                            albumActivity.songArrayList.add(mSong);
                        }
                    }
                }
                albumActivity.updateAlbums(albumPhoto,albumName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setURL(String url) {
            try {
                URL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }


    }


    private void updateAlbums( String albumPhoto,String albumName) {
        this.albumName.setText(albumName);
        Picasso.with(getApplicationContext())
                .load(albumPhoto)
                .into(mAlbumImage);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SongAdapter(songArrayList, getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }


    private class FetchAlbumList extends AsyncTask<String, String, String []> {

        String mNameAlbum, mAlbumImageURL;


        @Override
        protected String [] doInBackground(String... strings) {
            String [] string = Album(mNameAlbum, mAlbumImageURL);
            return string;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String [] s) {
            super.onPostExecute(s);
            albumName.setText(s[0]);
            topAlbumName.setText(s[0]);
            mAlbumImageURL =s[1];
            Picasso.with(getApplicationContext())
                    .load(mAlbumImageURL)
                    .into(mAlbumImage);
            SongIDStringArray=new String[songArrayList.size()];
            for (int i = 0; i < songArrayList.size(); i++) {
                SongIDStringArray[i] = songArrayList.get(i).id;
            }
//            mAdapter.notifyDataSetChanged();
            setupRecyclerView(songArrayList);
            likeAlbumButton.setImageResource(R.drawable.dislike_song);
            shareButton.setImageResource(R.drawable.share_song);
        }

    }

    private void  setupRecyclerView(ArrayList<Song> songArrayList) {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SongAdapter(songArrayList, getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public String [] Album(String mNameAlbum, String mAlbumImageURL ) {
        likeAlbumButton.setImageResource(R.drawable.like_song_loading); //disable the like button until data is fetched.
        shareButton.setImageResource(R.drawable.share_song_pressed); //disable the share button until data is fetched.
        songArrayList = new ArrayList<>();
        ArrayList<JSONObject> jsonObjectArray = new ArrayList<>();


        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Album_URL, null, future, future);
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(request);

        try {
            JSONObject response = future.get();
            Log.e("debugging", albumNameKey);
            Log.e("debugging", response.toString());
            JSONObject data = response.getJSONObject("data");
            JSONObject dPlaylist = data.getJSONObject("album");
            mNameAlbum = dPlaylist.getString("name");
            mAlbumImageURL = dPlaylist.getString("image");
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

        String [] result={mNameAlbum,mAlbumImageURL};
        return result ;

    }


}
