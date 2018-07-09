package com.dentalclinic.capstone.admin.api.requestobject;

import com.google.gson.annotations.SerializedName;

public class BookApptRequest {

    @SerializedName("phone")
    private String phone;
    @SerializedName("dentist_id")
    private int dentistId;
    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("booking_date")
    private String dateBooking;
    @SerializedName("note")
    private String note;
    @SerializedName("estimated_time")
    private String estimatedTime;


    public String getDateBooking() {
        return dateBooking;
    }

    public void setDateBooking(String dateBooking) {
        this.dateBooking = dateBooking;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
