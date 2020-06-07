package com.example.projectx.ui.yourlibrary.ArtistFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectx.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtisttFragment extends Fragment implements ArtistFragmentAdapter.onArtistListner {

    private final String MOCK_URL = "http://www.mocky.io/v2/5edbdfbc320000b5ad5d282a";
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;
    static ArrayList<ArtistData> onlineData;
    private static Context context;
    static String ClickedArtistId;

    public ArtisttFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_artistt, container, false);

        mRecyclerView = view.findViewById(R.id.artist_rv);
        mLayoutManager =  new LinearLayoutManager(getActivity());
        context= getActivity();
        FetchArtistFragmentData fetchArtistFragmentData = new FetchArtistFragmentData(this);
        fetchArtistFragmentData.setURL(MOCK_URL);
        fetchArtistFragmentData.execute();
        return view;
    }

    public  void updateArtist() {
        mAdapter = new ArtistFragmentAdapter(onlineData,context,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onArtistClick(int position) {

    }
}
