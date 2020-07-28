package com.rss.newsreader.models;

import com.rss.newsreader.datas.News;
import com.rss.newsreader.utils.NewsResultListener;

import java.util.ArrayList;

public class NewsStore {
    private ArrayList<News> newsList;

    public NewsStore(){
        newsList = new ArrayList<>();
    }

    public void update(ArrayList<News> newsList){
        this.newsList = newsList;
    }

    public ArrayList<News> getNewsList(){
        //sort
        return this.newsList;
    }

    public void clear(){
        this.newsList.clear();
    }

    public void addNews(News news){
        this.newsList.add(news);
    }

}

