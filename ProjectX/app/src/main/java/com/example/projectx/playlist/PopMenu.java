package com.example.projectx.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import com.example.projectx.R;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

public class PopMenu extends AppCompatActivity {
    ArrayList<String> menuItems ;
    ArrayList<Integer> mImageId ;
    PopMenuAdapter mPopMenuAdapter;
    ListView mListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_menu);
        AutofitTextView mInputDisplay = findViewById(R.id.user_aftv);
        mListView = (ListView) findViewById(R.id.menu_lv);
        menuItems = new ArrayList<>();
        menuItems=getMenuItemList();
        mImageId = new ArrayList<>();
        mImageId=getImageMenuList();
        mPopMenuAdapter = new PopMenuAdapter(this, menuItems, mImageId);
        mListView.setAdapter(mPopMenuAdapter);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
            mInputDisplay.setText(value);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String mItemString = menuItems.get(position);
                if (mItemString == "Rename playlist" ) {
                    Intent intent = new Intent(PopMenu.this, NameRenamePlaylist.class);
                    startActivity(intent);
                    finish();


                }else{
                    Toast.makeText(PopMenu.this, "clicked on"+ menuItems.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        });
        /* here i get the display width and height */
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.9), (int)(height*.8));

    }

    public ArrayList<String> getMenuItemList(){
        menuItems = new ArrayList<>();
        menuItems.add("Add song");
        menuItems.add("Rename playlist");
        menuItems.add("Delete playlist");
        menuItems.add("Share");
        return menuItems;
    }

    public ArrayList<Integer> getImageMenuList(){
        mImageId = new ArrayList<>();
        mImageId.add(R.drawable.ic_add_circle_outline);
        mImageId.add(R.drawable.ic_edit);
        mImageId.add(R.drawable.ic_delete);
        mImageId.add(R.drawable.ic_share);
        return mImageId;
    }
}
