package com.example.firmwise.model;

public class FarmProfile {
    private int id;
    private String farmName;
    private String city;
    private String country;

    public FarmProfile(int id, String farmName, String city, String country) {
        this.id = id;
        this.farmName = farmName;
        this.city = city;
        this.country = country;
    }

    public FarmProfile(String farmName, String city, String country) {
        this.farmName = farmName;
        this.city = city;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
