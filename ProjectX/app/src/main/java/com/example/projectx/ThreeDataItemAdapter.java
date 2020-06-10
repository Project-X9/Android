package com.example.projectx;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ThreeDataItemAdapter extends RecyclerView.Adapter<ThreeDataItemAdapter.ThreeDataItemViewHolder> {
    private ArrayList<ThreeDataItem> ThreeDataItemList;
    private OnItemClickListener Listener;

    public interface OnItemClickListener {
        void onItemClick(int Position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        Listener = listener;
    }

    public static class ThreeDataItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public ThreeDataItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.three_data_image_iv);
            mTextView1 = itemView.findViewById(R.id.three_data_text1_tv);
            mTextView2 = itemView.findViewById(R.id.three_data_text2_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ThreeDataItemAdapter(ArrayList<ThreeDataItem> List) {
        ThreeDataItemList = List;
    }

    @NonNull
    @Override
    public ThreeDataItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.three_data_item, parent, false);
        ThreeDataItemViewHolder tDIVH = new ThreeDataItemViewHolder(v, Listener);
        return tDIVH;
    }

    @Override
    public void onBindViewHolder(@NonNull final ThreeDataItemViewHolder holder, int position) {
        final ThreeDataItem currentItem = ThreeDataItemList.get(position);
        if (currentItem.getImageResourceId() == 0) {
            if (currentItem.getImageSource() != null) {
                Glide.with(holder.mImageView.getContext())
                        .load(currentItem.getImageSource())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.mImageView);
            }
        } else {
            holder.mImageView.setImageResource(currentItem.getImageResourceId());
        }
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return ThreeDataItemList.size();
    }
}
