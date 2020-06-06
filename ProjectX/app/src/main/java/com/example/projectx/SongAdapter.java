package com.example.projectx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private ArrayList<Song> mSongItems;
    private Context context;
    private onSongListner mOnSongListner;

    public SongAdapter(ArrayList<Song> SongItems , Context context , onSongListner onSongListner) {
        mSongItems=SongItems;
        this.context=context;
        this.mOnSongListner=onSongListner;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        onSongListner onSongListner;

        public SongViewHolder(@NonNull View itemView , onSongListner onSongListner) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.song_iv);
            mTextView1 = itemView.findViewById(R.id.song_name_tv);
            mTextView2 = itemView.findViewById(R.id.singer_name_tv);
            this.onSongListner=onSongListner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSongListner.onSongClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_song_list,parent,false);
        SongViewHolder evh = new SongViewHolder(v,mOnSongListner);
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

    public interface onSongListner{
        void onSongClick(int position);
    }
}
