package com.example.projectx.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import com.example.projectx.R;
import com.example.projectx.ui.home.ArtistRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();
    private ArrayList<String> Ids = new ArrayList<>();
    private ArrayList<AsyncTask> threads = new ArrayList<AsyncTask> ();
    SearchRecyclerViewAdapter adapter;
    Switch album;
    static ProgressDialog progDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        EditText searchBar = root.findViewById(R.id.search_page_bar);
        album = root.findViewById(R.id.album);

        album.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    album.setText("Turn Off To search for tracks");

                } else {
                    album.setText("Turn on to search for albums");
                }
            }});
        searchBar.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                for (AsyncTask e : threads){
                    e.cancel(true);
                }
                names.clear();
                urls.clear();
                Ids.clear();
                types.clear();

                if (!mEdit.toString().equals("")) {
                    Log.e("Query", "query is " + mEdit.toString());
                    names.clear();
                    urls.clear();
                    Ids.clear();
                    types.clear();
                    if (album.isChecked()){
                        SearchAlbumAsyncTask sat = new SearchAlbumAsyncTask(getContext(), mEdit.toString());

                        sat.execute();
                        threads.add(sat);
                    }
                    else{
                        SearchAsyncTask sat = new SearchAsyncTask(getContext(), mEdit.toString());

                        sat.execute();
                        threads.add(sat);
                    }

                }
                else{
                    Log.e("tag", "ArrayList is now" + names);
                    names.clear();
                    urls.clear();
                    Ids.clear();
                    types.clear();
                    LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    RecyclerView searchRecyclerView = getView().findViewById(R.id.search_recyclerView);
                    searchRecyclerView.setLayoutManager(llm);

                    adapter = new SearchRecyclerViewAdapter(getContext(), urls, names, Ids, 0);
                    searchRecyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        return root;
    }

    private class SearchAsyncTask extends AsyncTask<String, Integer, Void> {
        private Context context;
        private String query;
        String[] trackIds;
        public SearchAsyncTask(Context c, String query) {
            this.context = c;this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://192.168.1.7:3000/api/v1/search/track?q=" + query;
            JsonObjectRequest artistsRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, future, future);
            RequestQueue getUserQueue = Volley.newRequestQueue(this.context);
            getUserQueue.add(artistsRequest);
            try {
                Log.e("url", url);
                JSONObject tracks = future.get();
                JSONArray tracksArray = tracks.getJSONArray("tracks");
                 trackIds = new String[tracks.length()];
                for (int i = 0; i < tracksArray.length(); i++) {
                    if(tracksArray.getJSONObject(i).getString("name").toLowerCase().contains(query.toLowerCase())|| tracksArray.getJSONObject(i).getString("name").toUpperCase().contains(query.toUpperCase())){
                        names.add(tracksArray.getJSONObject(i).getString("name"));
                       // types.add(tracksArray.getJSONObject(i).getString("Type"));
                        urls.add(tracksArray.getJSONObject(i).getString("imageUrl"));
                        Ids.add(tracksArray.getJSONObject(i).getString("_id"));
                    }
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
            LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

            RecyclerView searchRecyclerView = getView().findViewById(R.id.search_recyclerView);
            searchRecyclerView.setLayoutManager(llm);


            adapter = new SearchRecyclerViewAdapter(getContext(), urls, names, Ids, 0);
            searchRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.e("tag", "ArrayList is now" + names);
        }
    }


    private class SearchAlbumAsyncTask extends AsyncTask<String, Integer, Void> {
        private Context context;
        private String query;
        String[] trackIds;
        public SearchAlbumAsyncTask(Context c, String query) {
            this.context = c;this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://192.168.1.7:3000/api/v1/search/Album?q=" + query;
            JsonObjectRequest artistsRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, future, future);
            RequestQueue getUserQueue = Volley.newRequestQueue(this.context);
            getUserQueue.add(artistsRequest);
            try {
                Log.e("url", url);
                JSONObject tracks = future.get();
                JSONArray tracksArray = tracks.getJSONArray("albums");
                trackIds = new String[tracks.length()];
                for (int i = 0; i < tracksArray.length(); i++) {
                    if(tracksArray.getJSONObject(i).getString("name").toLowerCase().contains(query.toLowerCase())|| tracksArray.getJSONObject(i).getString("name").toUpperCase().contains(query.toUpperCase())){
                        names.add(tracksArray.getJSONObject(i).getString("name"));
                        // types.add(tracksArray.getJSONObject(i).getString("Type"));
                        urls.add(tracksArray.getJSONObject(i).getString("image"));
                        Ids.add(tracksArray.getJSONObject(i).getString("_id"));
                    }
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
            LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);


            RecyclerView searchRecyclerView = getView().findViewById(R.id.search_recyclerView);
            searchRecyclerView.setLayoutManager(llm);

            adapter = new SearchRecyclerViewAdapter(getContext(), urls, names, Ids, 1);
            searchRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.e("tag", "ArrayList is now" + names);
        }
    }





}
