package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Absent implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("reason")
    private String reason;
    @SerializedName("staff_approve")
    private Staff staffApprove;
    @SerializedName("message_from_staff")
    private String messageFromStaff;
    @SerializedName("created_time")
    private String createdTime;
    @SerializedName("is_approved")
    private boolean isApproved;

    public Absent() {
    }

    public Absent(String startDate, String endDate, String reason) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }


    private boolean isExpand=false;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Staff getStaffApprove() {
        return staffApprove;
    }

    public void setStaffApprove(Staff staffApprove) {
        this.staffApprove = staffApprove;
    }

    public String getMessageFromStaff() {
        return messageFromStaff;
    }

    public void setMessageFromStaff(String messageFromStaff) {
        this.messageFromStaff = messageFromStaff;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
