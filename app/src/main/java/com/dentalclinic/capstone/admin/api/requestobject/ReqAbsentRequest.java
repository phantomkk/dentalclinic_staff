package com.dentalclinic.capstone.admin.api.requestobject;

import com.google.gson.annotations.SerializedName;

public class ReqAbsentRequest {
    @SerializedName("staff_id")
    private int staffId;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String listEndDate;
    private String reason;

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getListEndDate() {
        return listEndDate;
    }

    public void setListEndDate(String listEndDate) {
        this.listEndDate = listEndDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
