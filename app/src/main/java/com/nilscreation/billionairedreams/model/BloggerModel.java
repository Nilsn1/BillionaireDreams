package com.nilscreation.billionairedreams.model;

import com.google.gson.annotations.SerializedName;

public class BloggerModel {

    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;

    public BloggerModel() {
    }

    public BloggerModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
