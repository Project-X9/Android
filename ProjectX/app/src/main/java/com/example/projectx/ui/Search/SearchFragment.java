package com.example.projectx.ui.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.projectx.ArtistFragment.ArtistFragment;
import com.example.projectx.R;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Button openArtist = (Button) root.findViewById(R.id.openArtist);
        openArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                String url = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/artist/artists/5e877b8fae42032b7c867feb";
                manager.beginTransaction().replace(R.id.nav_host_fragment, new ArtistFragment(url)).commit();
            }
        });
        final TextView textView = root.findViewById(R.id.text_dashboard);
        /*searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        }); */
        return root;
    }
}
