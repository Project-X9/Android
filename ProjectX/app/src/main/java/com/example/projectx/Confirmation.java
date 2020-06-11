package com.example.projectx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.projectx.ui.home.GenreRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Confirmation extends AppCompatActivity {
    SharedPreferences shp;
    final String CREDENTIALS_FILE = "loginCreds";
    JSONObject result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmation);
        Button enterToken = (Button) findViewById(R.id.EnterToken);
        final EditText token = (EditText) findViewById(R.id.Token_et);
        enterToken.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 TokenAsyncTask tat = new TokenAsyncTask(getBaseContext(), token.getText().toString());
                 tat.execute();
            }
        });
    }
    private class TokenAsyncTask extends AsyncTask<String, Integer, Void> {
        private Context context;
        private String token;
        public TokenAsyncTask(Context c, String token) {
            this.context = c;
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://192.168.1.7:3000/api/v1/users/confirmation/" + token;
            JSONObject js = new JSONObject();
            shp = getSharedPreferences(CREDENTIALS_FILE, MODE_PRIVATE);
            try {
                js.put("id" , shp.getString("id", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest artistsRequest = new JsonObjectRequest(Request.Method.PATCH, url,
                    null, future, future){
                @Override
                public HashMap<String, String> getHeaders() {

                    String arg = "Bearer " + token;
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", token);
                    return params;
                }
            };
            RequestQueue getUserQueue = Volley.newRequestQueue(this.context);
            getUserQueue.add(artistsRequest);
            try {
                result = future.get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            try {
                if (result.getString("status").equals("success")){

                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }




}