package com.dentalclinic.capstone.admin.api.responseobject;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeedbackResponse implements Serializable {
    @SerializedName("dentist_name")
    private String dentistName;
    @SerializedName("treatment_name")
    private String treatmentName;
    @SerializedName("treatment_detail_id")
    private int treatmentDetailId;
    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("dentist_avatar")
private String dentistAvatar;
    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDentistAvatar() {
        return dentistAvatar;
    }

    public void setDentistAvatar(String dentistAvatar) {
        this.dentistAvatar = dentistAvatar;
    }

    public int getTreatmentDetailId() {
        return treatmentDetailId;
    }

    public void setTreatmentDetailId(int treatmentDetailId) {
        this.treatmentDetailId = treatmentDetailId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
