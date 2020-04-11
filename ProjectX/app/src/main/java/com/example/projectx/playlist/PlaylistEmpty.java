package com.example.projectx.playlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectx.R;
import com.example.projectx.playlist.PlayListFull;

import me.grantland.widget.AutofitTextView;

public class PlaylistEmpty extends AppCompatActivity {

    private  AutofitTextView mInputDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_empty);
         mInputDisplay = findViewById(R.id.user_aftv);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           changeUserName(extras);
        }

        //this is for the custom toolbar
//        Toolbar toolbar = findViewById(R.id.custom_tb);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        SlidingUpPanelLayout mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingUpPanelLayout);
//        mSlidingLayout.setEnabled(false);

    }

    /**
     * ChangeUserName it use the bundle to get the new name using a specific key
     * and set the text view with this new name.
     * @param extras
     */
    private void changeUserName(Bundle extras) {
        String value = extras.getString("key");
        mInputDisplay.setText(value);
    }

    /**
     * AddSong it open a new fragment to choose which song you want to add to your playlist
     * @param view
     */
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
        Intent intent = new Intent(this, PlayListFull.class);
        startActivity(intent);
    }

    /**
     * ShowMenu is for the menu that show when you click on the three dot image on the top left corner
     * @param view
     */
    public void showMenu(View view){
        String inputName = mInputDisplay.getText().toString();
        Intent intent = new Intent(this, PopMenu.class);
        intent.putExtra("key",inputName);
        startActivity(intent);
        onPause();

    }



}
