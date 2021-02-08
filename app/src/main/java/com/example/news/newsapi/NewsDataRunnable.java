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

import static java.net.HttpURLConnection.HTTP_OK;

public class NewsDataRunnable implements Runnable{

    private static final String TAG = "NewsDataRunnable";
    private MainActivity mainActivity;
    private static final String API_KEY = "e4e9821da29e405090b31e44d09bfe7a";
    private static final String URL = "https://newsapi.org/v2/top-headlines";
    private final ArrayList<NewsData> articles = new ArrayList<>();
    private String source;

    public NewsDataRunnable(MainActivity mainActivity, String source) {
        this.mainActivity = mainActivity;
        this.source = source;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: "+API_KEY);
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            Uri.Builder builder = Uri.parse(URL).buildUpon();
            builder.appendQueryParameter("sources",source);
            builder.appendQueryParameter("apiKey",API_KEY);
            String urlTOUser = builder.build().toString();
            Log.d(TAG, "run: URL"+urlTOUser);
            URL url = new URL(urlTOUser);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.addRequestProperty("User-Agent","");
            connection.connect();
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

        }
        catch (Exception e){


        }

    }

    private void processData(String data) {
        Log.d(TAG, "processData: "+data);
        try{
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jArray = jsonObject.getJSONArray("articles");
            for(int i=0;i<jArray.length();i++){
                NewsData nData = new NewsData();
                JSONObject dataObj = jArray.getJSONObject(i);
                nData.setAuthor(dataObj.getString("author"));
                nData.setTitle(dataObj.getString("title"));
                nData.setDescription(dataObj.getString("description"));
                nData.setImageUrl(dataObj.getString("urlToImage"));
                nData.setUrl(dataObj.getString("url"));
                nData.setDate(dataObj.getString("publishedAt"));
               // Log.d(TAG, "processData: "+nData.toString());
                articles.add(nData);
            }
            for(int i=0;i<articles.size();i++){
                Log.d(TAG, "processData: "+articles.get(i).toString());
            }

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.addArticles(articles);
                }
            });
        }
        catch (Exception e){

        }
    }
}
