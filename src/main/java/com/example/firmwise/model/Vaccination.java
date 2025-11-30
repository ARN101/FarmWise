package com.example.firmwise.model;

public class Vaccination {
    private int id;
    private int animalId;
    private String vaccineName;
    private String dateGiven;
    private String nextDueDate;
    private String status;
    private String notes;

    public Vaccination(int id, int animalId, String vaccineName, String dateGiven, String nextDueDate, String status,
            String notes) {
        this.id = id;
        this.animalId = animalId;
        this.vaccineName = vaccineName;
        this.dateGiven = dateGiven;
        this.nextDueDate = nextDueDate;
        this.status = status;
        this.notes = notes;
    }

    public Vaccination(int animalId, String vaccineName, String dateGiven, String nextDueDate, String status,
            String notes) {
        this.animalId = animalId;
        this.vaccineName = vaccineName;
        this.dateGiven = dateGiven;
        this.nextDueDate = nextDueDate;
        this.status = status;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(String dateGiven) {
        this.dateGiven = dateGiven;
    }

    public String getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(String nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
