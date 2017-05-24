package com.gmail.sanovikov71.tinkofftask.ui.details;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gmail.sanovikov71.tinkofftask.R;
import com.gmail.sanovikov71.tinkofftask.Utils;
import com.gmail.sanovikov71.tinkofftask.network.DataManager;
import com.gmail.sanovikov71.tinkofftask.network.model.NewsItem;
import com.gmail.sanovikov71.tinkofftask.storage.NewsProvider;
import com.gmail.sanovikov71.tinkofftask.storage.NewsTable;

public class NewsDetailsActivity extends AppCompatActivity implements UIDetails,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER_ID = 75;

    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private int mItemId;
    private TextView mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        mItemId = getIntent().getIntExtra(EXTRA_ITEM_ID, 0);

        final DataManager dataManager = new DataManager(this);

        mContent = (TextView) findViewById(R.id.news_content);

        dataManager.fetchOneItem(this, mItemId);

        getSupportLoaderManager().initLoader(ITEM_LOADER_ID, null, this);
    }

    @Override
    public void update(NewsItem item) {
    }

    @Override
    public void error() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                NewsProvider.NEWS_CONTENT_URI,
                null,
                NewsTable.COLUMN_BACKEND_ID + " = " + mItemId,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        DatabaseUtils.dumpCursor(data);
        data.moveToFirst();
        final String content = data.getString(data.getColumnIndex(NewsTable.COLUMN_CONTENT));
        mContent.setText(Utils.fromHtml(content));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}