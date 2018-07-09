package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("image_header")
    private String newsImage;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("create_date")
    private String createDate;
    @SerializedName("staff")
    private Staff author;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Staff getAuthor() {
        return author;
    }

    public void setAuthor(Staff author) {
        this.author = author;
    }

}
