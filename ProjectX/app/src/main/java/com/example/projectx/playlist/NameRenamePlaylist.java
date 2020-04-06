package com.example.projectx.playlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectx.R;
import com.google.android.material.textfield.TextInputEditText;

public class NameRenamePlaylist extends AppCompatActivity {

    private TextInputEditText mInputName;
    private SoftInputAssist softInputAssist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_rename_playlist);
         mInputName= findViewById(R.id.playlistName_tiet);
        softInputAssist = new SoftInputAssist(this);
    }

    public void onClickCancel(View view){
        Intent intent = new Intent(NameRenamePlaylist.this, PlaylistEmpty.class);
        startActivity(intent);
        finish();
    }

    public void onClickRename(View view){
        String inputName =mInputName.getText().toString();
        if (!inputName.equals("")){
            Intent intent = new Intent(NameRenamePlaylist.this, PlaylistEmpty.class);
            intent.putExtra("key",inputName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        softInputAssist.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        softInputAssist.onPause();

    }

    @Override
    protected void  onDestroy() {
        super.onDestroy();
        softInputAssist.onDestroy();

    }

}
