package com.example.projectx;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.ColorUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectx.ui.yourlibrary.AlbumsFragment.AlbumActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer extends AppCompatActivity {

    private String TRACK_FETCH_SERVER = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/track/";
    private String MOCK_SERVER = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/track/";
    private TextView playlistName, songTitle, songArtists, songCurrentTime, songDuration;
    private ImageButton likeSongButton, previousButton, playButton, nextButton, blacklistButton, downloadButton, repeatButton, shareButton;
    static MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ImageView albumArt;
    private boolean songLiked, songBlacklisted, songDownloaded;

    private final String CHANNEL_ID = "channel1";
    private final String actionPrevious = "actionprevious";
    private final String actionNext = "actionnext";
    private final String actionPlay = "actionplay";
    private Notification notification;
    private NotificationManager notificationManager;

    private int[] songs = {R.raw.starboy, R.raw.beautiful, R.raw.bent_el_geran, R.raw.side_to_side};
    public static String[] songIdsList;                      //this songlist is either passed to music player, or it is fetched from playlistid
    public static Song currentSong = new Song();
    private static int currentSongIndex;
    static boolean wasPlaying, seeking = false, switching, buffering = false;     //switching is probably not needed, when I made updateseekbar runonUIthread
    private boolean testing = false;
    static boolean loading = false;
    private String targetSongId;
    private boolean changedSong = false;
    boolean isLooping = false;
    private boolean onlineTesting = false;
    private boolean autoNextSong = false;
    static boolean newSong = false;

    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        initializeSongsInfo();
        initializeActivityComponents();
        initializeMediaPlayer();
        fetchSongData();
        updateActivityComponents();

        setListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
        showNotification();
    }

    private void showNotification() {
        if (!mediaPlayer.isPlaying()) return;
        Intent intent = new Intent(this, MusicPlayer.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.id.albumImage_iv);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setLargeIcon(icon)
                .setContentTitle(currentSong.name)
                .setContentText(Arrays.toString(currentSong.artistsNames).replaceAll("[\\[\\]]", ""))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    private void initializeSongsInfo() {
//        if (targetSongId != null) return;
        if (onlineTesting) {
            songIdsList = new String[]{"5e86459124471028e4d3539b"};
            targetSongId = "5e86459124471028e4d3539b";
        } else {
            Intent intent = getIntent();
            Bundle b = intent.getExtras();
            if (b == null) return;
            String temp = b.getString("songid");
            if (!temp.equals(currentSong.id)) {
                targetSongId = temp;
                songIdsList = b.getStringArray("songslistarray");
                newSong = true;
            }
        }
        Log.d("TAG", "initializeSongsInfo: called " + targetSongId);
    }

    private void fetchSongData() {
        if ((currentSong.url == null) || changedSong || newSong)    //if no song is currently playing this means, and this means app did not close and start again
        {
            fetchMusicData process = new fetchMusicData();
            process.setURL(TRACK_FETCH_SERVER + targetSongId);
            disableButtons();
            loading = true;

            if (newSong) {
                MusicPlayer.mediaPlayer.stop();
                MusicPlayer.mediaPlayer.reset();
                MusicPlayer.mediaPlayer.release();
                MusicPlayer.mediaPlayer = null;
                initializeMediaPlayer();
                newSong = false;
            }
            process.execute();
        }
    }

    private void disableButtons() {
        disableControlButtons();
        likeSongButton.setImageResource(R.drawable.like_song_loading);
        downloadButton.setImageResource(R.drawable.download_song_pressed);
        shareButton.setImageResource(R.drawable.share_song_pressed);
        blacklistButton.setImageResource(R.drawable.blacklist_song_loading);
    }

    private void enableButtons() {
        enableControlButtons();
        likeSongButton.setImageResource(R.drawable.like_song);
        downloadButton.setImageResource(R.drawable.download_song);
        shareButton.setImageResource(R.drawable.share_song);
        blacklistButton.setImageResource(R.drawable.blacklist_song);
    }

    private void disableControlButtons() {
        playButton.setImageResource(R.drawable.play_button_pressed);
        nextButton.setImageResource(R.drawable.next_song_button_pressed);
        previousButton.setImageResource(R.drawable.previous_song_button_pressed);
    }

    private void enableControlButtons() {
        playButton.setImageResource(R.drawable.play_button);
        nextButton.setImageResource(R.drawable.next_song_button);
        previousButton.setImageResource(R.drawable.previous_song_button);
    }


    /**
     * Sets listeners for mediaplayer and seekbar
     */
    private void setListeners() {
        if (testing) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    songCurrentTime.setText(convertToTime(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    wasPlaying = mediaPlayer.isPlaying();
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                    seeking = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (wasPlaying)
                        mediaPlayer.start();
                    mediaPlayer.seekTo(seekBar.getProgress());
                    seeking = false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if ((!mediaPlayer.isLooping())) {
                        playSongAt(1);
                        mediaPlayer.start();
                    }
                }
            });
        } else {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    songCurrentTime.setText(convertToTime(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    seeking = true;
                    wasPlaying = mediaPlayer.isPlaying();
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    seeking = false;
                    if (currentSong.url == null)
                        return;   //if no internet don't set buffering and don't seek
                    mediaPlayer.seekTo(seekBar.getProgress());
                    buffering = true;
                    disableControlButtons();
                }
            });
        }
    }

    /**
     * Gets called each time the song playing changes, this is responsible for getting info about the song and updating the UI
     */
    public void updateActivityComponents() {
        updatePlaylistName();
        updateAlbumArt();
        updateSongInfo();
        updateSeekBar();
        updateSongStats();
    }

    /**
     * Updates playlist name depending on current song
     */
    private void updatePlaylistName() {
        //TODO:call api and get playlist current song
        if (testing) {
            if (currentSongIndex == 0) {
                playlistName.setText("Starboy");
            } else if (currentSongIndex == 1) {
                playlistName.setText("COSMIC");
            } else if (currentSongIndex == 2) {
                playlistName.setText("Mahraganat Hassan Shakosh");
            } else if (currentSongIndex == 3) {
                playlistName.setText("Dangerous Woman");
            }
        } else {
            if (currentSong.albumName != null)
                playlistName.setText(currentSong.albumName);
        }
    }

    /**
     * updates album art depending on current song
     */
    private void updateAlbumArt() {
        //TODO: get song cover from api call
        if (testing) {
            if (currentSongIndex == 0) {
                albumArt.setImageResource(R.drawable.album_art_starboy);
            } else if (currentSongIndex == 1) {
                albumArt.setImageResource(R.drawable.album_art_cosmic);
            } else if (currentSongIndex == 2) {
                albumArt.setImageResource(R.drawable.album_art_mahraganat);
            } else if (currentSongIndex == 3) {
                albumArt.setImageResource(R.drawable.album_art_dangerous_woman);
            }
            getBgGradient();
        } else {
            if (currentSong.imageUrl != null) {
                new Thread() {

                    public void run() {
                        Bitmap image = null;
                        try {
                            URL url = new URL(currentSong.imageUrl);
                            image = BitmapFactory.decodeStream(url.openStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        final Bitmap finalImage = image;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                albumArt.setImageBitmap(finalImage);
                                getBgGradient();
                            }
                        });
                    }
                }.start();
            }
        }
    }

    /**
     * Updates song title and song artists depending on current song
     */
    private void updateSongInfo() {
        //get song title and artist and check if liked and downloaded
        if (testing) {
            if (currentSongIndex == 0) {
                songTitle.setText("Starboy");
                songArtists.setText("The Weeknd, Daft Punk");
            } else if (currentSongIndex == 1) {
                songTitle.setText("Beautiful");
                songArtists.setText("Bazzi");
            } else if (currentSongIndex == 2) {
                songTitle.setText("Mahragan Bent El Geran");
                songArtists.setText("Hassan Shakosh");
            } else if (currentSongIndex == 3) {
                songTitle.setText("Side To Side");
                songArtists.setText("Ariana Grande");
            }
        } else {
            if (currentSong.name != null) {
                songTitle.setText(currentSong.name);
                songArtists.setText(Arrays.toString(currentSong.artistsNames).replaceAll("[\\[\\]]", ""));
            }
        }
    }

    /**
     * Update duration of song, and change progress of seekBar at equal intervals
     */
    private void updateSeekBar() {
        int time = mediaPlayer.getDuration();
        seekBar.setMax(time);
        songDuration.setText(convertToTime(time));

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MusicPlayer.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!seeking & !switching) {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    }
                });
            }
        }, 0, 200);
        //TODO: get duration of current song
    }

    /**
     * Converts time in milliseconds to string in format 0:00
     *
     * @param millis: Time in milliseconds to be converted
     * @return
     */
    private String convertToTime(int millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        return String.format("%01d:%02d", minute, second);
    }

    /**
     * Updates if song is liked, blacklisted and downloaded
     */
    private void updateSongStats() {
        getIsSongLiked();
        songBlacklisted = false;
        songDownloaded = false;
    }

    /**
     * call api and check if currentSong is like
     */
    private void getIsSongLiked() {
        loginCredentials = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
        final String userId = loginCredentials.getString("id", null);
        String url = "http://localhost:3000/api/v1/users/Track/Ex/";
        url = url + targetSongId;
        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("TAG", "onResponse: success" + response.getBoolean("data"));
                    if (response.getBoolean("data")) {
                        likeSongButton.setImageResource(R.drawable.dislike_song);
                    } else {
                        likeSongButton.setImageResource(R.drawable.like_song);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            public HashMap<String, String> getHeaders() {
                String token = loginCredentials.getString("token", null);
                String arg = "Bearer " + token;
                HashMap<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", token);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", userId);
                return params;
            }

            @Override
            public byte[] getBody() {
                String body = "{\"" + "id" + "\":\"" + userId + "\"}";
                return body.getBytes();
            }
        };
        RequestQueue getUserQueue = Volley.newRequestQueue(getApplicationContext());
        getUserQueue.add(updateRequest);
    }

    /**
     * Closes music player activity and opens Song page
     *
     * @param V
     */
    public void songTitlePressed(View V) {
        //TODO: collapse musicplayer and gotoSongPage(V);
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }
//        closeActivity(V);
        //call api to get song page
        //make intent and open new activity of song page
        //gotoSongPage(V);
    }


    /**
     * Shares link to current song
     *
     * @param V
     */
    public void shareButtonPressed(View V) {
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String songTitle = this.songTitle.getText().toString();
        String artistName = songArtists.getText().toString();
        String shareMessage = "Here's a song for you... " + songTitle
                + " by " + artistName;
        String shareLink = currentSong.url;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage + "\n" + shareLink);
        startActivity(Intent.createChooser(shareIntent, "Share Using..."));
    }

    /**
     * Likes current song and adds it to liked playlist
     *
     * @param V
     */
    public void likeButtonPressed(View V) {
        if (loading) return;
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }
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

    /**
     * Downloads current song to device
     *
     * @param V
     */
    public void downloadButtonPressed(View V) {
        if (loading) return;
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }
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

    /**
     * Closes music player activity and opens album page
     *
     * @param V
     */
    public void albumNamePressed(View V) {
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        } else {
//            closeActivity(V);
            gotoAlbum(V);
        }
    }

    /**
     * Closes music player activity and opens Artist page
     *
     * @param V
     */
    public void artistNamePressed(View V) {
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            closeActivity(V);
            gotoArtist(V);
        }
    }

    /**
     * Closes music player activity and opens Artist page
     *
     * @param V
     */
    public void gotoAlbum(View V) {
        //TODO: goto album which current playing song belongs to
        Intent intent = new Intent(getApplicationContext(), AlbumActivity.class);
        intent.putExtra("AlbumId", currentSong.albumName);
        startActivity(intent);
    }

    /**
     * Closes music player activity
     *
     * @param V
     */
    public void collapseButtonPressed(View V) {
        closeActivity(V);
    }

    /**
     * Closes music player activity
     *
     * @param V
     */
    public void closeActivity(View V) {
        finish();
        //TODO: implement this function
    }

    /**
     * Opens Artist page
     *
     * @param V
     */
    public void gotoArtist(View V) {
        Intent intent = new Intent(getApplicationContext(), ArtistActivity.class);
        intent.putExtra("ArtistId", currentSong.artistsIds[0]);
        startActivity(intent);
        //TODO: call api to go to artist page
    }

    /**
     * Repeats the song if more than 10 seconds passed, otherwise starts current song
     *
     * @param V
     */
    public void previousButtonPressed(View V) {
        if (loading || buffering) return;         //if still loading the track ignore user presses
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mediaPlayer.getCurrentPosition() <= 10000)   //if player is at 10 seconds or less go back to previous song
        {
            playSongAt(-1);
        } else mediaPlayer.seekTo(0);
    }

    /**
     * Plays the next song in the playlist
     *
     * @param V
     */
    public void nextButtonPressed(View V) {
        if (loading || buffering) return;         //if still loading the track ignore user presses
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }
        playSongAt(1);
    }

    /**
     * Plays song either next or previous of the current track
     *
     * @param index: 1 play the next song, -1 play the previous song
     */
    private void playSongAt(int index) {
        if (testing) {
            switching = true;
            wasPlaying = mediaPlayer.isPlaying();
            boolean isLooping = mediaPlayer.isPlaying();
            mediaPlayer.stop();
            mediaPlayer.release();
            currentSongIndex = currentSongIndex + index;
            currentSongIndex = (currentSongIndex < 0) ? 3 : currentSongIndex;
            currentSongIndex = (currentSongIndex > 3) ? 0 : currentSongIndex;
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songs[currentSongIndex]);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(isLooping);
            updateActivityComponents();

            if (wasPlaying)
                mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mediaPlayer.isLooping()) {
                        playSongAt(1);
                        mediaPlayer.start();
                    }
                }
            });
