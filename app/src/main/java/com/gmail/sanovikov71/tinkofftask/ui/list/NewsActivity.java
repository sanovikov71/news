package com.gmail.sanovikov71.tinkofftask.ui.list;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import com.gmail.sanovikov71.tinkofftask.R;
import com.gmail.sanovikov71.tinkofftask.network.DataManager;
import com.gmail.sanovikov71.tinkofftask.network.model.list.NewsItem;
import com.gmail.sanovikov71.tinkofftask.storage.NewsTable;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements UIList, LoaderManager.LoaderCallbacks<Cursor> {

    private NewsAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        final RecyclerView list = (RecyclerView) findViewById(R.id.news_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        mAdapter = new NewsAdapter(this);
        list.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(list.getContext(), layoutManager.getOrientation());
        list.addItemDecoration(dividerItemDecoration);

        final DataManager dataManager = new DataManager(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataManager.fetchList(NewsActivity.this);
            }
        });

        dataManager.fetchList(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void update(List<NewsItem> list) {
        mSwipeRefreshLayout.setRefreshing(false);
//        mAdapter.updateDataset(list);
    }

    @Override
    public void error() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<NewsItem> dataList = new ArrayList<>();

        try {
            while (data.moveToNext()) {
                final int id = data.getInt(data.getColumnIndex(NewsTable.COLUMN_ID));
                final String text = data.getString(data.getColumnIndex(NewsTable.COLUMN_TEXT));
                final String content = data.getString(data.getColumnIndex(NewsTable.COLUMN_CONTENT));
            }
        } finally {
            data.close();
        }


        for (int i = 0; i < data.getCount(); i++) {
            dataList.add(new NewsItem(data.get);


        }
        mAdapter.updateDataset(dataList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
