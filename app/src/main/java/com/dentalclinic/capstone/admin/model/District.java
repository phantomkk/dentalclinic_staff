package com.dentalclinic.capstone.admin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class District implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("city_id")
    private int cityId;
    @SerializedName("city")
    private City city;

    public District() {
    }

    public District(int id) {
        this.id = id;
    }

    public District(String name, City city) {
        this.name = name;
        this.city = city;
    }

    public District(int id, String name, City city) {
        this.id = id;
        this.name = name;
        this.city = city;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
