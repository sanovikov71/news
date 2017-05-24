package com.gmail.sanovikov71.tinkofftask.ui.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.sanovikov71.tinkofftask.R;
import com.gmail.sanovikov71.tinkofftask.Utils;
import com.gmail.sanovikov71.tinkofftask.network.model.NewsItem;
import com.gmail.sanovikov71.tinkofftask.ui.details.NewsDetailsActivity;

import java.util.ArrayList;
import java.util.List;

class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private List<NewsItem> mData = new ArrayList<>();

    NewsAdapter(Context context) {
        mContext = context;
    }

    void updateDataset(List<NewsItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder typedHolder = (ViewHolder) holder;

        NewsItem item = mData.get(position);

        typedHolder.mItemId = item.getId();
        typedHolder.mText.setText(Utils.fromHtml(item.getText()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        int mItemId;
        TextView mText;

        ViewHolder(final Context context, View view) {
            super(view);
            mText = (TextView) view.findViewById(R.id.item_text);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewsDetailsActivity.class);
                    intent.putExtra(NewsDetailsActivity.EXTRA_ITEM_ID, mItemId);
                    context.startActivity(intent);
                }
            });
        }
    }

}
