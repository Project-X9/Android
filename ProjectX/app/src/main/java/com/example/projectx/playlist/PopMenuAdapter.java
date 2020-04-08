package com.example.projectx.playlist;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectx.R;

import java.util.ArrayList;

public class PopMenuAdapter  extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> menuItems ;
    private ArrayList<Integer> mImageId ;

    public PopMenuAdapter(Context mContext, ArrayList<String> menuItems, ArrayList<Integer> mImageId) {
        this.mContext = mContext;
        this.menuItems = menuItems;
        this.mImageId = mImageId;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView= View.inflate(mContext, R.layout.activity_menu_item_list,null);
           }
        ImageView mImageView = (ImageView) convertView.findViewById(R.id.menuItem_iv);
        TextView mTextView = (TextView) convertView.findViewById(R.id.menuItem_tv);
        mImageView.setImageResource(mImageId.get(position));
        mTextView.setText(menuItems.get(position));
        return convertView;
    }
}
