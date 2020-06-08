package com.example.projectx.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectx.R;
import com.example.projectx.ui.home.AlbumRecyclerViewAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder> {

    ArrayList<String> mImageUrls = new ArrayList<String>();
    ArrayList<String> mNames = new ArrayList<>();
    ArrayList<String> mTypes = new ArrayList<>();
    Context mContext;
    public SearchRecyclerViewAdapter(Context context, ArrayList<String> imageUrls, ArrayList<String> names, ArrayList<String> types) {
        mImageUrls = imageUrls;
        mNames = names;
        mTypes = types;
        mContext = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout, parent,false);
        return new SearchRecyclerViewAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        if (mImageUrls.size() != 0) {
            Glide.with(mContext).asBitmap().load(mImageUrls.get(position)).into(holder.itemPicture);
            holder.name.setText(mNames.get(position));
            holder.type.setText(mTypes.get(position));
            holder.itemPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Album clicked.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        //Album, Artist, Playlist, Track
        // Picture
        //Name
        //https://run.mocky.io/v3/63117a42-c771-4c10-a9c3-5fa974a77f14
        CircleImageView itemPicture;
        TextView name;
        TextView type;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            itemPicture = itemView.findViewById(R.id.search_image);
            name = itemView.findViewById(R.id.search_item_name);
            type = itemView.findViewById(R.id.search_item_type);

        }
    }
}