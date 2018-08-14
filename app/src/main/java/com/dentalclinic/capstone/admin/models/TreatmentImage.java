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
    private boolean isFile = false;

    public TreatmentImage(String imageLink) {
        this.imageLink = imageLink;
    }

    public TreatmentImage(String imageLink, boolean isFile) {
        this.imageLink = imageLink;
        this.isFile = isFile;
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
        if(isFile){
            return imageLink;
        }else{
            return Utils.linkServer+ imageLink;
        }
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

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
