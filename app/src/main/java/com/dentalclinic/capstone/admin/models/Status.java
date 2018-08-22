package com.dentalclinic.capstone.admin.models;

import android.support.annotation.NonNull;

import com.yalantis.filter.model.FilterModel;

/**
 * Created by galata on 16.09.16.
 */
public class Status implements FilterModel {
    private int id;
    private String text;
    private int color;

    public Status(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public Status(int id, String text, int color) {
        this.id = id;
        this.text = text;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status)) return false;

        Status tag = (Status) o;

        if (getColor() != tag.getColor()) return false;
        return getText().equals(tag.getText());

    }

    @Override
    public int hashCode() {
        int result = getText().hashCode();
        result = 31 * result + getColor();
        return result;
    }

}
