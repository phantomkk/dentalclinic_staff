package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreatmentCategory implements Serializable {
    @SerializedName("id")
    private int id=-1;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("icon_link")
    private String iconLink;
    @SerializedName("treatments")
    private List<Treatment> treatments;

    public TreatmentCategory() {
    }

    public TreatmentCategory(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        treatments = new ArrayList<>();
    }

    public TreatmentCategory(String name, String description, String iconLink) {
        this.name = name;
        this.description = description;
        this.iconLink = iconLink;
        treatments = new ArrayList<>();
    }

    public TreatmentCategory(String name, String iconLink) {
        this.name = name;
        this.iconLink = iconLink;
        treatments = new ArrayList<>();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconLink() {
        if(iconLink==null ||iconLink.length()==0){
            iconLink = "https://www.impactnyc.org/wp-content/uploads/2018/02/missing-image-avatar.png";
        }
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }


}
