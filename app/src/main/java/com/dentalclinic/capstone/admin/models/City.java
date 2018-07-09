package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class City implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public City(String name) {
        this.name = name;
    }

    public City() {
    }

    public City(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
