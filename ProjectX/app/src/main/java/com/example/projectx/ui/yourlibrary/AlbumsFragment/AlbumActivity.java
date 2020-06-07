package com.example.projectx.ui.yourlibrary.AlbumsFragment;

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

public class AlbumActivity extends AppCompatActivity implements SongAdapter.onSongListner {

    String albumNameKey;
    private final String MOCK_URL = "http://www.mocky.io/v2/5edbde6d32000009a05d2822";
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
        String temp = b.getString("AlbumName");
        albumNameKey = temp;
        albumLiked =false;

        albumName = findViewById(R.id.albumName_tv);
        mRecyclerView = findViewById(R.id.songlists_list_rv);
        likeAlbumButton = findViewById(R.id.likeButton2_ib);
        shareButton = findViewById(R.id.sharePlaylist_ibt);
        topAlbumName=findViewById(R.id.top_albumName_tv);
        mAlbumImage = findViewById(R.id.album_image);

        FetchAlbumList fetchAlbumsFragmentData = new FetchAlbumList(this);
        fetchAlbumsFragmentData.setURL(MOCK_URL);
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
        String Album_URL = MOCK_URL + albumNameKey;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage + "\n" + Album_URL);
        startActivity(Intent.createChooser(shareIntent, "Share Using..."));
    }

    public void likeButtonPressed(View V) {

        if (albumLiked)
        {
            likeAlbumButton.setImageResource(R.drawable.like_song);
            albumLiked = false;

        } else {
            likeAlbumButton.setImageResource(R.drawable.dislike_song);
            albumLiked = true;

        }
    }

    @Override
    public void onSongClick(int position) {

    }

    private class FetchAlbumList extends AsyncTask{
        AlbumActivity albumActivity;
        java.net.URL URL;
        private String data = "";
        private JSONObject dataObj;

        FetchAlbumList(AlbumActivity albumActivity) {
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

}
