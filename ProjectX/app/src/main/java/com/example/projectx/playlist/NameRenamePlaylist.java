package com.example.projectx.playlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.R;
import com.example.projectx.ui.yourlibrary.PlaylistFragment.PlaylistFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class NameRenamePlaylist extends AppCompatActivity {
    private TextInputEditText mInputName;
    private TextView mActionOk, mActionCancel, mTitle;
    private Context context;
    private RequestQueue requestQueue;
    String UserId, Create, PlaylistId, mPlaylistURL;
    String input;
    JSONObject result;
    PlaylistFragment playlistFragment = new PlaylistFragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_rename_playlist);
        mTitle = findViewById(R.id.textView);
        mInputName = findViewById(R.id.playlistName_tiet);
        mActionOk = findViewById(R.id.createRename_tv);
        mActionCancel = findViewById(R.id.cancel_tv);
        context = getApplicationContext();
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String mUSERID = b.getString("UserId");
        String mPLAYLISTid = b.getString("PlaylistId");
        String key = b.getString("Create");
        String playlistURL = b.getString("URL");
        mPlaylistURL = playlistURL;
        UserId = mUSERID;
        Create = key;
        PlaylistId = mPLAYLISTid;
        if (Create.equals("0")) {
            mTitle.setText("Rename Playlist");
            mActionOk.setText("Rename");
        }
    }

    public void OnClickActionCancel(View view) {
        if (Create.equals("1")) {
            finish();
        } else if (Create.equals("0")) {
            Intent i = new Intent(context, PlaylistEdit.class);
            Bundle extras = new Bundle();
            extras.putString("PlaylistName", input);
            extras.putString("URL", mPlaylistURL);
            extras.putString("PlaylistId", PlaylistId);
            i.putExtras(extras);
            startActivity(i);
            finish();
        }
    }

    public void OnClickActionOk(View view) {
        if (Create.equals("1")) {
            input = mInputName.getText().toString();
            if (!input.equals("")) {
                createPlaylistAsyncTask createPlaylistAsyncTask = new createPlaylistAsyncTask();
                createPlaylistAsyncTask.execute(input);
                finish();
            }
        } else if (Create.equals("0")) {
            input = mInputName.getText().toString();
            if (!input.equals("")) {
                renamePlaylistAsyncTask renamePlaylistAsyncTask = new renamePlaylistAsyncTask();
                renamePlaylistAsyncTask.execute(input);
                Intent i = new Intent(context, PlaylistEdit.class);
                Bundle extras = new Bundle();
                extras.putString("PlaylistName", input);
                extras.putString("URL", mPlaylistURL);
                extras.putString("PlaylistId", PlaylistId);
                i.putExtras(extras);
                startActivity(i);
                finish();

            }
        }
    }

    private class createPlaylistAsyncTask extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                result = onSubmitCreate(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            try {
                Log.e("reached", "postexecute");
                Log.e("result", result.toString());
                if (result.getString("status").equals("success")) {
                } else {
                    Log.e("Post Data ", "Response Failed");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class renamePlaylistAsyncTask extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                result = onSubmitRename(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            try {
                Log.e("reached", "postexecute");
                Log.e("result", result.toString());
                if (result.getString("status").equals("success")) {
                } else {
                    Log.e("Post Data ", "Response Failed");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private JSONObject onSubmitCreate(String data) throws JSONException {
        final String ipAddress = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getString("IpAddressTextPref", null);
        String PLAYLIST_FETCH_SERVER = "http://" + ipAddress + ":3000/api/v1/playlist/";
        String URL = PLAYLIST_FETCH_SERVER + UserId;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject playlistInfo = new JSONObject();
        playlistInfo.put("name", data);
        playlistInfo.put("image", "https://iwitness.com.ng/wp-content/uploads/2018/08/unnamed-1484220845.png");
        playlistInfo.put("description", "Private PlayList");
        JSONArray empty = new JSONArray();
        playlistInfo.put("tracks", empty);
        playlistInfo.put("artists", empty);
        playlistInfo.put("author", "5e877b8fae42032b7c867feb");
        playlistInfo.put("likers", empty);
        playlistInfo.put("followers", empty.put(UserId));

        Log.e("SendJson", playlistInfo.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                playlistInfo, future, future);
        RequestQueue createPlaylistReq = Volley.newRequestQueue(this.context);
        createPlaylistReq.add(request);

        try {
            JSONObject response = future.get();
            Log.e("post result", response.toString());
            if (response == null) {
                Log.e("Response", "error");
                response = new JSONObject();
                response.put("status", "Couldn't reach server");
                return response;
            } else {
//                    response.put("status", "success");
                return response;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject onSubmitRename(String data) throws JSONException {
        final String ipAddress = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getString("IpAddressTextPref", null);
        String PLAYLIST_FETCH_SERVER = "http://" + ipAddress + ":3000/api/v1/playlist/";
        String URL = PLAYLIST_FETCH_SERVER + PlaylistId;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject playlistInfo = new JSONObject();
        playlistInfo.put("name", data);

        Log.e("SendJson", playlistInfo.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, URL,
                playlistInfo, future, future);
        RequestQueue createPlaylistReq = Volley.newRequestQueue(this.context);
        createPlaylistReq.add(request);

        try {
            JSONObject response = future.get();
            Log.e("post result", response.toString());
            if (response == null) {
                Log.e("Response", "error");
                response = new JSONObject();
                response.put("status", "Couldn't reach server");
                return response;
            } else {
                response.put("status", "success");
                return response;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
