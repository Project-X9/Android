package com.example.projectx.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectx.R;
import com.example.projectx.ui.yourlibrary.AlbumsFragment.AlbumActivity;
import com.example.projectx.ui.yourlibrary.AlbumsFragment.AlbumsData;

import java.util.ArrayList;


public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumViewHolder> {

    private ArrayList<String> mNames;
    private ArrayList<String> mImageUrls;
    //private ArrayList<AlbumsData> albums = new ArrayList<>();
    private Context mContext;

    public AlbumRecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> urls ){
        mNames = names;
        mImageUrls = urls;
        mContext = context;

    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout, parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {

        Glide.with(mContext).asBitmap().load(mImageUrls.get(position)).into(holder.albumImage);
        holder.albumName.setText(mNames.get(position));
        holder.albumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Album clicked.", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(mContext, AlbumActivity.class);
//                Bundle extras = new Bundle();
//                extras.putString("AlbumID", ClickedAlbumId);
//                i.putExtras(extras);
//                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {

        ImageView albumImage;
        TextView albumName;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.album_iv);
            albumName = itemView.findViewById(R.id.album_name_tv);
        }


    }
}
