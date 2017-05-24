package com.gmail.sanovikov71.tinkofftask.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Title {

    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
