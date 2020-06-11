package com.example.projectx.ui.yourlibrary.PlaylistFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.MainActivity;
import com.example.projectx.R;
import com.example.projectx.UserActivity.UserData;
import com.example.projectx.playlist.NameRenamePlaylist;
import com.example.projectx.playlist.PlayListFull;
import com.example.projectx.ui.yourlibrary.ArtistFragment.ArtistData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment implements PlaylistFragmentAdapter.onPlaylistListner {
//    private final String SERVER_URL = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/playlist/";
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    JSONObject myUser;
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;
    static ArrayList<UserData>  onlineData= new ArrayList<>(); ;
    static ArrayList<String> UserId;
    private static Context context;
    static String ClickedPlaylistId;
    static ProgressDialog progDialog;
    UserAsyncTask user;
    ArrayList<UserAsyncTask> mUserAsyncTasksArray = new ArrayList<>();

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_playlist, container, false);
        mRecyclerView = view.findViewById(R.id.playlists_rv);
        mLayoutManager =  new LinearLayoutManager(getActivity());
        context= getActivity();
        for(UserAsyncTask item : mUserAsyncTasksArray){
            item.cancel(true);
            onlineData.clear();
        }
        setCreatePlaylist();
        user = new UserAsyncTask(context);
        user.execute();
        mUserAsyncTasksArray.add(user);
        return view;
    }

    private void addPlaylist() {
        onlineData.clear();
        setCreatePlaylist();
        try {
            JSONArray playlistsJson = myUser.getJSONObject("data").getJSONObject("user").getJSONArray("playlists");
            for (int i = 0; i < playlistsJson.length(); i++) {
                JSONObject playlist = playlistsJson.getJSONObject(i);
                onlineData.add(new UserData(playlist.getString("image"), playlist.getString("name"),playlist.getString("author"),playlist.getString("_id")));
            }
            updatePlaylists();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCreatePlaylist(){
        onlineData = new ArrayList<>();
        onlineData.add(new UserData(R.drawable.ic_add,"To Create Playlist Click Here","",""));
    }

    public  void updatePlaylists() {
        mAdapter = new PlaylistFragmentAdapter(onlineData,context,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPlaylistClick(int position) {
        if (onlineData.get(position).getId()== null){
            Toast.makeText(getContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            ClickedPlaylistId = onlineData.get(position).getId();
        }
        if(position==0){
            Intent i = new Intent(getContext(), NameRenamePlaylist.class);
            Bundle extras = new Bundle();
            extras.putString("UserId", UserId.get(0));
            extras.putString("Create", "1");
            i.putExtras(extras);
            startActivity(i);
//            finishActivity();
            onResume();
        }else {
            Intent i = new Intent(context, PlayListFull.class);
            Bundle extras = new Bundle();
            extras.putString("PlaylistIDs", ClickedPlaylistId);
            i.putExtras(extras);
            startActivity(i);
            onResume();
//            finishActivity();
        }

    }

    private void finishActivity() {
        if(getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progDialog = new ProgressDialog(context);
        progDialog.show();
        for(UserAsyncTask item : mUserAsyncTasksArray){
            item.cancel(true);
            onlineData.clear();
        }
        setCreatePlaylist();
        UserAsyncTask user = new UserAsyncTask(context);
        user.execute();
    }

    public void setUserId(JSONObject userId) {
        try {
            UserId=new ArrayList<>();
            UserId.add(userId.getJSONObject("data").getJSONObject("user").getString("_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class UserAsyncTask extends AsyncTask<String, Integer, JSONObject> {
        private Context context;

        public UserAsyncTask(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            final String CREDENTIALS_FILE = "loginCreds";
            loginCredentials = getActivity().getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
            String userId = loginCredentials.getString("id", null);
            Log.e("userId", userId);
            String url = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/users/";
            url = url + userId;
            JsonObjectRequest getUser = new JsonObjectRequest(Request.Method.GET, url,
                    null, future, future){
                @Override
                public HashMap<String, String> getHeaders() {
                    String token = loginCredentials.getString("token", null);
                    String arg = "Bearer " + token;
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", token);
                    return params;
                }
            };
            RequestQueue getUserQueue = Volley.newRequestQueue(this.context);
            getUserQueue.add(getUser);
            try{
                JSONObject user =  future.get();
                Log.e("user",user.toString());
//                SharedPreferences.Editor editor = loginCredentials.edit();
//                editor.putString("UserObject", user.toString());
//                editor.commit();
                myUser = user;
                return user;

            }catch(ExecutionException e){
                e.printStackTrace();
            }
            catch(InterruptedException e ){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            addPlaylist();
            if(progDialog.isShowing()){
                progDialog.dismiss();
            }
        }

    }


}
