package com.example.projectx.ui.yourlibrary.AlbumsFragment;

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

public class AlbumsFragmentAdapter extends RecyclerView.Adapter<AlbumsFragmentAdapter.AlbumsFragmentViewHolder>  {

    private ArrayList<AlbumsData> albumsData;
    private Context context;
    private onAlbumListner onAlbumListner;

    public AlbumsFragmentAdapter(ArrayList<AlbumsData> mArtistData , Context context , onAlbumListner onAlbumListner) {
        this.albumsData =mArtistData;
        this.context=context;
        this.onAlbumListner =onAlbumListner;
    }

    public static class AlbumsFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImageView;
        public TextView mTextView1;
        //        public TextView mTextView2;
        onAlbumListner onAlbumListner1;

        public AlbumsFragmentViewHolder(@NonNull View itemView , onAlbumListner onAlbumListner) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.song_iv);
            mTextView1 = itemView.findViewById(R.id.song_name_tv);
//            mTextView2 = itemView.findViewById(R.id.singer_name_tv);
            this.onAlbumListner1 =onAlbumListner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAlbumListner1.onAlbumClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public AlbumsFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_song_list,parent,false);
        AlbumsFragmentViewHolder evh = new AlbumsFragmentViewHolder(v, onAlbumListner);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsFragmentViewHolder holder, int position) {
        AlbumsData currentItem = albumsData.get(position);
        holder.mTextView1.setText(currentItem.getName());
//        holder.mTextView2.setText(currentItem.getmMadeBy());
        Picasso.with(context)
                .load(currentItem.getPhoto())
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return albumsData.size();
    }

    public interface onAlbumListner {
        void onAlbumClick(int position);
    }
}
