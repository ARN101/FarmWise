package com.example.firmwise.model;

public class Animal {
    private int id;
    private String type;
    private String name;
    private String breed;
    private int age;
    private String healthStatus;
    private String entryDate;
    private String photoPath;
    private String notes;

    public Animal(int id, String type, String name, String breed, int age, String healthStatus, String entryDate,
            String photoPath, String notes) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.healthStatus = healthStatus;
        this.entryDate = entryDate;
        this.photoPath = photoPath;
        this.notes = notes;
    }

    public Animal(String type, String name, String breed, int age, String healthStatus, String entryDate,
            String photoPath, String notes) {
        this.type = type;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.healthStatus = healthStatus;
        this.entryDate = entryDate;
        this.photoPath = photoPath;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
