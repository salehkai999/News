package com.example.news.newsapi;

import java.util.Objects;

public class News implements Comparable {
    private String sourceName;
    private String sourceId;
    private String category;
    private String language;
    private String country;

    public News() {
    }

    public News(String sourceName, String sourceId, String category, String language, String country) {
        this.sourceName = sourceName;
        this.sourceId = sourceId;
        this.category = category;
        this.language = language;
        this.country = country;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(sourceName, news.sourceName) &&
                Objects.equals(sourceId, news.sourceId) &&
                Objects.equals(category, news.category) &&
                Objects.equals(language, news.language) &&
                Objects.equals(country, news.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceName, sourceId, category, language, country);
    }

    @Override
    public String toString() {
        return "News{" +
                "sourceName='" + sourceName + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", category='" + category + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        News compareObject = (News)o;
        return this.getSourceName().compareTo(compareObject.getSourceName());
    }
}
