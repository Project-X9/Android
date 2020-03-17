package com.example.projectx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicPlayer extends AppCompatActivity {

    private TextView songTitleTV, songArtistsTV;
    private ImageButton likeSongButton,previousButton,playButton,nextButton,blacklistButton;
    private MediaPlayer mediaPlayer;
    private boolean songLiked,songBlacklisted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        initializeActivityComponents();
        getBgGradient();
    }

    public void songTitlePressed(View V){
        //TODO: collapse musicplayer and gotoSongPage(V);
        closeActivity(V);
        //gotoSongPage(V);
    }

    public void moreButtonPressed(View V){
        //showcontext menu
    }

    public void shareButtonPressed(View V){
        //TODO: share the current song
    }

    public void likeButtonPressed(View V){
        if(songLiked)   //song is liked and user wants to dislike
        {
            likeSongButton.setImageResource(R.drawable.like_song);
            songLiked=false;
            //TODO: remove song from liked playlist
        }
       else
        {
            likeSongButton.setImageResource(R.drawable.dislike_song);
            songLiked=true;
            //TODO: add song to liked playlist
        }
    }

    public void albumNamePressed(View V){
        closeActivity(V);
        gotoAlbum(V);
    }

    public void artistNamePressed(View V){
        closeActivity(V);
        gotoArtist(V);
    }

    public void gotoAlbum(View V){
        //TODO: goto album which current playing song belongs to
        Log.d("TAG ","gotoAlbum: clicked album");
    }

    public void collapseButtonPressed(View V){
        closeActivity(V);
    }

    public void closeActivity(View V){
        finish();
        //TODO: implement this function
    }

    public void gotoArtist(View V){
        closeActivity(V);
        //TODO: goto album which current playing song belongs to
    }

    public void previousButtonPressed(View V){
        //TODO
    }

    public void nextButtonPressed(View V){
        //TODO
    }

    public void blacklistButtonPressed(View V){
        if(songBlacklisted)   //song is liked and user wants to dislike
        {
            blacklistButton.setImageResource(R.drawable.blacklist_song_normal);
            songBlacklisted=false;
            //TODO: remove song from blacklisted playlist
        }
        else
        {
            blacklistButton.setImageResource(R.drawable.blacklist_song_pressed);
            songBlacklisted=true;
            //TODO: add song to blacklisted playlist
        }
    }

    public void playButtonPressed(View V){
        playButton.setImageResource(R.drawable.pause_button);
    }

    private  void getBgGradient(){
        ImageView albumArtIV =  (ImageView)findViewById(R.id.albumImage_iv);
        Drawable albumArtDrawable = albumArtIV.getDrawable();
        int width = albumArtDrawable.getIntrinsicWidth();
        int height = albumArtDrawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, albumArtDrawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        albumArtDrawable.setBounds(0, 0, width, height);
        albumArtDrawable.draw(canvas);

        long redBucket = 0;
        long greenBucket = 0;
        long blueBucket = 0;
        long pixelCount = 0;
        int c;

        for (int x = 0; x < bitmap.getWidth(); x++)
        {
            for (int y = 0; y < bitmap.getHeight(); y++)
            {
                c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
                // does alpha matter?
            }
        }

        int red = (int) (redBucket/pixelCount);
        int green = (int) (greenBucket/pixelCount);
        int blue = (int) (blueBucket/pixelCount);
        int averageColor = Color.rgb(red, green, blue);
        float [] hsl = new float[3];
        ColorUtils.colorToHSL(averageColor,hsl);
        hsl[2]=hsl[2]*2f;
        hsl[1]=hsl[1]*1.2f;
        averageColor=ColorUtils.HSLToColor(hsl);
        int [] colors =  {averageColor,0xFF101010};

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors);
        ConstraintLayout L = findViewById(R.id.music_player_layout);
        L.setBackground(gd);
    }

    private void initializeActivityComponents(){
        mediaPlayer=MediaPlayer.create(this,R.raw.starboy);

        //Set their scrolling animation if overflow text
        songTitleTV=(TextView)findViewById(R.id.songTitle_tv);
        songTitleTV.setSelected(true);

        songArtistsTV=(TextView)findViewById(R.id.songArtist_tv);
        songArtistsTV.setSelected(true);

        likeSongButton = (ImageButton)findViewById(R.id.likeButton_ib);
        previousButton = (ImageButton)findViewById(R.id.previousSong_ib);
        playButton = (ImageButton)findViewById(R.id.playSong_ib);
        nextButton = (ImageButton)findViewById(R.id.nextSong_ib);
        blacklistButton = (ImageButton)findViewById(R.id.blackListSong_ib);

        songLiked=false;
        songBlacklisted=false;
        //check if song is like and set songliked variable, if song liked display dislike button instead
        //TODO: get song info and update textviews

    }
}
