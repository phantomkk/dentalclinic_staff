package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentDetail implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("payment")
    private Payment payment;
    @SerializedName("staff")
    private Staff receptionist;
    @SerializedName("date_create")
    private String dateCreate;
    @SerializedName("received_money")
    private Long receivedMoney;

    public PaymentDetail(Staff receptionist, Long receivedMoney) {
        this.receptionist = receptionist;
        this.receivedMoney = receivedMoney;
    }

    public PaymentDetail(Staff receptionist, String dateCreate, Long receivedMoney) {
        this.receptionist = receptionist;
        this.dateCreate = dateCreate;
        this.receivedMoney = receivedMoney;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Staff getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Staff receptionist) {
        this.receptionist = receptionist;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Long getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(Long receivedMoney) {
        this.receivedMoney = receivedMoney;
    }
}
