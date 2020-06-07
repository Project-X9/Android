package com.example.projectx.playlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.projectx.R;

public class PlaylistEmpty extends AppCompatActivity {

      TextView mInputDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_empty);
         mInputDisplay = findViewById(R.id.user_tv);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           changePlaylistName(extras);
        }

        //this is for the custom toolbar
//        Toolbar toolbar = findViewById(R.id.custom_tb);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        SlidingUpPanelLayout mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingUpPanelLayout);
//        mSlidingLayout.setEnabled(false);

    }

    private void changePlaylistName(Bundle extras) {
        String value = extras.getString("key");
        mInputDisplay.setText(value);
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
//        Intent intent = new Intent(this, PlayListFull.class);
//        startActivity(intent);
    }

    /*this for the menu that show when you click on the three dot image*/
    public void showMenu(View view){
        String inputName = mInputDisplay.getText().toString();
        Intent intent = new Intent(this, PopMenu.class);
        intent.putExtra("key",inputName);
        startActivity(intent);
        onPause();

    }
    public void returnBack(View v) {
        finish();
//        Intent intent = new Intent(this, PlaylistEmpty.class);
//        startActivity(intent);
    }


}
