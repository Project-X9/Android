package com.example.projectx.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectx.MusicPlayer;
import com.example.projectx.R;

import java.util.ArrayList;

public class ArtistTrackRecyclerView  extends RecyclerView.Adapter<ArtistTrackRecyclerView.TrackViewHolder> {
    private ArrayList<String> imageUrls;
    private ArrayList<String> names;
    private ArrayList<String> trackid;
    private Context mContext;

    public ArtistTrackRecyclerView (Context context,ArrayList<String> images, ArrayList<String> artistNames, ArrayList<String> ids){
        mContext = context;
        imageUrls = images;
        Log.e("Image Urls", images.toString());
        names = artistNames;
        trackid = ids;
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
    public void onBindViewHolder(@NonNull TrackViewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(imageUrls.get(position))
                .into(holder.trackImage);
        Log.e("Loops", String.valueOf(position));
        holder.trackName.setText(names.get(position));
        holder.trackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, MusicPlayer.class);
                Bundle extras = new Bundle();
                String[] trackIdsStringArray = new String[trackid.size()];
                int counter = 0;
                for (String s : trackid){
                    trackIdsStringArray[counter] = s;
                }
                extras.putStringArray("songslistarray", trackIdsStringArray);
                extras.putString("songid", trackid.get(position));
                i.putExtras(extras);
                mContext.startActivity(i);
            }
        });
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        public ImageView trackImage;
        public TextView trackName;
        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            trackImage = itemView.findViewById(R.id.addSong_iv);
            trackName = itemView.findViewById(R.id.addSong_name_tv);

        }
    }
}
