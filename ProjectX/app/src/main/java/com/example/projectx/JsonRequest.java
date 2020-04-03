package com.example.projectx;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonRequest extends AsyncTask<String, String, String> {
    String data = "";
    URL currentURL;
    static JsonRequest object = new JsonRequest();
    public static JsonRequest getInstance() {
        return object;
    }
    public void setURL(URL url) {
        currentURL = url;
    }
    public void setURL(String url) throws MalformedURLException {
        currentURL = new URL(url);
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL z = new URL("zaks");
            HttpURLConnection connection = (HttpURLConnection) currentURL.openConnection();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line = "";
            while (line != null) {
                line = reader.readLine();
                data = data + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
