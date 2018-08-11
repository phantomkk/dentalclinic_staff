package com.dentalclinic.capstone.admin.models;

import com.dentalclinic.capstone.admin.utils.Utils;
import com.github.dewinjm.monthyearpicker.Util;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Patient implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    @SerializedName("gender")
    private String gender;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("district")
    private District district;
    @SerializedName("city")
    private City city;
    @SerializedName("anamnesis")
    private ArrayList<AnamnesisCatalog> listAnamnesis;
    @SerializedName("treatment_histories")
    private List<TreatmentHistory> treatmentHistories;

    @SerializedName("subaccounts")
    private List<Patient> subAccounts;

    public Patient(String name, String address, String phone, String dateOfBirth, String avatar) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
    }

    public Patient(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Patient() {
    }

    public Patient(String name, String dateOfBirth, String gender, String avatar) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.avatar = avatar;
    }

    public Patient(String name, String dateOfBirth, String gender) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        if (this.avatar == null) {
            avatar = "http://150.95.104.237/assets/images/avatar/user_avatar_5.png";
        }
        return Utils.linkServer + avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public List<TreatmentHistory> getTreatmentHistories() {
        return treatmentHistories;
    }

    public void setTreatmentHistories(List<TreatmentHistory> treatmentHistories) {
        this.treatmentHistories = treatmentHistories;
    }


    public List<Patient> getSubAccounts() {
        return subAccounts;
    }

    public void setSubAccounts(List<Patient> subAccounts) {
        this.subAccounts = subAccounts;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ArrayList<AnamnesisCatalog> getListAnamnesis() {
        return listAnamnesis;
    }

    public void setListAnamnesis(ArrayList<AnamnesisCatalog> listAnamnesis) {
        this.listAnamnesis = listAnamnesis;
    }
}
