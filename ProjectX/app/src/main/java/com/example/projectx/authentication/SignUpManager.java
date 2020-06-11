package com.example.projectx.authentication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SignUpManager {

    Context context;

    public SignUpManager(Context context) {
        this.context = context;
    }

    public JSONObject signUp(Boolean mockState, String name, String email, String age, String gender, String password) {
        final String ipAddress = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("IpAddressTextPref", null);
        final String SIGNUP_URL = "http://" + ipAddress + ":" +
                "3000/api/v1/users/SignUp/";
        final String MOCK_SIGNUP_URL = "http://" + ipAddress + ":8000/Users";
        String usedUrl;
        if (mockState) {
            usedUrl = MOCK_SIGNUP_URL;
        } else {
            usedUrl = SIGNUP_URL;
        }
        RequestFuture<JSONObject> future = RequestFuture.newFuture();


        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("name", name);
            userInfo.put("email", email);
            userInfo.put("age", Integer.valueOf(age));
//            userInfo.put("gender", gender);
            userInfo.put("password", password);
//            userInfo.put("premium", false);
//            userInfo.put("previouslyPremium", false);
//            JSONArray empty = new JSONArray();
//            userInfo.put("CreatedPlaylists", empty);
//            userInfo.put("followers", empty);
//            userInfo.put("following", empty);
//            userInfo.put("playlists", empty);
//            userInfo.put("playlists", empty);
//            userInfo.put("playlists", empty);
//            userInfo.put("playlists", empty);
//            userInfo.put("playlists", empty);
//            userInfo.put("tracks", empty);
//            userInfo.put("albums", empty);
//            userInfo.put("artists", empty);
//            userInfo.put("deletedPlaylists", empty);
//            userInfo.put("likedPlaylists", empty);
//            userInfo.put("tokens", empty);
//            userInfo.put("ConfirmationToken", "");
//            userInfo.put("__v", 1);
//            userInfo.put("isVerified", false);
//            userInfo.put("notifications", empty);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //JSONObject parameters = new JSONObject(userInfo);
        Log.e("json", userInfo.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, usedUrl,
                userInfo, future, future);
        Log.e("url", usedUrl);
        RequestQueue signUpRq = Volley.newRequestQueue(this.context);
        signUpRq.add(request);

        try {
            JSONObject response = future.get();
            Log.e("post result", response.toString());
            if (!mockState) {
                return response;
            } else {
                if (response == null) {
                    response = new JSONObject();
                    response.put("status", "Couldn't reach mock server");
                    return response;
                } else {
                    response.put("status", "success");
                    return response;
                }
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


