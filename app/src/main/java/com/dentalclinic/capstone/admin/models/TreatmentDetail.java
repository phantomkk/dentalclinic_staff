package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TreatmentDetail implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("treatment_history")
    private TreatmentHistory treatmentHistory;
    @SerializedName("note")
    private String note;
    @SerializedName("dentist")
    private Staff dentist;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("payment")
    private Payment payment;
    @SerializedName("treatment_detail_steps")
    private List<TreatmentDetailStep> steps;
    @SerializedName("treatment_images")
    private List<TreatmentImage> images;
    @SerializedName("treatment")
    private Treatment treatment;
    @SerializedName("prescriptions")
    private List<Prescription> prescriptions;

    public TreatmentDetail() {
    }

    public TreatmentDetail(int id, TreatmentHistory treatmentHistory, String note, Staff dentist, String createdDate, Payment payment, List<TreatmentDetailStep> steps, List<TreatmentImage> images, Treatment treatment, List<Prescription> prescriptions) {
        this.id = id;
        this.treatmentHistory = treatmentHistory;
        this.note = note;
        this.dentist = dentist;
        this.createdDate = createdDate;
        this.payment = payment;
        this.steps = steps;
        this.images = images;
        this.treatment = treatment;
        this.prescriptions = prescriptions;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public int getId() {
        return id;
    }

    public TreatmentDetail(String note) {
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TreatmentHistory getTreatmentHistory() {
        return treatmentHistory;
    }

    public void setTreatmentHistory(TreatmentHistory treatmentHistory) {
        this.treatmentHistory = treatmentHistory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Staff getDentist() {
        return dentist;
    }

    public void setDentist(Staff dentist) {
        this.dentist = dentist;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<TreatmentDetailStep> getSteps() {
        return steps;
    }

    public void setSteps(List<TreatmentDetailStep> steps) {
        this.steps = steps;
    }

    public List<TreatmentImage> getImages() {
        return images;
    }

    public void setImages(List<TreatmentImage> images) {
        this.images = images;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}
