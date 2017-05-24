package com.gmail.sanovikov71.tinkofftask.ui.details;

import com.gmail.sanovikov71.tinkofftask.network.model.details.DetailedNewsItem;

public interface UIDetails {
    void update(DetailedNewsItem item);

    void error();
}
