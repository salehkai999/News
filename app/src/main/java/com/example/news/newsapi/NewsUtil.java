package com.example.news.newsapi;

import java.util.ArrayList;
import java.util.HashSet;

public class NewsUtil {
    private static ArrayList<News> allNews = new ArrayList<>();
    private static ArrayList<News> currentList = new ArrayList<>();
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

    public static void processLang(String lang){

    }
}
