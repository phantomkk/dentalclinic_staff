package com.dentalclinic.capstone.admin.models;

import com.dentalclinic.capstone.admin.utils.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class TreatmentImage implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("treatment_detail")
    private TreatmentDetail treatmentDetail;
    @SerializedName("image_link")
    private String imageLink;
    @SerializedName("public_date")
    private Date publicDate;

    public TreatmentImage(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TreatmentDetail getTreatmentDetail() {
        return treatmentDetail;
    }

    public void setTreatmentDetail(TreatmentDetail treatmentDetail) {
        this.treatmentDetail = treatmentDetail;
    }

    public String getImageLink() {
        return Utils.linkServer+ imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Date getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(Date publicDate) {
        this.publicDate = publicDate;
    }
}
