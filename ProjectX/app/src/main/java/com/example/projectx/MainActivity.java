package com.example.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.ArtistFragment.ArtistFragment;
import com.example.projectx.authentication.AuthenticationPage;
import com.example.projectx.authentication.SignInManager;
import com.example.projectx.playlist.PlayListFull;
import com.example.projectx.ui.yourlibrary.AlbumsFragment.AlbumsFragment;
import com.example.projectx.ui.yourlibrary.ArtistFragment.ArtisttFragment;
import com.example.projectx.ui.yourlibrary.PlaylistFragment.PlaylistFragment;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends FragmentActivity {
    SharedPreferences loginCredentials;
    final String CREDENTIALS_FILE = "loginCreds";
    JSONObject myUser;
    AlbumsFragment albumsFragment=new AlbumsFragment();
    ArtisttFragment artisttFragment = new ArtisttFragment();
    PlaylistFragment playlistFragment = new PlaylistFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,
                R.id.artistfragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        UserAsyncTask user = new UserAsyncTask(this);
        user.execute();
    }
        private class UserAsyncTask extends AsyncTask<String, Integer, JSONObject> {
            private Context context;
            public UserAsyncTask (Context c){
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
                loginCredentials = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
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
                    SharedPreferences.Editor editor = loginCredentials.edit();
                    editor.putString("UserObject", user.toString());
                    editor.commit();
                    myUser = user;
                    albumsFragment.setUserAlbum(myUser);
                    artisttFragment.setUserArtist(myUser);
                    playlistFragment.setUserId(myUser);
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
        }

    }
    public void storeCredentials(String credentialsFile, String email) {
        loginCredentials = getSharedPreferences(credentialsFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = loginCredentials.edit();
        editor.putString("email", email);
        editor.commit();
    }
}
