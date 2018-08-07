package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BarChartData implements Serializable {
    @SerializedName("total_price")
    private Long money;
    @SerializedName("staff_id")
    private int treatmentId;
    @SerializedName("staff_name")
    private String name;

    public BarChartData(Long money, int treatmentId, String name) {
        this.money = money;
        this.treatmentId = treatmentId;
        this.name = name;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
