package com.example.projectx.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    String [] playlistIDs;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        ImageView recommended = (ImageView) root.findViewById(R.id.recommended);
        ImageView likes = (ImageView) root.findViewById(R.id.likedTracks);
        ImageView mostPopular = (ImageView) root.findViewById(R.id.popular);
        ImageView newReleases = (ImageView) root.findViewById(R.id.newReleases);
        PlaylistAsyncTask getPlaylists = new PlaylistAsyncTask(getContext());
        getPlaylists.execute();
        recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PlayListFull.class));
            }
        });

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PlayListFull.class));
            }
        });

        mostPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PlayListFull.class));
            }
        });

        newReleases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PlayListFull.class));
            }
        });

        TextView about = (TextView) root.findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutActivity.class));
            }
        });


        return root;
    }
    private class PlaylistAsyncTask extends AsyncTask<String, Integer, String[]> {
        private Context context;
        public PlaylistAsyncTask (Context c){
            this.context = c;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/playlist/";
            JsonObjectRequest getPlaylists = new JsonObjectRequest(Request.Method.GET, url,
                    null, future, future);
            RequestQueue getUserQueue = Volley.newRequestQueue(this.context);
            getUserQueue.add(getPlaylists);
            try{
                JSONObject playlists =  future.get();
                JSONArray playlistsArray = playlists.getJSONArray("playlists");
                String [] playlistIds = new String[playlistsArray.length()];
                for (int i = 0; i<playlistsArray.length(); i++) {
                    playlistIds[i] = playlistsArray.getJSONObject(i).getString("_id");
                }
                playlistIDs = playlistIds;
                return playlistIds;

            }catch(ExecutionException e){
                e.printStackTrace();
            }
            catch(InterruptedException e ){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

        }

    }

}