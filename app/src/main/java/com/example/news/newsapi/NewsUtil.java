package com.example.news.newsapi;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class NewsUtil {
    private static String TAG = "NewsUtil";
    private static ArrayList<News> allNews = new ArrayList<>();
    private static ArrayList<News> currentList = new ArrayList<>();
    private static HashMap<String,ArrayList<News>> idk = new HashMap<>();
    private static HashMap<String,ArrayList<News>> countriesBasedList = new HashMap<>();
    private static String category = "";
    private static String oldCategory = "";


    public static ArrayList<News> getAllNews() {
        return allNews;
    }

    public static void setAllNews(ArrayList<News> allNews) {
        NewsUtil.allNews = allNews;
    }

    public static ArrayList<News> getCurrentList() {
        return currentList;
    }

    public static void setCurrentList(ArrayList<News> currentList) {
        NewsUtil.currentList = currentList;
    }

    public static void setCategory(String category) {
        NewsUtil.category = category;
    }

    public static String getCategory() {
        return category;
    }

    public static void setOldCategory(String oldCategory) {
        if(!oldCategory.isEmpty())
            NewsUtil.oldCategory = oldCategory;
    }

    public static void processLang(Object[] codes){

       for(int i=0;i<codes.length;i++){
           idk.put(codes[i].toString(),new ArrayList<>());
       }
        Log.d(TAG, "processLang: "+idk.keySet().toString());
        processCategories();

    }

    public static void processCountries(Object[] codes){
        for(int i=0;i<codes.length;i++){
            countriesBasedList.put(codes[i].toString(),new ArrayList<>());
        }
        for(int i=0;i<allNews.size();i++){
            for(String s : countriesBasedList.keySet())
            {
                if(s.equalsIgnoreCase(allNews.get(i).getCountry())) {
                    ArrayList<News> list = countriesBasedList.get(s);
                    list.add(allNews.get(i));
                }
            }
        }
        Log.d(TAG, "processCountries: "+countriesBasedList.keySet().toString());
        for(String s : countriesBasedList.keySet()) {
            Log.d(TAG, "processCountries: "+s+" : "+countriesBasedList.get(s).toString());
        }
    }

    private static void processCategories() {
        for(int i=0;i<allNews.size();i++){
            for(String s : idk.keySet()){
                if(s.equalsIgnoreCase(allNews.get(i).getLanguage())) {
                    ArrayList<News> List = idk.get(s);
                    List.add(allNews.get(i));
                }
            }
        }
        for(String s : idk.keySet()){
            Log.d(TAG, "processCategories: "+s+" : "+idk.get(s).toString());
        }
        //Log.d(TAG, "processCategories: ");
    }

    public static void subList (String category,String language){
        ArrayList<News> list = idk.get(language);
        for(int i=0;i<list.size();i++){
            if(list.get(i).getCategory().equalsIgnoreCase(category)){
                Log.d(TAG, "subList: "+language+" : "+category+" : "+list.get(i).getSourceName());
            }
        }
    }

    public static void subListCountry(String country){
        Log.d(TAG, "subListCountry: "+countriesBasedList.get(country));
    }

    public static void subListLangCountryTopic(String language,String topic, String country){
        ArrayList<News> list = idk.get(language);
        for(int i=0;i<list.size();i++){
            if(list.get(i).getCategory().equalsIgnoreCase(topic) && list.get(i).getCountry().equalsIgnoreCase(country)) {
                Log.d(TAG, "subListLangCountryTopic: "+list.get(i).toString());
            }
        }
    }

}
