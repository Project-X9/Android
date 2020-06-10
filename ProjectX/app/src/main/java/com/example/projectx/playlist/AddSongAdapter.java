package com.example.projectx.playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectx.R;
import com.example.projectx.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddSongAdapter extends RecyclerView.Adapter<AddSongAdapter.AddSongViewHolder> implements Filterable {
    private ArrayList<Song> mSongItems;
    private ArrayList<Song> mSongItemsFull;
    private Context context;

    public AddSongAdapter(ArrayList<Song> SongItems , Context context) {
        mSongItems=SongItems;
        mSongItemsFull=new ArrayList<>(SongItems);
        this.context=context;
    }

    public static class AddSongViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView,mAddImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public AddSongViewHolder(@NonNull View itemView ) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.addSong_iv);
            mTextView1 = itemView.findViewById(R.id.addSong_name_tv);
            mTextView2 = itemView.findViewById(R.id.addSong_description_tv);
            mAddImageView=itemView.findViewById(R.id.addSongPlus_iv);

        }

    }

    @NonNull
    @Override
    public AddSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_add_song_list,parent,false);
        AddSongViewHolder evh = new  AddSongViewHolder(v);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull  AddSongAdapter.AddSongViewHolder holder, int position) {
        Song currentItem = mSongItems.get(position);
        holder.mTextView1.setText(currentItem.name);
        holder.mTextView2.setText(currentItem.description);
        Picasso.with(context)
                .load(currentItem.imageUrl)
                .into(holder.mImageView);
        holder.mAddImageView.setImageResource(currentItem.CreateImage);
    }

    @Override
    public int getItemCount() {
        return mSongItems.size();
    }

    @Override
    public Filter getFilter() {
        return SongItemFilter;
    }

    private Filter SongItemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           ArrayList<Song> filteredList = new ArrayList<>();
           if (constraint == null || constraint.length()==0){
               filteredList.addAll(mSongItemsFull);
           }else{
               String filterPattern = constraint.toString().toLowerCase().trim();
               for (Song mSong : mSongItemsFull){
                   if (mSong.name.toLowerCase().contains(filterPattern)){
                       filteredList.add(mSong);
                   }
               }
           }
           FilterResults results = new FilterResults();
           results.values=filteredList;
           return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mSongItems.clear();
            mSongItems.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
