package com.example.projectx.ui.yourlibrary.AlbumsFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectx.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsFragment extends Fragment implements AlbumsFragmentAdapter.onAlbumListner {

    private final String MOCK_URL = "http://www.mocky.io/v2/5edbde6d32000009a05d2822";
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;
    static ArrayList<AlbumsData> onlineData;
    private static Context context;
    static String ClickedAlbumId;

    public AlbumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        mRecyclerView = view.findViewById(R.id.albums_rv);
        mLayoutManager =  new LinearLayoutManager(getActivity());
        context= getActivity();
        FetchAlbumsFragmentData fetchAlbumsFragmentData = new FetchAlbumsFragmentData(this);
        fetchAlbumsFragmentData.setURL(MOCK_URL);
        fetchAlbumsFragmentData.execute();
        return view;
    }

    public void updateAlbums() {
        mAdapter = new AlbumsFragmentAdapter(onlineData,context,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAlbumClick(int position) {
        if (onlineData.get(position).getName()== null){
            Toast.makeText(getContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            ClickedAlbumId = onlineData.get(position).getId();
        }
        Intent i = new Intent(getContext(), AlbumActivity.class);
        Bundle extras = new Bundle();
        extras.putString("AlbumID", ClickedAlbumId);
        i.putExtras(extras);
        startActivity(i);

    }

    public void setUserAlbum(JSONObject myUser) {
        try {
            JSONArray albumsJsonArray = myUser.getJSONObject("data").getJSONObject("user").getJSONArray("albums");
            onlineData = new ArrayList<>();
            for (int i = 0; i < albumsJsonArray.length(); i++) {
                JSONObject album = albumsJsonArray.getJSONObject(i);
                onlineData.add(new AlbumsData(album.getString("name"), album.getString("image"),album.getString("_id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
