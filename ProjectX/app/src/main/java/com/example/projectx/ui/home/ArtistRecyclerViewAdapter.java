package com.example.projectx.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectx.R;
import com.example.projectx.playlist.PlayListFull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistRecyclerViewAdapter extends RecyclerView.Adapter<ArtistRecyclerViewAdapter.ArtistViewHolder>{

    private static final String TAG = "ArtistRecyclerViewAdapt";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public ArtistRecyclerViewAdapter(Context context, ArrayList<String> names,
                                     ArrayList<String> imageUrls){
        mNames = names;
        mImageUrls = imageUrls;
        mContext = context;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder called.");
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_layout, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

           Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.artistImage);
           holder.artistName.setText(mNames.get(position));
           holder.artistImage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(mContext,"This has been clicked", Toast.LENGTH_SHORT).show();
//                           Intent i = new Intent(mContext, PlayListFull.class);
//                           Bundle extras = new Bundle();
//                           extras.putString("PlaylistIDs", playlistIDs[0]);
//                           i.putExtras(extras);
//                           startActivity(i);
                       }
                   });
    }




    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        CircleImageView artistImage;
        TextView artistName;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image);
            artistName = itemView.findViewById(R.id.artist_name);
        }
    }
}
