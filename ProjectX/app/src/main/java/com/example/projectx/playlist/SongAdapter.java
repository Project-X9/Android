package com.example.projectx.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectx.R;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private ArrayList<SongList> mSongItems;

    public SongAdapter(ArrayList<SongList> SongItems) {
        mSongItems=SongItems;
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
        SongList currentItem = mSongItems.get(position);
        holder.mImageView.setImageResource(currentItem.getmImageResoure());
        holder.mTextView1.setText(currentItem.getmText1());
        holder.mTextView2.setText(currentItem.getmText2());

    }

    @Override
    public int getItemCount() {
        return mSongItems.size();
    }
}
