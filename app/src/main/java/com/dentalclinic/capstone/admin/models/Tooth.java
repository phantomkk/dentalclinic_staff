package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tooth implements Serializable {
    @SerializedName("tooth_number")
    private int toothNumber;
    @SerializedName("tooth_name")
    private String toothName;

    public Tooth(String toothName) {
        this.toothName = toothName;
    }

    public int getToothNumber() {
        return toothNumber;
    }

    public void setToothNumber(int toothNumber) {
        this.toothNumber = toothNumber;
    }

    public String getToothName() {
        return toothName;
    }

    public void setToothName(String toothName) {
        this.toothName = toothName;
    }
}
