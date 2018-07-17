package com.dentalclinic.capstone.admin.api.requestobject;

import java.io.Serializable;

public class TreatmentHistoryRequest implements Serializable {
    private int staffId;
    private int treatmentId;
    private int patientId;
    private float price;
    private String description;
    private String treatmentDetailNote;
    private int toothNumber;

}
