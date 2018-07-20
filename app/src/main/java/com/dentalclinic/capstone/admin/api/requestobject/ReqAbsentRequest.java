package com.dentalclinic.capstone.admin.api.requestobject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReqAbsentRequest {
    @SerializedName("staff_id")
    private int staffId;
    @SerializedName("start_dates[]")
    private List<String> listStartDate;
    @SerializedName("end_dates[]")
    private List<String> listEndDate;
    private String reason;

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public List<String> getListStartDate() {
        return listStartDate;
    }

    public void setListStartDate(List<String> listStartDate) {
        this.listStartDate = listStartDate;
    }

    public List<String> getListEndDate() {
        return listEndDate;
    }

    public void setListEndDate(List<String> listEndDate) {
        this.listEndDate = listEndDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
