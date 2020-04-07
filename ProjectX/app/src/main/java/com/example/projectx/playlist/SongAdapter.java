package com.example.projectx.playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectx.R;
import com.example.projectx.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private ArrayList<Song> mSongItems;
    private Context context;

    public SongAdapter(ArrayList<Song> SongItems , Context context) {
        mSongItems=SongItems;
        this.context=context;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.song_iv);
            mTextView1 = itemView.findViewById(R.id.song_name_tv);
            mTextView2 = itemView.findViewById(R.id.singer_name_tv);
        }
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_song_list,parent,false);
        SongViewHolder evh = new SongViewHolder(v);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song currentItem = mSongItems.get(position);
        holder.mTextView1.setText(currentItem.name);
        StringBuffer sb = new StringBuffer();
        for (String s : currentItem.artist_name) {
            sb.append(s);
            sb.append(" ");
        }
        String ArtistName = sb.toString();
          holder.mTextView2.setText(ArtistName);
        Picasso.with(context)
                .load(currentItem.imageUrl)
                .into(holder.mImageView);


    }

    @Override
    public int getItemCount() {
        return mSongItems.size();
    }
}
