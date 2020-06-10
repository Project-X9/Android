package com.example.projectx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.ui.home.ArtistRecyclerViewAdapter;
import com.example.projectx.ui.home.ArtistTrackRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ArtistActivity extends AppCompatActivity {
    public String artistUrl;
    ArrayList<String> trackNames = new ArrayList<String>();
    ArrayList<String> trackPictures = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ArtistId");
        artistUrl = "http://192.168.1.7:3000/api/v1/artist/artists/" + id;
        Log.e("Artist id is ", id);
        ArtistAsyncTask aat = new ArtistAsyncTask();
        aat.execute();
    }
//    public ArtistActivity (String passedId){
//        artistUrl = "http://192.168.1.7:3000/api/v1/artist/artists/" + passedId;
//    }


    private class ArtistAsyncTask extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected JSONObject doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, artistUrl, null,
                    future, future);
            RequestQueue signUpRq = Volley.newRequestQueue(getApplicationContext());
            signUpRq.add(request);
            try {
                JSONObject response = future.get();
                if (response.getString("status").equals("400")){
//                    makeToast("Bad request. Make sure you entered your full name.");
                }
                else if (response.getString("status").equals("500")) {
//                    makeToast("Seems the server down. Sorry!");
                }
                else {
                    return response;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            super.onPostExecute(result);
            Bitmap image = null;
            try {
                JSONObject data = result.getJSONObject("data").getJSONObject("artist");
                final URL url = new URL(data.getString("image"));
                TextView artistName = (TextView) findViewById(R.id.artist_page_name_tv);
                artistName.setText(data.getString("name"));
                TextView artistBio = (TextView) findViewById(R.id.artist_page_bio);
                artistBio.setText(data.getString("Bio"));
                JSONArray tracks = data.getJSONArray("tracks");

                for (int i = 0; i < tracks.length(); i++){
                    trackNames.add(tracks.getJSONObject(i).getString("name"));
                    trackPictures.add(tracks.getJSONObject(i).getString("imageUrl"));
                }
                LinearLayoutManager llm = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);

                RecyclerView recyclerView = findViewById(R.id.artist_page_recyclerview);

                Log.e("Debugging One song", trackNames.toString());
                Log.e("Pictures", trackPictures.toString());
                ArtistTrackRecyclerView adapter = new ArtistTrackRecyclerView(getBaseContext(), trackPictures, trackNames);
                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(adapter);
                new Thread() {

                    public void run() {
                        Bitmap image = null;
                        try {
                            image = BitmapFactory.decodeStream(url.openStream());
                            final Bitmap finalImage = image;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView layout = (ImageView) findViewById(R.id.artist_page_image);
                                    Drawable dr = new BitmapDrawable(finalImage);
                                    layout.setImageDrawable(dr);


                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }
                }.start();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
}

}