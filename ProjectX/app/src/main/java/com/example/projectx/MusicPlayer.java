package com.example.projectx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer extends AppCompatActivity {

    private TextView playlistName, songTitle, songArtists, songCurrentTime, songDuration;
    private ImageButton likeSongButton, previousButton, playButton, nextButton, blacklistButton, downloadButton, repeatButton;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ImageView albumArt;
    private boolean songLiked, songBlacklisted, songDownloaded;

    private int[] songs = {R.raw.starboy, R.raw.beautiful, R.raw.bent_el_geran, R.raw.side_to_side};
    private int currentSong;
    private boolean wasPlaying, seeking, switching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        initializeActivityComponents();
        updateActivityComponents();
        setListeners();
    }

    private void setListeners() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                songCurrentTime.setText(convertToTime(progress));
                if (fromUser) {

//                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                wasPlaying = mediaPlayer.isPlaying();
                mediaPlayer.pause();
                seeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                if ((seekBar.getProgress() == seekBar.getMax()) && (mediaPlayer.isPlaying())) {
//                    nextso
//                }
                if (wasPlaying)
                    mediaPlayer.start();
                mediaPlayer.seekTo(seekBar.getProgress());
                seeking = false;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!mediaPlayer.isLooping()) {
                    playNextSong();
                    mediaPlayer.start();
                }
            }
        });
    }

    //should be called on every new song
    private void updateActivityComponents() {
        updatePlaylistName();
        updateAlbumArt();
        updateSongInfo();
        updateSeekBar();
        updateSongStats();
    }

    private void updateAlbumArt() {
        //TODO: get song cover from api call
        if (currentSong == 0) {
            albumArt.setImageResource(R.drawable.album_art_starboy);
        } else if (currentSong == 1) {
            albumArt.setImageResource(R.drawable.album_art_cosmic);
        } else if (currentSong == 2) {
            albumArt.setImageResource(R.drawable.album_art_mahraganat);
        } else if (currentSong == 3) {
            albumArt.setImageResource(R.drawable.album_art_dangerous_woman);
        }
        getBgGradient();
    }

    private void updatePlaylistName() {
        //TODO:call api and get playlist current song
        if (currentSong == 0) {
            playlistName.setText("Starboy");
        } else if (currentSong == 1) {
            playlistName.setText("COSMIC");
        } else if (currentSong == 2) {
            playlistName.setText("Mahraganat Hassan Shakosh");
        } else if (currentSong == 3) {
            playlistName.setText("Dangerous Woman");
        }
    }

    private void updateSongInfo() {
        //get song title and artist and check if liked and downloaded
        if (currentSong == 0) {
            songTitle.setText("Starboy");
            songArtists.setText("The Weeknd, Daft Punk");
        } else if (currentSong == 1) {
            songTitle.setText("Beautiful");
            songArtists.setText("Bazzi");
        } else if (currentSong == 2) {
            songTitle.setText("Mahtagan Bent El Geran");
            songArtists.setText("Hassan Shakosh");
        } else if (currentSong == 3) {
            songTitle.setText("Side To Side");
            songArtists.setText("Ariana Grande");
        }
    }


    private void updateSeekBar() {
        int time = mediaPlayer.getDuration();
        seekBar.setMax(time);
        songDuration.setText(convertToTime(time));

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!seeking & !switching)
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 200);
        //TODO: get duration of current song
    }

    private String convertToTime(int millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        return String.format("%01d:%02d", minute, second);
    }

    private void updateSongStats() {
        songLiked = false;
        songBlacklisted = false;
        songDownloaded = false;
    }

    public void songTitlePressed(View V) {
        //TODO: collapse musicplayer and gotoSongPage(V);
        closeActivity(V);
        //call api to get song page
        //make intent and open new activity of song page
        //gotoSongPage(V);
    }

    public void moreButtonPressed(View V) {
        //showcontext menu
    }

    public void shareButtonPressed(View V) {
        //TODO: get song link via api call
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String songTitle = this.songTitle.getText().toString();
        String artistName = songArtists.getText().toString();
        String shareMessage = "Here's a song for you... " + songTitle
                + " by " + artistName;
        String shareLink = "Song link goes here";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage + "\n" + shareLink);
        startActivity(Intent.createChooser(shareIntent, "Share Using..."));
    }

    public void likeButtonPressed(View V) {
        if (songLiked)   //song is liked and user wants to dislike
        {
            likeSongButton.setImageResource(R.drawable.like_song);
            songLiked = false;
            //TODO: remove song from liked playlist via api call
        } else {
            likeSongButton.setImageResource(R.drawable.dislike_song);
            songLiked = true;
            //TODO: add song to liked playlist via api call
        }
    }

    public void downloadButtonPressed(View V) {
        if (songDownloaded)   //song is liked and user wants to dislike
        {
            downloadButton.setImageResource(R.drawable.download_song);
            songDownloaded = false;
            //TODO: remove song from liked playlist via api call
        } else {
            downloadButton.setImageResource(R.drawable.delete_song);
            songDownloaded = true;
            //TODO: add song to liked playlist via api call
        }
    }

    public void albumNamePressed(View V) {
        closeActivity(V);
        gotoAlbum(V);
    }

    public void artistNamePressed(View V) {
        closeActivity(V);
        gotoArtist(V);
    }

    public void gotoAlbum(View V) {
        //TODO: goto album_art_starboy which current playing song belongs to
        Log.d("TAG ", "gotoAlbum: clicked album_art_starboy");
    }

    public void collapseButtonPressed(View V) {
        closeActivity(V);
    }

    public void closeActivity(View V) {
        finish();
        //TODO: implement this function
    }

    public void gotoArtist(View V) {
        closeActivity(V);
        //TODO: call api to go to artist page
    }

    public void previousButtonPressed(View V) {
        //TODO: implement this
    }

    public void nextButtonPressed(View V) {
        playNextSong();
    }

    private void playNextSong() {
        switching = true;
        wasPlaying = mediaPlayer.isPlaying();
        boolean isLooping = mediaPlayer.isPlaying();
        mediaPlayer.stop();
        mediaPlayer.release();
        int nextSong = (currentSong == 3) ? 0 : currentSong + 1;
        currentSong = nextSong;
        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs[nextSong]);
        mediaPlayer.setLooping(isLooping);
        updateActivityComponents();

        if (wasPlaying)
            mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!mediaPlayer.isLooping()) {
                    playNextSong();
                    mediaPlayer.start();
                }
            }
        });
        switching = false;
    }


    public void blacklistButtonPressed(View V) {
        if (songBlacklisted)   //song is liked and user wants to dislike
        {
            blacklistButton.setImageResource(R.drawable.blacklist_song_normal);
            songBlacklisted = false;
            //TODO: remove song from blacklisted playlist
        } else {
            blacklistButton.setImageResource(R.drawable.blacklist_song_pressed);
            songBlacklisted = true;
            //TODO: add song to blacklisted playlist
        }
    }

    public void repeatButtonPressed(View V) {
        if (mediaPlayer.isLooping()) {
            repeatButton.setImageResource(R.drawable.repeat_button);
            mediaPlayer.setLooping(false);
        } else {
            repeatButton.setImageResource(R.drawable.unrepeat_button);
            mediaPlayer.setLooping(true);
        }
    }


    public void playButtonPressed(View V) {
        if (mediaPlayer.isPlaying()) //music is playing and user wants to stop it
        {
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.play_button);
        } else                        //music is not playing and user wants to play it
        {
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.pause_button);
        }
    }

    public void seekBarPressed(View V) {
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mediaPlayer.seekTo(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
////                mediaPlayer.pause();
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
////                mediaPlayer.start();
////                mediaPlayer.seekTo(seekBar.getProgress());
//            }
//        });
    }

    private void getBgGradient() {
        Drawable albumArtDrawable = albumArt.getDrawable();
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

        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
                // does alpha matter?
            }
        }

        int red = (int) (redBucket / pixelCount);
        int green = (int) (greenBucket / pixelCount);
        int blue = (int) (blueBucket / pixelCount);
        int averageColor = Color.rgb(red, green, blue);
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(averageColor, hsl);
//        hsl[2] = hsl[2] * 2f;
//        hsl[1] = hsl[1] * 1.2f;
        averageColor = ColorUtils.HSLToColor(hsl);
        int[] colors = {averageColor, 0xFF101010};

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        ConstraintLayout L = findViewById(R.id.music_player_layout);
        L.setBackground(gd);
    }

    private void initializeActivityComponents() {
        currentSong = new Random().nextInt(4);
        mediaPlayer = MediaPlayer.create(this, songs[currentSong]);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(false);

        //Set their scrolling animation if overflow text
        songTitle = (TextView) findViewById(R.id.songTitle_tv);
        songTitle.setSelected(true);

        songArtists = (TextView) findViewById(R.id.songArtist_tv);
        songArtists.setSelected(true);

        playlistName = (TextView) findViewById(R.id.playlistName_tv);
        songCurrentTime = (TextView) findViewById(R.id.songCurrentTime_tv);
        songDuration = (TextView) findViewById(R.id.songDuration_tv);

        likeSongButton = (ImageButton) findViewById(R.id.likeButton_ib);
        previousButton = (ImageButton) findViewById(R.id.previousSong_ib);
        playButton = (ImageButton) findViewById(R.id.playSong_ib);
        nextButton = (ImageButton) findViewById(R.id.nextSong_ib);
        blacklistButton = (ImageButton) findViewById(R.id.blackListSong_ib);
        downloadButton = (ImageButton) findViewById(R.id.downloadButton_ib);
        repeatButton = (ImageButton) findViewById(R.id.repeatButton_ib);

        albumArt = (ImageView) findViewById(R.id.albumImage_iv);
        seekBar = (SeekBar) findViewById(R.id.seekBar_sb);

        seeking = false;
        switching = false;

        //check if song is liked and set songliked variable, if song liked display dislike button instead
        //TODO: get song info and update textviews

    }
}
