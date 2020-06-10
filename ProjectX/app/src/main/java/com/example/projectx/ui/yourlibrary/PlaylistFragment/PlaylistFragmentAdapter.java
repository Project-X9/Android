package com.example.projectx.ui.yourlibrary.PlaylistFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectx.R;
import com.example.projectx.UserActivity.UserData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaylistFragmentAdapter extends RecyclerView.Adapter<PlaylistFragmentAdapter.PlaylistFragmentViewHolder> {
    private ArrayList<UserData> mUserData;
    private Context context;
    private onPlaylistListner mOnPlaylistListner;

    public PlaylistFragmentAdapter(ArrayList<UserData> mUserData , Context context , onPlaylistListner mOnPlaylistListner) {
        this.mUserData=mUserData;
        this.context=context;
        this.mOnPlaylistListner=mOnPlaylistListner;
    }

    public static class PlaylistFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        onPlaylistListner onPlaylistListner;

        public PlaylistFragmentViewHolder(@NonNull View itemView , onPlaylistListner onPlaylistListner) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.addSong_iv);
            mTextView1 = itemView.findViewById(R.id.addSong_name_tv);
            mTextView2 = itemView.findViewById(R.id.addSong_description_tv);
            this.onPlaylistListner=onPlaylistListner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPlaylistListner.onPlaylistClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public PlaylistFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_song_list,parent,false);
        PlaylistFragmentViewHolder evh = new PlaylistFragmentViewHolder(v,mOnPlaylistListner);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistFragmentViewHolder holder, int position) {
        UserData currentItem = mUserData.get(position);
        holder.mTextView1.setText(currentItem.getmUserPlaylistName());
        holder.mTextView2.setText(currentItem.getmMadeBy());
        if(currentItem.getmUserPlaylistImageURL()!=null){
            Picasso.with(context)
                    .load(currentItem.getmUserPlaylistImageURL())
                    .into(holder.mImageView);
        }else{
//            Picasso.with(context)
//                    .load(currentItem.getCreateImage())
//                    .into(holder.mImageView);
            holder.mImageView.setImageResource(currentItem.getCreateImage());
        }



    }

    @Override
    public int getItemCount() {
        return mUserData.size();
    }

    public interface onPlaylistListner{
        void onPlaylistClick(int position);
    }
}
