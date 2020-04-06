package com.example.projectx.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.projectx.AboutActivity;
import com.example.projectx.PlayListFull;
import com.example.projectx.R;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

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
}
