package com.example.projectx.playlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.projectx.R;
import com.google.android.material.textfield.TextInputEditText;

import static android.content.ContentValues.TAG;

public class NameRenamePlaylist extends AppCompatActivity {
    private TextInputEditText mInputName;
    private TextView mActionOk,mActionCancel;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_rename_playlist);
        mInputName= findViewById(R.id.playlistName_tiet);
        mActionOk= findViewById(R.id.createRename_tv);
        mActionCancel= findViewById(R.id.cancel_tv);
        context=getApplicationContext();

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input =mInputName.getText().toString();
                if (!input.equals("")){
                    Intent i = new Intent(context, PlaylistEmpty.class);
                    Bundle extras = new Bundle();
                    extras.putString("key", input);
                    i.putExtras(extras);
                    startActivity(i);
                }
                finish();
            }
        });

    }
}
