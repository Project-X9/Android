package com.example.projectx.ui.yourlibrary.ArtistFragment;

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

import com.example.projectx.ArtistActivity;
import com.example.projectx.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistFragmentAdapter extends RecyclerView.Adapter<ArtistFragmentAdapter.ArtistFragmentViewHolder> {
    private ArrayList<ArtistData> mArtistData;
    private Context context;
    private onArtistListner onArtistListner;

    public ArtistFragmentAdapter(ArrayList<ArtistData> mArtistData , Context context , onArtistListner onArtistListner) {
        this.mArtistData=mArtistData;
        this.context=context;
        this.onArtistListner =onArtistListner;
    }

    public static class ArtistFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImageView;
        public TextView mTextView1;
//        public TextView mTextView2;
        onArtistListner onArtistListner1;

        public ArtistFragmentViewHolder(@NonNull View itemView , onArtistListner onArtistListner) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.song_iv);
            mTextView1 = itemView.findViewById(R.id.song_name_tv);
//            mTextView2 = itemView.findViewById(R.id.singer_name_tv);
            this.onArtistListner1 =onArtistListner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onArtistListner1.onArtistClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ArtistFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_song_list,parent,false);
        ArtistFragmentViewHolder evh = new ArtistFragmentViewHolder(v, onArtistListner);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistFragmentViewHolder holder, final int position) {
        ArtistData currentItem = mArtistData.get(position);
        holder.mTextView1.setText(currentItem.getName());
//        holder.mTextView2.setText(currentItem.getmMadeBy());
            Picasso.with(context)
                    .load(currentItem.getPhoto())
                    .into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"This has been clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, ArtistActivity.class);
                Bundle extras = new Bundle();
                extras.putString("ArtistId", mArtistData.get(position).getId() );
                i.putExtras(extras);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArtistData.size();
    }

    public interface onArtistListner {
        void onArtistClick(int position);
    }
}
