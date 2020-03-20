package com.example.projectx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class PlayListFull extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_full);


        ArrayList<SongList> exampleList = new ArrayList<>();
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Venom","Eminem" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Angel","theweeknd" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"At my best ","MGK" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Stitches","Shawn Mends" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"macarena","Tyga" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Venom","Eminem" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Angel","theweeknd" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"At my best ","MGK" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Stitches","Shawn Mends" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"macarena","Tyga" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Venom","Eminem" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Angel","theweeknd" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"At my best ","MGK" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"Stitches","Shawn Mends" ));
        exampleList.add(new SongList(R.drawable.ic_music_note_black_24dp,"macarena","Tyga" ));

        mRecyclerView = findViewById(R.id.recyclerView);

        mLayoutManager =  new LinearLayoutManager(this);
        mAdapter = new SongAdapter(exampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