//        setListeners();
            switching = false;
        } else {
            switching = true;
            changedSong = true;
            isLooping = mediaPlayer.isLooping();
            wasPlaying = mediaPlayer.isPlaying();
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            initializeMediaPlayer();
            switching = false;
            if (index == 1) {
                for (int i = 0; i < songIdsList.length; i++) {
                    if (songIdsList[i].equals(currentSong.id)) {
                        if (i + 1 >= songIdsList.length)
                            targetSongId = songIdsList[songIdsList.length - 1];
                        else
                            targetSongId = songIdsList[i + 1];
                        fetchSongData();
                        switching = false;
                        return;
                    }
                }
            }
            if (index == -1) {
                for (int i = 0; i < songIdsList.length; i++) {
                    if (songIdsList[i].equals(currentSong.id)) {
                        if (i - 1 < 0)
                            targetSongId = songIdsList[0];
                        else
                            targetSongId = songIdsList[i - 1];
                        fetchSongData();
                        switching = false;
                        return;
                    }
                }
            }
        }
    }

    /**
     * Blacklist the current playing song
     *
     * @param V
     */
    public void blacklistButtonPressed(View V) {
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }
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

    /**
     * Loops the current Song
     *
     * @param V
     */
    public void repeatButtonPressed(View V) {
        if (mediaPlayer.isLooping()) {
            repeatButton.setImageResource(R.drawable.repeat_button);
            mediaPlayer.setLooping(false);
        } else {
            repeatButton.setImageResource(R.drawable.unrepeat_button);
            mediaPlayer.setLooping(true);
        }
    }

    /**
     * Play/Pause the current song
     *
     * @param V
     */
    public void playButtonPressed(View V) {
        Log.d("TAG", "playButtonPressed: " + loading + buffering);
        if (loading || buffering) return;         //if still loading the track ignore user presses
        if (currentSong.url == null)   //check if no internet connection
        {
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mediaPlayer.isPlaying()) //music is playing and user wants to stop it
        {
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.play_button);
        } else                        //music is not playing and user wants to play it
        {
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if ((!mediaPlayer.isLooping())) {
                        autoNextSong = true;
                        playSongAt(1);
                        mediaPlayer.start();
                    }
                    Log.d("TAG", "onCompletion: calledso");
                }
            });

            playButton.setImageResource(R.drawable.pause_button);
        }
    }

    /**
     * Sets the background gradient color of the music player depending on the album art
     */
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

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
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

    /**
     * Find and set views in the activity
     */
    private void initializeActivityComponents() {

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
        shareButton = (ImageButton) findViewById(R.id.shareButton_ib);

        albumArt = (ImageView) findViewById(R.id.albumImage_iv);
        seekBar = (SeekBar) findViewById(R.id.seekBar_sb);

        seeking = false;
        switching = false;

    }

    private void initializeMediaPlayer() {
        if (mediaPlayer == null) {
            if (testing) {
                currentSongIndex = new Random().nextInt(4);
                mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex]);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(false);
            } else {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(isLooping);

                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        buffering = false;
                        enableControlButtons();
                        if (wasPlaying) {
                            mediaPlayer.start();
                        }
                        if (mediaPlayer.isPlaying())
                            playButton.setImageResource(R.drawable.pause_button);
                        else playButton.setImageResource(R.drawable.play_button);
                    }
                });

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        loading = false;
                        enableButtons();
                        if (changedSong) {
//                            mediaPlayer.setLooping(isLooping);
                            if (wasPlaying || autoNextSong) {
                                mediaPlayer.start();
                                playButton.setImageResource(R.drawable.pause_button);
                                autoNextSong = false;
                            }
                            changedSong = false;
                        }
                        updateActivityComponents();
                    }
                });
            }


        } else {

            if (mediaPlayer.isLooping()) {
                repeatButton.setImageResource(R.drawable.unrepeat_button);
            } else {
                repeatButton.setImageResource(R.drawable.repeat_button);
            }
            if (mediaPlayer.isPlaying()) //music is playing and user wants to stop it
            {
                playButton.setImageResource(R.drawable.pause_button);
            } else                        //music is not playing and user wants to play it
            {
                playButton.setImageResource(R.drawable.play_button);
            }
        }
    }

}
