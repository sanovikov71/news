package com.gmail.sanovikov71.tinkofftask.ui.list;

import com.gmail.sanovikov71.tinkofftask.network.model.list.NewsItem;

import java.util.List;

public interface UIList {
    void update(List<NewsItem> list);

    void error();
}