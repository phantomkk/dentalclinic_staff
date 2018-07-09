package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TreatmentDetailStep implements Serializable {
    @SerializedName("treatment_detail")
    private TreatmentDetail treatmentDetail;
    @SerializedName("step")
    private  Step step;
    @SerializedName("description")
    private String description;

    public TreatmentDetail getTreatmentDetail() {
        return treatmentDetail;
    }

    public void setTreatmentDetail(TreatmentDetail treatmentDetail) {
        this.treatmentDetail = treatmentDetail;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
