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
    @SerializedName("dentist_id")
    private String staffId = null;
    @SerializedName("patient_id")
    private String patientId = null;
    @SerializedName("estimated_time")
    private String estimatedTime;

    public AppointmentRequest() {
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

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
