package com.gmail.sanovikov71.tinkofftask.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsResponse {

    @SerializedName("payload")
    @Expose
    private NewsItem newsDetails;

    public NewsItem getNewsDetails() {
        return newsDetails;
    }

    public void setNewsDetails(NewsItem newsDetails) {
        this.newsDetails = newsDetails;
    }
}
