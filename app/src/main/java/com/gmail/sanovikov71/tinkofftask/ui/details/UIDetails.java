package com.gmail.sanovikov71.tinkofftask.ui.details;

import com.gmail.sanovikov71.tinkofftask.network.model.NewsItem;

public interface UIDetails {
    void update(NewsItem item);

    void error();
}
