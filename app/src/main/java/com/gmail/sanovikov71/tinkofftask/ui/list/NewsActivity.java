package com.gmail.sanovikov71.tinkofftask.ui.list;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.sanovikov71.tinkofftask.R;
import com.gmail.sanovikov71.tinkofftask.network.DataManager;
import com.gmail.sanovikov71.tinkofftask.network.model.NewsItem;
import com.gmail.sanovikov71.tinkofftask.network.model.PublicationDate;
import com.gmail.sanovikov71.tinkofftask.storage.NewsProvider;
import com.gmail.sanovikov71.tinkofftask.storage.NewsTable;
import com.gmail.sanovikov71.tinkofftask.ui.ServerListenerActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends ServerListenerActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LIST_LOADER_ID = 25;

    private NewsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyView;

    private RecyclerView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        emptyView = (TextView) findViewById(R.id.empty_view);

        list = (RecyclerView) findViewById(R.id.news_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);

        adapter = new NewsAdapter(this);
        list.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(list.getContext(), linearLayoutManager.getOrientation());
        list.addItemDecoration(dividerItemDecoration);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DataManager.fetchList(NewsActivity.this);
            }
        });

        DataManager.fetchList(NewsActivity.this);

        getSupportLoaderManager().initLoader(LIST_LOADER_ID, null, this);

        showEmptyView();
    }

    @Override
    protected void onFetchOk() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onFetchError() {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(
                this,
                R.string.network_error_message,
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                NewsProvider.NEWS_CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() > 0) {
            showList();
        } else {
            showEmptyView();
            return;
        }

        List<NewsItem> dataList = new ArrayList<>();

        data.moveToFirst();
        while (data.moveToNext()) {
            final int id = data.getInt(data.getColumnIndex(NewsTable.COLUMN_BACKEND_ID));
            final String text = data.getString(data.getColumnIndex(NewsTable.COLUMN_TEXT));
            final String content = data.getString(data.getColumnIndex(NewsTable.COLUMN_CONTENT));
            final long milliseconds
                    = data.getLong(data.getColumnIndex(NewsTable.COLUMN_PUBLICATION_DATE));
            dataList.add(new NewsItem(id, text, content, new PublicationDate(milliseconds)));
        }

        adapter.updateDataset(dataList);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
    }

    private void showList() {
        emptyView.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
    }
}
