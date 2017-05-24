package com.gmail.sanovikov71.tinkofftask.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsItem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("publicationDate")
    @Expose
    private PublicationDate publicationDate;
    @SerializedName("title")
    @Expose
    private Title title;

    public NewsItem(int id, String text, String content, PublicationDate publicationDate) {
        this.id = id;
        this.text = text;
        this.content = content;
        this.publicationDate = publicationDate;
    }

    public Integer getId() {
        return id != null ? id : title.getId();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text != null ? text : title.getText();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PublicationDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(PublicationDate publicationDate) {
        this.publicationDate = publicationDate;
    }

}
