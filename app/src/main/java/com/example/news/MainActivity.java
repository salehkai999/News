package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.news.newsapi.News;
import com.example.news.newsapi.NewsData;
import com.example.news.newsapi.NewsDataRunnable;
import com.example.news.newsapi.NewsRunnable;
import com.example.news.newsapi.NewsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


// e4e9821da29e405090b31e44d09bfe7a
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private HashSet categories;
    private HashSet langs;
    private HashSet countries;
    private static ArrayList<News> newsList;
    private static HashSet<News> currentList = new HashSet<>();
    private static HashMap<String,String> countryNames = new HashMap<>();
    private static HashMap<String,String> langsMap = new HashMap<>();
    private static ArrayList<String> countNamesList = new ArrayList<>();
    private static ArrayList<String> lanuagesNames = new ArrayList<>();
    private static ArrayList<News> currentArrayList = new ArrayList<>();
    private static ArrayList<String> newsResources = new ArrayList<String>();
    private static HashSet<News> tempCurrent = new HashSet<>();
    private List<Fragment> fragments;
    private MyPageAdapter pageAdapter;
    private ViewPager pager;
    private String topicsFlag="";
    private String languageFlag="";
    private String countryFlag="";
    private int numFlag=0;
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
                    Log.d(TAG, "onCreate: "+currentArrayList.get(position).toString());
                    new Thread(new NewsDataRunnable(this,currentArrayList.get(position).getSourceId())).start();
                    setTitle(currentArrayList.get(position).getSourceName());
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

        fragments = new ArrayList<>();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

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
            if(numFlag==0)
                item.getSubMenu().add("All");
            Object[] catAll = categories.toArray();
            for(int i=0;i<catAll.length;i++){
                item.getSubMenu().add(catAll[i].toString().toUpperCase());
            }
            Log.d(TAG, "onOptionsItemSelected: "+item.getTitle().toString());
            categories.clear();
            numFlag = 1;
          //  processNews("All");

        }
        else if(item.getItemId()==R.id.languages){
            numFlag=2;
            Toast.makeText(this, "Languages", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onOptionsItemSelected: LANG "+item.getItemId());
            Object[] catAll = langs.toArray();
            for(String s : lanuagesNames){
                item.getSubMenu().add(s);
            }
            lanuagesNames.clear();

        }
        else if(item.getItemId()==R.id.countries){
            numFlag=3;
            Toast.makeText(this, "Countries", Toast.LENGTH_SHORT).show();
            Object[] catAll = countries.toArray();
            for(String s : countNamesList){
                item.getSubMenu().add(s);
            }
            countNamesList.clear();
        }
        else {
            Log.d(TAG, "onOptionsItemSelected: " + item.getTitle().toString());
            Log.d(TAG, "onOptionsItemSelected: ID " + item.getItemId());
            switch (numFlag) {
                case 1:
                    topicsFlag = item.getTitle().toString().toLowerCase();
                    processNews(topicsFlag);
                //    processNewsTopics(topicsFlag);
                    break;
                case 2:
                    languageFlag = langsMap.get(item.getTitle().toString());
                    processNews(languageFlag);
                //    processNewsLanguage(languageFlag);
                    break;
                case 3:
                    countryFlag = countryNames.get(item.getTitle().toString());
                    Log.d(TAG, "onOptionsItemSelected: "+countryFlag);
                    processNews(countryFlag);
                   // processNewsCountry(countryFlag);
                    break;
            }
            if(!topicsFlag.isEmpty() && (!languageFlag.isEmpty())) {
                Log.d(TAG, "onOptionsItemSelected: ");
                NewsUtil.subList(topicsFlag, languageFlag);
            }
            if(!topicsFlag.isEmpty() && (!languageFlag.isEmpty()) && (!countryFlag.isEmpty())) {
                NewsUtil.subListLangCountryTopic(languageFlag,topicsFlag,countryFlag);
            }
            if(!countryFlag.isEmpty() && topicsFlag.isEmpty() && languageFlag.isEmpty()){
                NewsUtil.subListCountry(countryFlag);
            }
            //processNewsAll(topicsFlag,languageFlag,countryFlag);
        }




      /*  else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        } */

      //  ((ArrayAdapter) mDrawerList.getAdapter()).notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    private void processNewsAll(String topicsFlag, String languageFlag, String countryFlag) {
        currentArrayList.clear();
        currentList.clear();
        newsResources.clear();
        if(topicsFlag.isEmpty())
        {

        }
    }


    private void processNewsCountry(String countryFlag) {
        if(!currentList.isEmpty()){
            for(News n : currentList){
                if(!n.getCountry().equals(countryFlag))
                    tempCurrent.remove(n);
            }
        }
        else{
            for(int i=0;i<newsList.size();i++){
                if(newsList.get(i).getCountry().equals(countryFlag))
                    currentList.add(newsList.get(i));
            }

        }
        newsResources.clear();
        currentArrayList.clear();
        for(News n : tempCurrent){
            newsResources.add(n.getSourceName());
            currentArrayList.add(n);
        }
        Collections.sort(newsResources);
        Collections.sort(currentArrayList);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, newsResources));
    }

    private void processNewsLanguage(String langFlag) {
        if(!currentList.isEmpty()){
            for(News n : currentList){
                if(!n.getLanguage().equals(langFlag))
                    tempCurrent.remove(n);
            }
        }
        else{
            for(int i=0;i<newsList.size();i++){
                if(newsList.get(i).getLanguage().equals(langFlag))
                    currentList.add(newsList.get(i));
            }

        }
        newsResources.clear();
        currentList.clear();
        for(News n : tempCurrent){
            newsResources.add(n.getSourceName());
            currentArrayList.add(n);
        }
        Collections.sort(newsResources);
        Collections.sort(currentArrayList);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, newsResources));

    }

    private void processNewsTopics(String topicsFlag) {
        if(!currentList.isEmpty()){
            for(News n : currentList){
                if(!n.getCategory().equals(topicsFlag))
                    tempCurrent.remove(n);
            }
        }
        else{
            for(int i=0;i<newsList.size();i++){
                if(newsList.get(i).getCategory().equals(topicsFlag))
                    currentList.add(newsList.get(i));
            }

        }
        newsResources.clear();
        currentArrayList.clear();
        for(News n : tempCurrent){
            newsResources.add(n.getSourceName());
            currentArrayList.add(n);
        }
        Collections.sort(newsResources);
        Collections.sort(currentArrayList);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, newsResources));
    }


    private void processNews(String topic){

        currentList.clear();
        newsResources.clear();
        currentArrayList.clear();
        Log.d(TAG, "processNews: TopicsFalg: "+topicsFlag);
        Log.d(TAG, "processNews: LanguageFalg: "+languageFlag);
        Log.d(TAG, "processNews: CountryFalg: "+countryFlag);
        if(topic.equals("All")){
            for(int i=0;i<newsList.size();i++){
                //newsResources.add(newsList.get(i).getSourceName());
                currentList.add(newsList.get(i));
            }

        }
        else {
            for(int i=0;i<newsList.size();i++) {
                if(newsList.get(i).getCategory().equals(topic)) {
                    currentList.add(newsList.get(i));
                }
                if(newsList.get(i).getCountry().equals(topic)) {
                    currentList.add(newsList.get(i));
                }
                if(newsList.get(i).getLanguage().equals(topic)){
                    currentList.add(newsList.get(i));
                }
            }
        }
        for(News n : currentList){
            newsResources.add(n.getSourceName());
            tempCurrent.add(n);
            currentArrayList.add(n);
        }
        Collections.sort(newsResources);
        Collections.sort(currentArrayList);

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
        processNews("All");
        try {
            NewsUtil.setAllNews(newsList);
            processRAW();
            processRAWLanguages();
        }
        catch (Exception e){
            Log.d(TAG, "process: "+e.getMessage());
        }
    }

    private void processRAW() throws IOException, JSONException {
        countNamesList.clear();
        Log.d(TAG, "processRAW: ");
        InputStream is = this.getResources().openRawResource(R.raw.country_codes);
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
        //    Log.d(TAG, "processRAW: "+line);
            line = reader.readLine();
        }
        reader.close();
        JSONObject codesObj = new JSONObject(sb.toString());
        Log.d(TAG, "processRAW: "+codesObj.toString());
        JSONArray codesArray = codesObj.getJSONArray("countries");
        Object[] codes = countries.toArray();
        for(int i=0;i<codes.length;i++) {
            for(int j=0;j<codesArray.length();j++) {
                if(codes[i].toString().equalsIgnoreCase(codesArray.getJSONObject(j).getString("code"))){
                    Log.d(TAG, "processRAW: "+codesArray.getJSONObject(j).getString("name"));
                    countryNames.put(codesArray.getJSONObject(j).getString("name"),codes[i].toString());
                }
            }
        }
        NewsUtil.processCountries(codes);
        for(String s : countryNames.keySet()){
            countNamesList.add(s);
        }
        Collections.sort(countNamesList);
        Log.d(TAG, "processRAW: "+countryNames.toString());


    }


    private void processRAWLanguages() throws IOException, JSONException {
        lanuagesNames.clear();
        InputStream is = this.getResources().openRawResource(R.raw.language_codes);
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            //    Log.d(TAG, "processRAW: "+line);
            line = reader.readLine();
        }
        reader.close();
        JSONObject codesObj = new JSONObject(sb.toString());
        Log.d(TAG, "processRAW: "+codesObj.toString());
        JSONArray codesArray = codesObj.getJSONArray("languages");
        Object[] codes = langs.toArray();
        for(int i=0;i<codes.length;i++) {
            for(int j=0;j<codesArray.length();j++) {
                if(codes[i].toString().equalsIgnoreCase(codesArray.getJSONObject(j).getString("code"))){
                    Log.d(TAG, "processRAW: "+codesArray.getJSONObject(j).getString("name"));
                    langsMap.put(codesArray.getJSONObject(j).getString("name"),codes[i].toString());
                }
            }
        }
        for(String s : langsMap.keySet()){
            lanuagesNames.add(s);
        }
        Collections.sort(lanuagesNames);
        //NewsUtil.setAllNews(newsList);
        NewsUtil.processLang(codes);
        Log.d(TAG, "processRAW: "+lanuagesNames.toString());

    }

    public void addArticles(ArrayList<NewsData> articles) {

        for(int i=0;i<pageAdapter.getCount();i++){
            pageAdapter.notifyChangeInPosition(i);
        }
        fragments.clear();
        for(int i=0;i<articles.size();i++){
            fragments.add(NewsFragment.newInstance(articles.get(i),i+1,articles.size()));
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);

    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        MyPageAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }

}