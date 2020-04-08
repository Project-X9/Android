package com.example.projectx.ArtistFragment;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.MainActivity;
import com.example.projectx.R;
import com.example.projectx.ui.home.HomeViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ArtistFragment extends Fragment {

    private ArtistViewModel mViewModel;
    private String artistUrl;

    public ArtistFragment(String passedUrl){
        artistUrl = passedUrl;
    }
     View root;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(ArtistViewModel.class);
         root = inflater.inflate(R.layout.artist_fragment, container, false);
        ArtistAsyncTask artistAgent = new ArtistAsyncTask();
        artistAgent.execute();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ArtistViewModel.class);

        // TODO: Use the ViewModel
    }
    private class ArtistAsyncTask extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected JSONObject doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, artistUrl, null,
                     future, future);
            RequestQueue signUpRq = Volley.newRequestQueue(getContext());
            signUpRq.add(request);
            try {
                JSONObject response = future.get();
                if (response.getString("status").equals("400")){
                    makeToast("Bad request. Make sure you entered your full name.");
                }
                else if (response.getString("status").equals("500")) {
                    makeToast("Seems the server down. Sorry!");
                }
                else {
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

        @Override
        protected void onPostExecute(JSONObject result){
            super.onPostExecute(result);
                Bitmap image = null;
                try {
                    JSONObject data = result.getJSONObject("data").getJSONObject("artist");
                    final URL url = new URL(data.getString("image"));
                    TextView artistName = (TextView) root.findViewById(R.id.artistName);
                    artistName.setText(data.getString("name"));
                    TextView artistBio = (TextView) root.findViewById(R.id.artistBio);
                    artistBio.setText(data.getString("Bio"));
                    new Thread() {

                        public void run() {
                            Bitmap image = null;
                            try {
                                image = BitmapFactory.decodeStream(url.openStream());
                                final Bitmap finalImage = image;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LinearLayout layout = (LinearLayout) root.findViewById(R.id.hostImage);
                                        Drawable dr = new BitmapDrawable(finalImage);
                                        layout.setBackground(dr);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                        }
                    }.start();

            } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }


    /**
     * creates a toast message and displays it with duration Toast.LENGTH_SHORT
     * @param message, string you want to show the user in a toast;
     *
     *
     */
    public void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Stores User credentials by opening a file and storing user email or Facebook ID, so user does
     * not login every time
     * @param credentialsFile  the name of the file to store user credentials in
     * @param email  the user's email or Facebook ID
     */


    /**
     * extracts a string that contains the text of an EditView
     * @param view
     * @return text of an EditText formatted as a string
     */
    public String stringify(EditText view) {
        return view.getText().toString();
    }

}
}

