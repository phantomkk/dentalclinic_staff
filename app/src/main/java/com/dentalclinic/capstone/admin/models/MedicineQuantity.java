package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MedicineQuantity implements Serializable {
    @SerializedName("medicine_id")
    private int medicineId;
    @SerializedName("treatment_detail_id")
    private int treatmentDetailId;
    @SerializedName("medicine")
    private Medicine medicine;
    @SerializedName("treatment_detail")
    private TreatmentDetail treatmentDetail;
    @SerializedName("quantity")
    private int quantity;

    public MedicineQuantity(int medicineId, int treatmentDetailId, int quantity) {
        this.medicineId = medicineId;
        this.treatmentDetailId = treatmentDetailId;
        this.quantity = quantity;
    }
    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getTreatmentDetailId() {
        return treatmentDetailId;
    }

    public void setTreatmentDetailId(int treatmentDetailId) {
        this.treatmentDetailId = treatmentDetailId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public TreatmentDetail getTreatmentDetail() {
        return treatmentDetail;
    }

    public void setTreatmentDetail(TreatmentDetail treatmentDetail) {
        this.treatmentDetail = treatmentDetail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
