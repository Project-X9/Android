package com.example.projectx.Artist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectx.R;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistInterface extends AppCompatActivity {
    private static boolean test=false;
    private static Context context;
    private final String MOCK_URL = "http://www.mocky.io/v2/5eda925e330000650079ecc9";
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout headerLayout;
    static TextView mArtistName,topArtistName;
    static ImageView mArtistImage;
    static String imageURL;
    private AppBarLayout appBarLayout;
    private ConstraintLayout backgroundGradient;
    static   ArrayList<ArtistTracksList> testList,onlineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_interface);
        mArtistName = findViewById(R.id.artistName_tv);
        topArtistName=findViewById(R.id.top_artistname_tv);
        mArtistImage = findViewById(R.id.artist_image);
        appBarLayout = findViewById(R.id.playListFull_activity_appbarlayout);
        headerLayout =  findViewById(R.id.header_content_ll);
        backgroundGradient = findViewById(R.id.background_gradient_cl);
        context=getApplicationContext();
        mRecyclerView = findViewById(R.id.songlists_list_rv);
        mLayoutManager =  new LinearLayoutManager(this);
        Listeners();
        FetchArtistData fetchArtistData = new FetchArtistData(this);
        fetchArtistData.setURL(MOCK_URL);
        fetchArtistData.execute();
    }

    private static void testData() {
        testList = new ArrayList<>();
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Venom" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Greatest" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","Normal" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Fall" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Nice Guy" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","Good Guy" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Believe" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","Like Home" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","In Your Head" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Rap God" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Cold Wind Blows" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","No Love" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem ","25 to Life" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","My Mom" ));
        testList.add(new ArtistTracksList(R.drawable.ic_music_note_black_24dp,"Eminem","Beautiful" ));

    }

    private void Listeners() {
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

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float x = headerLayout.getHeight();
                topArtistName.setAlpha((-verticalOffset / x));
                int[] y = new int[2];
                ViewGroup.LayoutParams params = backgroundGradient.getLayoutParams();
                params.height = y[1];
                backgroundGradient.setLayoutParams(params);

                backgroundGradient.setY(verticalOffset);
            }
        });
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Rename Track", "Delete Track"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog( onlineData.get(position), position);
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
        testList.remove(position);
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
        ArtistTracksList artistTracksList = onlineData.get(position);
        artistTracksList.setmArtistSongName(mTrackName);
        // refreshing the list
        onlineData.set(position, artistTracksList);
        mAdapter.notifyItemChanged(position);
    }

    public void addsongs(View view){

    }

    public void returnBack(View v) {

        finish();

    }

    public static void setmArtisPicture(String imageURL){
        Picasso.with(context)
                .load(imageURL)
                .into(mArtistImage);
    }

    public static void updatePlaylists() {
        if (test){
            testData();
            mAdapter = new ArtistTracksAdapter(testList,context);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            setmArtisPicture(imageURL);
            mAdapter = new ArtistTracksAdapter(onlineData,context);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
