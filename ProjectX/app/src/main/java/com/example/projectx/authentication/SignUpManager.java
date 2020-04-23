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
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SignUpManager  {

    Context context;
    static int errorFromServer;
    public SignUpManager(Context context){
        this.context = context;
    }

    public JSONObject signUp(Boolean mockState, String name, String email, String age, String gender ,String password) {
        final String SIGNUP_URL = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:" +
                "3000/api/v1/users/";
        final String MOCK_SIGNUP_URL = "http://192.168.1.15:8000/Users";
        String usedUrl;
        if (mockState){
            usedUrl = MOCK_SIGNUP_URL;
            Log.e("url", "shouldn't be here");
        }
        else {
            usedUrl = SIGNUP_URL;
        }
        RequestFuture<JSONObject> future = RequestFuture.newFuture();


            Map<String, String> userInfo = new HashMap();
            userInfo.put("name", name);
            userInfo.put("email", email);
            userInfo.put("age", age);
            userInfo.put("gender", gender);
            userInfo.put("password", password);
            JSONObject parameters = new JSONObject(userInfo);
            Log.e("json", parameters.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, usedUrl,
                    parameters, future, future) {
                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    if (volleyError != null && volleyError.networkResponse != null) {
                        errorFromServer = volleyError.networkResponse.statusCode;
                    }
                    return volleyError;
                }
            };
            Log.e("url", usedUrl);
            RequestQueue signUpRq = Volley.newRequestQueue(this.context);
            signUpRq.add(request);

            try {
                JSONObject response = future.get();
                if (!mockState) {
                    return response;
                }
                else {
                    if (response == null) {
                        response = new JSONObject();
                        response.put("status", "Couldn't reach mock server");
                        return response;
                    }
                    else {
                        response.put("status", "success");
                        return response;
                    }
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (ExecutionException e) {
                Log.e("duplicate email", "reached here");
                if(errorFromServer == 400){
                    JSONObject response = new JSONObject();
                    try {
                        response.put("status", "email already in use");
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    return response;
                }
                e.printStackTrace();
                Log.e("duplicate mailing",e.getMessage());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
    }



    }


