package com.example.projectx.Artist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectx.R;

import java.util.ArrayList;

public class ArtistInterface extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mArtistName;
    private  ArrayList<ArtistTracksList> exampleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_interface);
        mArtistName = findViewById(R.id.artistName_tv);

        exampleList = new ArrayList<>();

        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Venom" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Greatest" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","Normal" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Fall" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Nice Guy" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","Good Guy" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Believe" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","Like Home" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","In Your Head" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Rap God" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Cold Wind Blows" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","No Love" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","25 to Life" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","My Mom" ));
        exampleList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Beautiful" ));

        mRecyclerView = findViewById(R.id.tracks_rv);

        mLayoutManager =  new LinearLayoutManager(this);
        mAdapter = new ArtistTracksAdapter(exampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Rename Track", "Delete Track"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog( exampleList.get(position), position);
//                    Toast.makeText(ArtistInterface.this, "clicked on Rename Track", Toast.LENGTH_SHORT).show();
                } else {
                    deleteTrack(position);
//                    Toast.makeText(ArtistInterface.this, "clicked on Delete Track", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.show();
    }

    private void deleteTrack(int position) {
        exampleList.remove(position);
        mAdapter.notifyItemChanged(position);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void showNoteDialog( final ArtistTracksList mArtistTracksList, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.activity_edit_track_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ArtistInterface.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.trackNameNew_et);

        if ( mArtistTracksList != null) {
            inputNote.setText(mArtistTracksList.getmArtistSongName());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton( "update" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(ArtistInterface.this, "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                //  updating track name
                if ( mArtistTracksList != null) {
                    // update track name by it's id
                    updateTrackName(inputNote.getText().toString(), position);
                }
            }
        });
    }

    private void updateTrackName(String mTrackName, int position) {
        ArtistTracksList artistTracksList = exampleList.get(position);
        artistTracksList.setmArtistSongName(mTrackName);
        // refreshing the list
        exampleList.set(position, artistTracksList);
        mAdapter.notifyItemChanged(position);
    }

    public void addsongs(View view){

    }


}
