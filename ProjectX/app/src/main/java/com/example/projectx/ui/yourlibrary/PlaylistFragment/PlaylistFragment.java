package com.example.projectx.ui.yourlibrary.PlaylistFragment;

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
import com.example.projectx.UserActivity.UserData;
import com.example.projectx.playlist.NameRenamePlaylist;
import com.example.projectx.playlist.PlayListFull;
import com.example.projectx.ui.yourlibrary.ArtistFragment.ArtistData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment implements PlaylistFragmentAdapter.onPlaylistListner {
    private final String SERVER_URL = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/playlist/";
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;
    static ArrayList<UserData> onlineData;
    static ArrayList<String> UserId;
    private static Context context;
    static String ClickedPlaylistId;



    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_playlist, container, false);

        mRecyclerView = view.findViewById(R.id.playlists_rv);
        mLayoutManager =  new LinearLayoutManager(getActivity());
        context= getActivity();
        setCreatePlaylist();
        FetchPlaylistFragmentData fetchPlaylistFragmentData = new FetchPlaylistFragmentData(this);
        fetchPlaylistFragmentData.setURL(SERVER_URL);
        fetchPlaylistFragmentData.execute();
        return view;
    }

    public void setCreatePlaylist(){
        onlineData = new ArrayList<>();
        onlineData.add(new UserData(R.drawable.ic_add,"To Create Playlist Click Here","",""));
    }
    public  void updatePlaylists() {
        mAdapter = new PlaylistFragmentAdapter(onlineData,context,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onPlaylistClick(int position) {
        if (onlineData.get(position).getId()== null){
            Toast.makeText(getContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            ClickedPlaylistId = onlineData.get(position).getId();
        }
        if(position==0){
            Intent i = new Intent(getContext(), NameRenamePlaylist.class);
            Bundle extras = new Bundle();
            extras.putString("UserId", UserId.get(0));
            i.putExtras(extras);
            startActivity(i);
        }else {
            Intent i = new Intent(getContext(), PlayListFull.class);
            Bundle extras = new Bundle();
            extras.putString("PlaylistIDs", ClickedPlaylistId);
            i.putExtras(extras);
            startActivity(i);
        }

    }


    public void setUserId(JSONObject userId) {
        try {
            UserId=new ArrayList<>();
            UserId.add(userId.getJSONObject("data").getJSONObject("user").getString("_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
