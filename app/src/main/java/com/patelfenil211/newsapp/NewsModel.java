package com.patelfenil211.newsapp;

public class NewsModel {

    //data members
    String sourceName, newsTitle, newsDescription, newsUrl, newsImage, publishedAt, newsContent;

    //constructor
    public NewsModel(String sourceName, String newsTitle, String newsDescription, String newsUrl, String newsImage,
                     String publishedAt, String newsContent) {
        this.sourceName = sourceName;
        this.newsTitle = newsTitle;
        this.newsDescription = newsDescription;
        this.newsUrl = newsUrl;
        this.newsImage = newsImage;
        this.publishedAt = publishedAt;
        this.newsContent = newsContent;
    }

    //getters
    public String getSourceName() {
        return sourceName;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getNewsContent() {
        return newsContent;
    }

}
