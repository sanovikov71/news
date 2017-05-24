package com.gmail.sanovikov71.tinkofftask.network;

import com.gmail.sanovikov71.tinkofftask.network.model.DetailsResponse;
import com.gmail.sanovikov71.tinkofftask.network.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface Endpoint {

    @GET("news")
    Call<NewsResponse> fetchNewsList();

    @GET("news_content")
    Call<DetailsResponse> fetchNewsItem(@Query("id") int id);

}