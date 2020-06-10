package com.example.projectx.UserActivity;

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

public class UserPlaylistAdapter extends RecyclerView.Adapter<UserPlaylistAdapter.UserPlaylistViewHolder> {
    private ArrayList<UserData> mUserData;
    private Context context;

    public UserPlaylistAdapter(ArrayList<UserData> UserData , Context context ) {
        this.mUserData=UserData;
        this.context=context;
    }

    public static class UserPlaylistViewHolder extends RecyclerView.ViewHolder  {

        public ImageView mUserImageView;
        public TextView mPlaylistName;

        public UserPlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserImageView = itemView.findViewById(R.id.addSong_iv);
            mPlaylistName = itemView.findViewById(R.id.addSong_name_tv);

        }


    }

    @NonNull
    @Override
    public UserPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_song_list,parent,false);
        UserPlaylistViewHolder evh = new UserPlaylistViewHolder(v);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserPlaylistViewHolder holder, int position) {
        UserData currentItem = mUserData.get(position);
        holder.mPlaylistName.setText(currentItem.getmUserPlaylistName());
        Picasso.with(context)
                .load(currentItem.getmUserPlaylistImageURL())
                .into(holder.mUserImageView);
    }

    @Override
    public int getItemCount() {
        return mUserData.size();
    }


}
