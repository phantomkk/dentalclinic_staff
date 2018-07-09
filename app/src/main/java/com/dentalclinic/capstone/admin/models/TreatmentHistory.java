package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TreatmentHistory implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("patient")
    private Patient patient;
    @SerializedName("created_date")
    private String createDate;
    @SerializedName("finish_date")
    private String finishDate;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private Long price;
    @SerializedName("total_price")
    private Long totalPrice;
    @SerializedName("treatment")
    private Treatment treatment;
    @SerializedName("details")
    private List<TreatmentDetail> treatmentDetails;
    @SerializedName("tooth")
    private Tooth tooth;


    public TreatmentHistory(String createDate, String finishDate, Long price, Long totalPrice, Treatment treatment, Tooth tooth) {
        this.createDate = createDate;
        this.finishDate = finishDate;
        this.price = price;
        this.totalPrice = totalPrice;
        this.treatment = treatment;
        this.tooth = tooth;
    }

    public TreatmentHistory(String createDate, String finishDate, Treatment treatment) {
        this.createDate = createDate;
        this.finishDate = finishDate;
        this.treatment = treatment;
    }

    public TreatmentHistory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<TreatmentDetail> getTreatmentDetails() {
        return treatmentDetails;
    }

    public void setTreatmentDetails(List<TreatmentDetail> treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Tooth getTooth() {
        return tooth;
    }

    public void setTooth(Tooth tooth) {
        this.tooth = tooth;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
