package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Prescription implements Serializable {
    @SerializedName("patient")
    private Patient patient;
    @SerializedName("staff")
    private Staff dentist;
    @SerializedName("medicine")
    private Medicine medicine;
    @SerializedName("treatment_detail")
    private TreatmentDetail treatm;
    @SerializedName("qualtity")
    private int qualtity;

    public Prescription(Medicine medicine) {
        this.medicine = medicine;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Staff getDentist() {
        return dentist;
    }

    public void setDentist(Staff dentist) {
        this.dentist = dentist;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public TreatmentDetail getTreatm() {
        return treatm;
    }

    public void setTreatm(TreatmentDetail treatm) {
        this.treatm = treatm;
    }

    public int getQualtity() {
        return qualtity;
    }

    public void setQualtity(int qualtity) {
        this.qualtity = qualtity;
    }
}
