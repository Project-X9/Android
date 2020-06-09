package com.example.projectx.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.AboutActivity;
import com.example.projectx.playlist.PlayListFull;
import com.example.projectx.R;
import com.example.projectx.authentication.AuthenticationPage;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    String[] playlistIDs = new String[4];

    //For Artists
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();

    //For Albums
    private ArrayList<String> albumNames = new ArrayList<>();
    private ArrayList<String> albumUrls = new ArrayList<>();

    //For Genres
    private ArrayList<String> genreNames = new ArrayList<>();
    private ArrayList<String> genreUrls = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        PlaylistAsyncTask pat = new PlaylistAsyncTask(getContext());
        pat.execute();
        AlbumAsyncTask aat = new AlbumAsyncTask(getContext());
        aat.execute();
        GenreAsyncTask gat = new GenreAsyncTask(getContext());
        gat.execute();
        return root;
    }


    private class PlaylistAsyncTask extends AsyncTask<String, Integer, Void> {
        private Context context;

        public PlaylistAsyncTask(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://www.mocky.io/v2/5edbdfbc320000b5ad5d282a";
            JsonObjectRequest artistsRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, future, future);
            RequestQueue getUserQueue = Volley.newRequestQueue(this.context);
            getUserQueue.add(artistsRequest);
            try {
                JSONObject artists = future.get();
                JSONArray artistsArray = artists.getJSONArray("Artists");

                for (int i = 0; i < artistsArray.length(); i++) {
                    names.add(artistsArray.getJSONObject(i).getString("Name"));
                    urls.add(artistsArray.getJSONObject(i).getString("Photo"));
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

            RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(llm);

            ArtistRecyclerViewAdapter adapter = new ArtistRecyclerViewAdapter(getContext(), names, urls);
            recyclerView.setAdapter(adapter);
        }

    }

    private class AlbumAsyncTask extends AsyncTask<String, Integer, Void> {
        private Context context;

        public AlbumAsyncTask(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://www.mocky.io/v2/5edbde6d32000009a05d2822";
            JsonObjectRequest artistsRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, future, future);
            RequestQueue getUserQueue = Volley.newRequestQueue(this.context);
            getUserQueue.add(artistsRequest);
            try {
                JSONObject artists = future.get();
                JSONArray artistsArray = artists.getJSONArray("Albums");

                for (int i = 0; i < artistsArray.length(); i++) {
                    albumNames.add(artistsArray.getJSONObject(i).getString("name"));
                    albumUrls.add(artistsArray.getJSONObject(i).getString("photo"));
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

            RecyclerView albumRecyclerView = getView().findViewById(R.id.albums_recyclerView);
            albumRecyclerView.setLayoutManager(llm);

            ArtistRecyclerViewAdapter adapter = new ArtistRecyclerViewAdapter(getContext(), albumNames, albumUrls);
            albumRecyclerView.setAdapter(adapter);
        }

    }


    private class GenreAsyncTask extends AsyncTask<String, Integer, Void> {
        private Context context;

        public GenreAsyncTask(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://192.168.1.7:3000/api/v1/browse/categories";
            JsonObjectRequest artistsRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, future, future);
            RequestQueue getUserQueue = Volley.newRequestQueue(this.context);
            getUserQueue.add(artistsRequest);
            try {
                JSONObject genres = future.get();
                JSONArray genresArray = genres
                        .getJSONObject("data")
                        .getJSONArray("Categories");

                for (int i = 0; i < genresArray.length(); i++) {
                    genreNames.add(genresArray.getJSONObject(i).getString("name"));
                    genreUrls.add(genresArray.getJSONObject(i).getString("icon"));
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

            RecyclerView albumRecyclerView = getView().findViewById(R.id.genres_recyclerView);
            albumRecyclerView.setLayoutManager(llm);

            ArtistRecyclerViewAdapter adapter = new ArtistRecyclerViewAdapter(getContext(), genreNames, genreUrls);
            albumRecyclerView.setAdapter(adapter);
        }

    }



}