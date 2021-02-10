package com.example.news.newsapi;

import android.net.Uri;
import android.util.Log;

import com.example.news.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static java.net.HttpURLConnection.HTTP_OK;

public class NewsRunnable implements Runnable  {


    private static final String TAG = "NewsRunnable";
    private MainActivity mainActivity;
    private static final String API_KEY = "e4e9821da29e405090b31e44d09bfe7a";
    private static final String URL = "https://newsapi.org/v2/sources?apiKey="+API_KEY;
    private final HashSet<String> categories = new HashSet<>();
    private final HashSet<String> languages = new HashSet<>();
    private final HashSet<String> countries = new HashSet<>();
    private final ArrayList<News> newsData = new ArrayList<>();

    public NewsRunnable() {
    }

    public NewsRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    @Override
    public void run() {

        Log.d(TAG, "run: "+API_KEY);
        Uri uri= Uri.parse(URL);
        Uri.Builder builder = Uri.parse(URL).buildUpon();
        Log.d(TAG, "runGettingALl: "+builder.toString());
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            java.net.URL url = new URL(builder.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.addRequestProperty("User-Agent","");
            connection.connect();
            Log.d(TAG, "run: "+url.toString());
            int responseCode = connection.getResponseCode();

            StringBuilder result = new StringBuilder();

            if (responseCode == HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }
                Log.d(TAG, "run: Return "+result.toString());
                processData(result.toString());
            }
            else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }
                Log.d(TAG, "run: "+responseCode+"\n"+result.toString());
            }
        }
        catch (Exception e){
            Log.d(TAG, "run: "+e.getStackTrace());
        }

    }

    private void processData(String data) {
        try {
            JSONObject allObject = new JSONObject(data);
            JSONArray jsonArray = allObject.getJSONArray("sources");
            for( int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String cat = obj.getString("category");
                String lang = obj.getString("language");
                String country = obj.getString("country");
                News n = new News();
                n.setCategory(obj.getString("category"));
                n.setLanguage(obj.getString("language"));
                n.setCountry(obj.getString("country"));
                n.setSourceId(obj.getString("id"));
                n.setSourceName(obj.getString("name"));
                newsData.add(n);
                categories.add(cat);
                languages.add(lang);
                countries.add(country);
            }
            Log.d(TAG, "processData: "+categories.toString());
            Log.d(TAG, "processData: "+languages.toString());
            Log.d(TAG, "processData: "+countries.toString());
            for(int i=0;i<newsData.size();i++){
                Log.d(TAG, "processData: "+newsData.get(i).toString());
            }
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    categories.add("All");
                    mainActivity.process(categories,languages,countries,newsData);
                }
            });
        }
        catch(Exception e){

        }

    }


}
