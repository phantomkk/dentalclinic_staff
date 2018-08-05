package com.dentalclinic.capstone.admin.models;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Appointment implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("staff_id")
    private int staffId;
    @SerializedName("dentist")
    private Staff staff;
    @SerializedName("date_booking")
    private String dateBooking;
    @SerializedName("phone")
    private String phone;
    @SerializedName("note")
    private String note;
    @SerializedName("name")
    private String name;
    @SerializedName("numerical_order")
    private int numericalOrder;
    @SerializedName("estimated_time")
    private String estimatedTime;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("status")
    private int status;
    @SerializedName("patient")
    private Patient patient;
//    @SerializedName("dentist")
//    private Staff dentist;

    private boolean isExpand = false;
    public Appointment(String note, String name, int numericalOrder, int status) {
        this.note = note;
        this.name = name;
        this.numericalOrder = numericalOrder;
        this.status = status;
    }


    public Appointment(String note, String name, int numericalOrder, int status, Patient patient) {
        this.note = note;
        this.name = name;
        this.numericalOrder = numericalOrder;
        this.status = status;
        this.patient = patient;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateBooking() {
        return dateBooking;
    }

    public void setDateBooking(String dateBooking) {
        this.dateBooking = dateBooking;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getNumericalOrder() {
        return numericalOrder;
    }

    public void setNumericalOrder(int numericalOrder) {
        this.numericalOrder = numericalOrder;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getName() {
        return name;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressLint("SimpleDateFormat")
    public WeekViewEvent toWeekViewEvent(){

        // Create an week view event.
        WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setId(getId());
        weekViewEvent.setName(getPhone());
        Calendar calendar= Calendar.getInstance();
        Date startTime = DateUtils.getDate(getStartTime(), DateTimeFormat.DATE_TIME_DB);
        calendar.setTime(startTime);
        weekViewEvent.setStartTime(calendar);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(startTime);
        Time estimatedTime = Time.valueOf(getEstimatedTime());
        calendarEnd.add(Calendar.HOUR,estimatedTime.getHours());
        calendarEnd.add(Calendar.MINUTE,estimatedTime.getMinutes());
        weekViewEvent.setEndTime(calendarEnd);
        if(getStatus()==1) {
            weekViewEvent.setColor(Color.parseColor("#b71414"));
        }else{
            weekViewEvent.setColor(Color.parseColor("#1480b7"));
        }

        return weekViewEvent;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
