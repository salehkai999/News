package com.example.news.newsapi;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class NewsUtil {
    private static String TAG = "NewsUtil";
    private static ArrayList<News> allNews = new ArrayList<>();
    private static ArrayList<News> currentList = new ArrayList<>();
    private static HashMap<String,ArrayList<News>> langBasedList = new HashMap<>();
    private static HashMap<String,ArrayList<News>> countriesBasedList = new HashMap<>();
    private static HashMap<String,ArrayList<News>> topicsBasedList = new HashMap<>();
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

        Log.d(TAG, "processLang: "+codes.length);
        Log.d(TAG, "processLang: "+allNews.size());
       for(int i=0;i<codes.length;i++){
           langBasedList.put(codes[i].toString(),new ArrayList<>());
       }
       for(int i=0;i<allNews.size();i++){
           for(String s : langBasedList.keySet()){
               if(s.equalsIgnoreCase(allNews.get(i).getLanguage())){
                   ArrayList<News> list = langBasedList.get(s);
                   list.add(allNews.get(i));
               }
           }
       }
        Log.d(TAG, "processLang: "+langBasedList.keySet().toString());
       for(String s : langBasedList.keySet()){
           Log.d(TAG, "processLang: "+s+" : "+langBasedList.get(s).toString());
       }
        //processCategories();

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

    public static void processCategories(Object[] codes) {
        Log.d(TAG, "processCategories: "+codes.length);
        for(int i=0;i<codes.length;i++){
            topicsBasedList.put(codes[i].toString(),new ArrayList<>());
        }
        for(int i=0;i<allNews.size();i++){
            for(String s : topicsBasedList.keySet()){
                if(s.equalsIgnoreCase(allNews.get(i).getCategory())){
                    ArrayList<News> list = topicsBasedList.get(s);
                    list.add(allNews.get(i));
                }
            }
        }
        Log.d(TAG, "processCategories: "+topicsBasedList.keySet().toString());
        for(String s:topicsBasedList.keySet()){
            Log.d(TAG, "processCategories: "+s+" : "+topicsBasedList.get(s).toString());
        }
    }

    public static ArrayList<News> subListLangTopic (String category,String language){
        ArrayList<News> list = topicsBasedList.get(category);
        Log.d(TAG, "subListLangTopic: "+list.toString());
        ArrayList<News> returnList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getLanguage().equalsIgnoreCase(language)){
                Log.d(TAG, "subList: "+language+" : "+category+" : "+list.get(i).getSourceName());
                returnList.add(list.get(i));
            }
        }
        return returnList ;
    }

    public static ArrayList<News> subListCountry(String country){
        Log.d(TAG, "subListCountry: "+countriesBasedList.get(country));
        return countriesBasedList.get(country);
    }

    public static ArrayList<News> subListLangCountryTopic(String language,String topic, String country){
        ArrayList<News> list = countriesBasedList.get(country);
        ArrayList<News> returnList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getCategory().equalsIgnoreCase(topic) && list.get(i).getLanguage().equalsIgnoreCase(language)) {
                Log.d(TAG, "subListLangCountryTopic: "+list.get(i).toString());
                returnList.add(list.get(i));
            }
        }
        return returnList;
    }

    public static ArrayList<News> subListTopicCountry(String topic, String country){
        ArrayList<News> list = countriesBasedList.get(country);
        ArrayList<News> returnList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getCategory().equalsIgnoreCase(topic)) {
                Log.d(TAG, "subListTopicCountry: "+list.get(i).toString());
                returnList.add(list.get(i));
            }
        }
        return returnList;
    }

    public static ArrayList<News> subListTopic(String topicsFlag) {
        if(topicsFlag.equalsIgnoreCase("all")){
            return allNews;
        }
        return topicsBasedList.get(topicsFlag);
    }

    public static ArrayList<News> subListLang(String lang){
        return langBasedList.get(lang);
    }
}
