package com.gmail.sanovikov71.tinkofftask.network.model.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsResponse {

    @SerializedName("payload")
    @Expose
    private DetailedNewsItem newsDetails;

    public DetailedNewsItem getNewsDetails() {
        return newsDetails;
    }

    public void setNewsDetails(DetailedNewsItem newsDetails) {
        this.newsDetails = newsDetails;
    }
}
