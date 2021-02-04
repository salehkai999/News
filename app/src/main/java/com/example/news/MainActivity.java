package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.news.newsapi.News;
import com.example.news.newsapi.NewsRunnable;

import java.util.ArrayList;
import java.util.HashSet;


// e4e9821da29e405090b31e44d09bfe7a
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private HashSet categories;
    private HashSet langs;
    private HashSet countries;
    private ArrayList<News> newsList;
    private Menu opt_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);
       // getSupportActionBar().setHomeButtonEnabled(true);

        new Thread(new NewsRunnable(this)).start();

        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
        );

        // Create the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.open,  /* "open drawer" description for accessibility */
                R.string.close  /* "close drawer" description for accessibility */
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        if(item.getItemId()==R.id.topics){
            Toast.makeText(this, "Topics", Toast.LENGTH_SHORT).show();
            item.getSubMenu().add("All");
            Object[] catAll = categories.toArray();
            for(int i=0;i<catAll.length;i++){
                item.getSubMenu().add(catAll[i].toString());
            }
            Log.d(TAG, "onOptionsItemSelected: "+item.getTitle().toString());
            processNews("All");

        }
        if(item.getItemId()==R.id.languages){
            Toast.makeText(this, "Languages", Toast.LENGTH_SHORT).show();
            Object[] catAll = langs.toArray();
            for(int i=0;i<catAll.length;i++){
                item.getSubMenu().add(catAll[i].toString());
            }
        }
        if(item.getItemId()==R.id.countries){
            Toast.makeText(this, "Countries", Toast.LENGTH_SHORT).show();
            Object[] catAll = countries.toArray();
            for(int i=0;i<catAll.length;i++){
                item.getSubMenu().add(catAll[i].toString());
            }
        }
        Log.d(TAG, "onOptionsItemSelected: "+item.getTitle().toString());
        processNews(item.getTitle().toString());




      /*  else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        } */

      //  ((ArrayAdapter) mDrawerList.getAdapter()).notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    private void processNews(String topic){
        ArrayList<String> newsResources = new ArrayList<String>();
        if(topic.equals("All")){
            for(int i=0;i<newsList.size();i++){
                newsResources.add(newsList.get(i).getSourceName());
            }

        }
        else {
            for(int i=0;i<newsList.size();i++) {
                if(newsList.get(i).getCategory().equals(topic))
                    newsResources.add(newsList.get(i).getSourceName());
            }
        }
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, newsResources));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        opt_menu=menu;
        return true;
    }

    public void process(HashSet<String> categories, HashSet<String> languages, HashSet<String> countries, ArrayList<News> newsData) {
        this.categories = categories;
        this.langs = languages;
        this.countries = countries;
        this.newsList = newsData;
        Log.d(TAG, "process: "+categories.toString());
    }
}