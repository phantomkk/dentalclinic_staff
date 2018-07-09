package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("content")
    private String content ;
    @SerializedName("treatment_detail_id")
    private int treatmentDetailId;
    @SerializedName("date_feedback")
    private String dateFeedback;
    @SerializedName("num_of_stars")
    private float numOfStars;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }


    public String getDateFeedback() {
        return dateFeedback;
    }

    public void setDateFeedback(String dateFeedback) {
        this.dateFeedback = dateFeedback;
    }

    public float getNumOfStars() {
        return numOfStars;
    }

    public void setNumOfStars(float numOfStars) {
        this.numOfStars = numOfStars;
    }

    public int getTreatmentDetailId() {
        return treatmentDetailId;
    }

    public void setTreatmentDetailId(int treatmentDetailId) {
        this.treatmentDetailId = treatmentDetailId;
    }
}
