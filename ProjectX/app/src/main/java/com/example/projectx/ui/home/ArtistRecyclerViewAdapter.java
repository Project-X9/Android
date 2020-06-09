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
import com.example.projectx.ArtistActivity;
import com.example.projectx.ArtistFragment.ArtistFragment;
import com.example.projectx.R;
import com.example.projectx.playlist.PlayListFull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistRecyclerViewAdapter extends RecyclerView.Adapter<ArtistRecyclerViewAdapter.ArtistViewHolder>{

    private static final String TAG = "ArtistRecyclerViewAdapt";
    ArrayList<ArtistInfo> artists = new ArrayList<>();
    private Context mContext;

    public ArtistRecyclerViewAdapter(Context context, ArrayList<ArtistInfo> artists){
        this.artists = artists;
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
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

           Glide.with(mContext)
                .asBitmap()
                .load(artists.get(position).getImageUrl())
                .into(holder.artistImage);
           holder.artistName.setText(artists.get(position).getName());
           holder.artistImage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(mContext,"This has been clicked", Toast.LENGTH_SHORT).show();
                           Intent i = new Intent(mContext, ArtistActivity.class);
                           Bundle extras = new Bundle();
                           extras.putString("ArtistId", artists.get(position).getId() );
                           i.putExtras(extras);
                           mContext.startActivity(i);
                       }
                   });
    }




    @Override
    public int getItemCount() {
        return artists.size();
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
