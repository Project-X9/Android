package com.example.projectx.ui.dashboard;

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
import android.widget.EditText;
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
import com.example.projectx.ArtistFragment.ArtistFragment;
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
    SearchRecyclerViewAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        EditText searchBar = root.findViewById(R.id.search_page_bar);

        searchBar.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                if (!mEdit.toString().isEmpty()) {
                    Log.e("Query", "query is " + mEdit.toString());
                    names.clear();
                    urls.clear();
                    types.clear();
                    SearchAsyncTask sat = new SearchAsyncTask(getContext(), mEdit.toString());
                    sat.execute();
                }
                else{
                    Log.e("tag", "ArrayList is now" + names);
                    names.clear();
                    urls.clear();
                    types.clear();
                    LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    RecyclerView searchRecyclerView = getView().findViewById(R.id.search_recyclerView);
                    searchRecyclerView.setLayoutManager(llm);

                    adapter = new SearchRecyclerViewAdapter(getContext(), urls, names, types);
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
            String url = "https://run.mocky.io/v3/63117a42-c771-4c10-a9c3-5fa974a77f14";
            JsonObjectRequest artistsRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, future, future);
            RequestQueue getUserQueue = Volley.newRequestQueue(this.context);
            getUserQueue.add(artistsRequest);
            try {
                JSONObject artists = future.get();
                JSONArray artistsArray = artists.getJSONArray("items");
                for (int i = 0; i < artistsArray.length(); i++) {
                    if(artistsArray.getJSONObject(i).getString("Name").toLowerCase().contains(query)|| artistsArray.getJSONObject(i).getString("Name").toUpperCase().contains(query.toUpperCase())){
                        names.add(artistsArray.getJSONObject(i).getString("Name"));
                        types.add(artistsArray.getJSONObject(i).getString("Type"));
                        urls.add(artistsArray.getJSONObject(i).getString("Picture"));
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

            adapter = new SearchRecyclerViewAdapter(getContext(), urls, names, types);
            searchRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.e("tag", "ArrayList is now" + names);
        }
    }





}
