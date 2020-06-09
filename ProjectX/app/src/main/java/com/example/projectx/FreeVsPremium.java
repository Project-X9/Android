package com.example.projectx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FreeVsPremium extends Fragment {

    private TextView Free, Premium;

    public FreeVsPremium() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_vs_premium, container, false);
        Free = view.findViewById(R.id.free_demo_tv);
        Premium = view.findViewById(R.id.premium_demo_tv);
        Free.setText(getArguments().getString("Free"));
        Premium.setText(getArguments().getString("Premium"));

        return view;
    }
}