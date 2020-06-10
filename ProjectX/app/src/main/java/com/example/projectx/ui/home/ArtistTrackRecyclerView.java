package com.example.projectx.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectx.Artist.ArtistTracksAdapter;
import com.example.projectx.Artist.ArtistTracksList;
import com.example.projectx.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistTrackRecyclerView  extends RecyclerView.Adapter<ArtistTrackRecyclerView.TrackViewHolder> {
    private ArrayList<String> imageUrls;
    private ArrayList<String> names;
    private Context mContext;

    public ArtistTrackRecyclerView (Context context,ArrayList<String> images, ArrayList<String> artistNames){
        mContext = context;
        imageUrls = images;
        Log.e("Image Urls", images.toString());
        names = artistNames;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_song_list, parent, false);
        return new TrackViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(imageUrls.get(position))
                .into(holder.trackImage);
        Log.e("Loops", String.valueOf(position));
        holder.trackName.setText(names.get(position));
        holder.trackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Album clicked.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        public ImageView trackImage;
        public TextView trackName;
        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            trackImage = itemView.findViewById(R.id.song_iv);
            trackName = itemView.findViewById(R.id.song_name_tv);

        }
    }
}
