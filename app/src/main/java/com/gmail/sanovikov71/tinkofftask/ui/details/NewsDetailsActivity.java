package com.gmail.sanovikov71.tinkofftask.ui.details;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.sanovikov71.tinkofftask.R;
import com.gmail.sanovikov71.tinkofftask.Utils;
import com.gmail.sanovikov71.tinkofftask.network.DataManager;
import com.gmail.sanovikov71.tinkofftask.storage.NewsProvider;
import com.gmail.sanovikov71.tinkofftask.storage.NewsTable;
import com.gmail.sanovikov71.tinkofftask.ui.ServerListenerActivity;

public class NewsDetailsActivity extends ServerListenerActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER_ID = 75;

    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private int itemId;
    private TextView content;
    private ScrollView scrollView;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, 0);

        content = (TextView) findViewById(R.id.news_content);
        scrollView = (ScrollView) findViewById(R.id.news_scroll_view);
        progressBar = (ProgressBar) findViewById(R.id.news_progress_bar);

        DataManager.fetchOneItem(this, itemId);

        getSupportLoaderManager().initLoader(ITEM_LOADER_ID, null, this);

        showProgressBar();
    }

    @Override
    protected void onFetchOk() {
        // can be used for UI updates
    }

    @Override
    protected void onFetchError() {
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
                NewsTable.COLUMN_BACKEND_ID + " = " + itemId,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 1) {
            return;
        }

        showContent();

        data.moveToFirst();

        final String content = data.getString(data.getColumnIndex(NewsTable.COLUMN_CONTENT));
        this.content.setText(Utils.fromHtml(content));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
    }

    private void showContent() {
        progressBar.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }
}