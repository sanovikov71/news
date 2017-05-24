package com.gmail.sanovikov71.tinkofftask.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.gmail.sanovikov71.tinkofftask.network.model.DetailsResponse;
import com.gmail.sanovikov71.tinkofftask.network.model.NewsItem;
import com.gmail.sanovikov71.tinkofftask.network.model.NewsResponse;
import com.gmail.sanovikov71.tinkofftask.storage.NewsProvider;
import com.gmail.sanovikov71.tinkofftask.storage.NewsTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataManager extends IntentService {

    public static final String ACTION_FETCH_LIST
            = "com.gmail.sanovikov71.tinkofftask.ACTION_FETCH_LIST";
    public static final String ACTION_FETCH_ITEM
            = "com.gmail.sanovikov71.tinkofftask.ACTION_FETCH_ITEM";
    public static final String EXTRA_ITEM_ID
            = "com.gmail.sanovikov71.tinkofftask.EXTRA_ITEM_ID";

    public static final String ACTION_FETCH_OK
            = "com.gmail.sanovikov71.tinkofftask.ACTION_FETCH_OK";
    public static final String ACTION_FETCH_ERROR
            = "com.gmail.sanovikov71.tinkofftask.ACTION_FETCH_ERROR";

    private static final String API_URL = "https://api.tinkoff.ru/v1/";

    private Endpoint server;

    public DataManager() {
        super(DataManager.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        server = retrofit.create(Endpoint.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        if (ACTION_FETCH_LIST.equals(action)) {
            downLoadList();
        } else if (ACTION_FETCH_ITEM.equals(action)) {
            downLoadOneItem(intent.getIntExtra(EXTRA_ITEM_ID, 0));
        }

    }

    private void downLoadList() {
        server.fetchNewsList().enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                final NewsResponse body = response.body();
                if (response.isSuccessful()) {
                    final List<NewsItem> newsItemList = body.getNewsItemList();

                    broadcastSuccess();

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
                    getContentResolver().bulkInsert(NewsProvider.NEWS_CONTENT_URI, cvs);
                } else {
                    broadcastError();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                broadcastError();
            }

        });
    }

    private void downLoadOneItem(final int id) {
        server.fetchNewsItem(id).enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                final DetailsResponse body = response.body();
                if (response.isSuccessful()) {
                    final NewsItem newsDetails = body.getNewsDetails();

                    broadcastSuccess();

                    final ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsTable.COLUMN_BACKEND_ID, newsDetails.getId());
                    contentValues.put(NewsTable.COLUMN_TEXT, newsDetails.getText());
                    contentValues.put(NewsTable.COLUMN_CONTENT, newsDetails.getContent());

                    getContentResolver().update(
                            NewsProvider.NEWS_CONTENT_URI,
                            contentValues,
                            NewsTable.COLUMN_BACKEND_ID + " = " + newsDetails.getId(),
                            null
                    );

                } else {
                    broadcastError();
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                broadcastError();
            }

        });
    }

    private void broadcastSuccess() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_FETCH_OK));
    }

    private void broadcastError() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_FETCH_ERROR));
    }

    public static void fetchList(Context context) {
        Intent intent = new Intent(context, DataManager.class);
        intent.setAction(ACTION_FETCH_LIST);
        context.startService(intent);
    }

    public static void fetchOneItem(Context context, int itemId) {
        Intent intent = new Intent(context, DataManager.class);
        intent.setAction(ACTION_FETCH_ITEM);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        context.startService(intent);
    }

}

