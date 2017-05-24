package com.gmail.sanovikov71.tinkofftask.network.model.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailedNewsItem {

    @SerializedName("content")
    @Expose
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
