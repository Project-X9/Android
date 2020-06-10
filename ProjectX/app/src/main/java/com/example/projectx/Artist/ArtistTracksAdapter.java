package com.example.projectx.Artist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectx.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistTracksAdapter extends RecyclerView.Adapter<ArtistTracksAdapter.ArtistTracksViewHolder> {

    private ArrayList<ArtistTracksList> mTracksItems;
    private Context context;

    public ArtistTracksAdapter(ArrayList<ArtistTracksList> TracksItems,Context context) {
        this.mTracksItems=TracksItems;
        this.context=context;
    }

    public static class ArtistTracksViewHolder extends RecyclerView.ViewHolder {

        public ImageView mArtistImageResoure;
        public TextView mArtistName;
        public TextView mArtistSongName;

        public ArtistTracksViewHolder(@NonNull View itemView) {
            super(itemView);
            mArtistImageResoure = itemView.findViewById(R.id.addSong_iv);
            mArtistName = itemView.findViewById(R.id.addSong_description_tv);
            mArtistSongName = itemView.findViewById(R.id.addSong_name_tv);
        }
    }


    @NonNull
    @Override
    public ArtistTracksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_song_list,parent,false);
        ArtistTracksViewHolder evh = new ArtistTracksViewHolder(v);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistTracksViewHolder holder, int position) {
        ArtistTracksList currentItem = mTracksItems.get(position);
//        holder.mArtistImageResoure.setImageResource(currentItem.getmArtistImageResoure());
        holder.mArtistName.setText(currentItem.getmArtistName());
        holder.mArtistSongName.setText(currentItem.getmArtistSongName());
        Picasso.with(context)
                .load(currentItem.getmArtistImageURL())
                .into(holder.mArtistImageResoure);
    }


    @Override
    public int getItemCount() {
        return mTracksItems.size();
    }
}
