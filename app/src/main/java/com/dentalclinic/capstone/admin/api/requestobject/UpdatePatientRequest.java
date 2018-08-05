package com.dentalclinic.capstone.admin.api.requestobject;

import com.google.gson.annotations.SerializedName;

public class UpdatePatientRequest {
    @SerializedName("id")
    private int patientId;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("gender")
    private String gender;
    @SerializedName("district_id")
    private int districtId;
    @SerializedName("date_of_birth")
    private String birthday;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
