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

public class NameRenamePlaylist extends AppCompatDialogFragment {
    private TextInputEditText mInputName;
    private TextView mActionOk,mActionCancel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_name_rename_playlist,container,false);
        mInputName=view.findViewById(R.id.playlistName_tiet);
        mActionOk= view.findViewById(R.id.createRename_tv);
        mActionCancel= view.findViewById(R.id.cancel_tv);

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input =mInputName.getText().toString();
                if (!input.equals("")){
                    ((PopMenu)getActivity()).playlistName=input;
                }
                getDialog().dismiss();
            }
        });

        return view;
    }
}
