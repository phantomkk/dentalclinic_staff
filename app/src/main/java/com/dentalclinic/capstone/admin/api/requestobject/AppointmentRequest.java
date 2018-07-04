package com.dentalclinic.capstone.admin.api.requestobject;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppointmentRequest implements Serializable {

    @SerializedName("booking_date")
    private String date;
    @SerializedName("phone")
    private String phone;
    @SerializedName("note")
    private String note;
    @SerializedName("name")
    private String fullname;

    public AppointmentRequest() {
    }

    public AppointmentRequest(String date, String phone, String note, String fullname) {
        this.date = date;
        this.phone = phone;
        this.note = note;
        this.fullname = fullname;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
