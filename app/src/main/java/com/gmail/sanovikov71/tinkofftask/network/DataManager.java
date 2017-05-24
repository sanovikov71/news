package com.gmail.sanovikov71.tinkofftask.network;

import android.content.ContentValues;
import android.content.Context;

import com.gmail.sanovikov71.tinkofftask.network.model.DetailsResponse;
import com.gmail.sanovikov71.tinkofftask.network.model.NewsItem;
import com.gmail.sanovikov71.tinkofftask.network.model.NewsResponse;
import com.gmail.sanovikov71.tinkofftask.storage.NewsProvider;
import com.gmail.sanovikov71.tinkofftask.storage.NewsTable;
import com.gmail.sanovikov71.tinkofftask.ui.details.UIDetails;
import com.gmail.sanovikov71.tinkofftask.ui.list.UIList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DataManager {
    private static final String API_URL = "https://api.tinkoff.ru/v1/";

    private Context context;
    private Endpoint server;

    public DataManager(Context context) {
        this.context = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        server = retrofit.create(Endpoint.class);
    }

    public void fetchList(final UIList uiList) {
        server.fetchNewsList().enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                final NewsResponse body = response.body();
                if (response.isSuccessful()) {
                    final List<NewsItem> newsItemList = body.getNewsItemList();
                    uiList.update();

                    ContentValues[] cvs = new ContentValues[newsItemList.size()];
                    for (int i = 0; i < cvs.length; i++) {
                        final ContentValues contentValues = new ContentValues();
                        final NewsItem newsItem = newsItemList.get(i);

                        contentValues.put(NewsTable.COLUMN_BACKEND_ID, newsItem.getId());
                        contentValues.put(NewsTable.COLUMN_TEXT, newsItem.getText());
                        contentValues.put(NewsTable.COLUMN_CONTENT, newsItem.getContent());
                        contentValues.put(NewsTable.COLUMN_PUBLICATION_DATE,
                                newsItem.getPublicationDate().getMilliseconds());

                        cvs[i] = contentValues;
                    }
                    context.getContentResolver().bulkInsert(NewsProvider.NEWS_CONTENT_URI, cvs);
                } else {
                    uiList.error();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                uiList.error();
            }

        });
    }

    public void fetchOneItem(final UIDetails uiDetails, final int id) {
        server.fetchNewsItem(id).enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                final DetailsResponse body = response.body();
                if (response.isSuccessful()) {
                    final NewsItem newsDetails = body.getNewsDetails();
                    uiDetails.update(newsDetails);

                    final ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsTable.COLUMN_BACKEND_ID, newsDetails.getId());
                    contentValues.put(NewsTable.COLUMN_TEXT, newsDetails.getText());
                    contentValues.put(NewsTable.COLUMN_CONTENT, newsDetails.getContent());

                    context.getContentResolver().update(
                            NewsProvider.NEWS_CONTENT_URI,
                            contentValues,
                            NewsTable.COLUMN_BACKEND_ID + " = " + newsDetails.getId(),
                            null
                    );

                } else {
                    uiDetails.error();
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                uiDetails.error();
            }

        });
    }

}

