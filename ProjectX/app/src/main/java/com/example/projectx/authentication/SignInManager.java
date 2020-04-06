package com.example.projectx.authentication;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SignInManager {
    private Context context;
    public SignInManager(Context c) {
        this.context = c;
    }

    public JSONObject login(final String email, final String password, boolean mockState) {
        String SERVICE_URL = "http://ec2-3-21-218-250.us-east-2.compute.amazonaws.com:3000/api/v1/users/login";
        final String MOCK_SERVICE_URL = "http://192.168.1.15:8000/Users";
        ArrayList<JSONObject> Users  = new ArrayList<JSONObject>();
        if(mockState) {
            RequestFuture<JSONArray> future = RequestFuture.newFuture();
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, MOCK_SERVICE_URL,
                    null, future, future);
            RequestQueue rq = Volley.newRequestQueue(this.context);
            rq.add(request);
            boolean found = false;
            try {
                JSONArray response = future.get();
                for (int i = 0; i < response.length(); i++) {
                        if (response.getJSONObject(i).getString("email").equals(email)) {
                            if (response.getJSONObject(i).getString("password").equals(password)) {
                                found = true;
                                return response.getJSONObject(i);
                            }
                            else {
                                JSONObject result = new JSONObject();
                                result.put("status", "failure");
                                return result;
                            }
                        }
                }
                if (!found) {
                    JSONObject result = new JSONObject();
                    result.put("status", "failure");
                    return result;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SERVICE_URL,
                    null, future, future);
            RequestQueue rq = Volley.newRequestQueue(this.context);
            rq.add(request);
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
        }
        JSONObject errorResult = new JSONObject();
        try {
            errorResult.put("status", "failure");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorResult;
    }
    public void makeToast(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }

}
