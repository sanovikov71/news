package com.gmail.sanovikov71.tinkofftask.ui.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gmail.sanovikov71.tinkofftask.R;
import com.gmail.sanovikov71.tinkofftask.Utils;
import com.gmail.sanovikov71.tinkofftask.network.model.details.DetailedNewsItem;
import com.gmail.sanovikov71.tinkofftask.network.DataManager;

public class NewsDetailsActivity extends AppCompatActivity implements UIDetails {

    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private static final String TAG = "PictureActivity";

    private int mItemId;
    private TextView mContent;

//    private ItemStorage mItemStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        mItemId = getIntent().getIntExtra(EXTRA_ITEM_ID, 0);

        final DataManager dataManager = new DataManager(this);

        mContent = (TextView) findViewById(R.id.news_content);

        dataManager.fetchOneItem(this, mItemId);
    }

    @Override
    public void update(DetailedNewsItem item) {
        //        Glide.with(this)
//                .load(item.getImgUrl())
//                .into(mImage);

        mContent.setText(Utils.fromHtml(item.getContent()));
    }

    @Override
    public void error() {

    }
}