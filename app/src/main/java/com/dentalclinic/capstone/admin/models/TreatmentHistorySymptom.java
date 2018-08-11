package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TreatmentHistorySymptom implements Serializable {
    @SerializedName("treatment_history_id")
    private int treatmentHistoryId;
    @SerializedName("symptom_id")
    private int symptomId;

    public int getTreatmentHistoryId() {
        return treatmentHistoryId;
    }

    public void setTreatmentHistoryId(int treatmentHistoryId) {
        this.treatmentHistoryId = treatmentHistoryId;
    }

    public int getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(int symptomId) {
        this.symptomId = symptomId;
    }
}
