package com.example.projectx;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ThreeDataItemAdapter extends RecyclerView.Adapter<ThreeDataItemAdapter.ThreeDataItemViewHolder> {
    private ArrayList<ThreeDataItem> ThreeDataItemList;

    public static class ThreeDataItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public ThreeDataItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.three_data_image_iv);
            mTextView1 = itemView.findViewById(R.id.three_data_text1_tv);
            mTextView2 = itemView.findViewById(R.id.three_data_text2_tv);
        }
    }

    public ThreeDataItemAdapter(ArrayList<ThreeDataItem> List) {
        ThreeDataItemList = List;
    }

    @NonNull
    @Override
    public ThreeDataItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.three_data_item, parent, false);
        ThreeDataItemViewHolder tDIVH = new ThreeDataItemViewHolder(v);
        return tDIVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ThreeDataItemViewHolder holder, int position) {
        ThreeDataItem currentItem=ThreeDataItemList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return ThreeDataItemList.size();
    }
}
