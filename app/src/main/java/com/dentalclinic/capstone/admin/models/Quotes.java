package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Quotes implements Serializable {
    @SerializedName("USDUSD")
    private String USD;
    @SerializedName("USDVND")
    private String VND;

    public String getUSD() {
        return USD;
    }

    public void setUSD(String USD) {
        this.USD = USD;
    }

    public String getVND() {
        return VND;
    }

    public void setVND(String VND) {
        this.VND = VND;
    }
}
