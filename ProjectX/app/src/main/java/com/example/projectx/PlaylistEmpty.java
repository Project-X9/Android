package com.example.projectx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlaylistEmpty extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_empty);

        //this is for the custom toolbar
//        Toolbar toolbar = findViewById(R.id.custom_tb);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        SlidingUpPanelLayout mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingUpPanelLayout);
//        mSlidingLayout.setEnabled(false);

    }

    public void addsongs (View view){
//
//        ListView listView = findViewById(R.id.lv);
//        SlidingUpPanelLayout mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingUpPanelLayout);
//        String str1;
//
//        if(mSlidingLayout.isEnabled()) {
//            mSlidingLayout.setEnabled(false);
//            str1 = " ";
//        }else{
//            mSlidingLayout.setEnabled(true);
//            str1 = "New Song is added";
//            }
//        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Collections.singletonList(str1)));
//

        Intent intent = new Intent(this,PlayListFull.class);
        startActivity(intent);
    }


}
