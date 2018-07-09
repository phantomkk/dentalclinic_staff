package com.dentalclinic.capstone.admin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Treatment implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("max_price")
    private Long maxPrice;
    @SerializedName("min_price")
    private Long minPrice;
    @SerializedName("category")
    private TreatmentCategory category;
    @SerializedName("treatment_steps")
    private List<TreatmentStep> treatmentSteps;
    @SerializedName("event")
    private Event event;


    public Treatment(String name, Event event) {
        this.name = name;
        this.event = event;
    }

    public Treatment(String name) {
        this.name = name;
    }

    public Treatment(String name, Long maxPrice, Long minPrice) {
        this.name = name;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public Treatment(String name, String description, Long maxPrice, Long minPrice) {
        this.name = name;
        this.description = description;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public Treatment() {
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

    public Long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Long minPrice) {
        this.minPrice = minPrice;
    }

    public TreatmentCategory getCategory() {
        return category;
    }

    public void setCategory(TreatmentCategory category) {
        this.category = category;
    }

    public List<TreatmentStep> getTreatmentSteps() {
        return treatmentSteps;
    }

    public void setTreatmentSteps(List<TreatmentStep> treatmentSteps) {
        this.treatmentSteps = treatmentSteps;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
