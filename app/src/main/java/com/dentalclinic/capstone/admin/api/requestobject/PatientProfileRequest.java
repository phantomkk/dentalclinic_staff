package com.dentalclinic.capstone.admin.api.requestobject;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PatientProfileRequest implements Serializable{
    @SerializedName("phone")
    private String phone;
    @SerializedName("name")
    private String name;
    @SerializedName("gender")
    private String gender;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("districtId")
    private int districtId;
    @SerializedName("address")
    private String address;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
