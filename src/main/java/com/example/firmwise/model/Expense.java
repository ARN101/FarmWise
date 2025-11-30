package com.example.firmwise.model;

public class Expense {
    private int id;
    private String category;
    private double amount;
    private String date;
    private Integer animalId;
    private String notes;

    public Expense(int id, String category, double amount, String date, Integer animalId, String notes) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.animalId = animalId;
        this.notes = notes;
    }

    public Expense(String category, double amount, String date, Integer animalId, String notes) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.animalId = animalId;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Integer animalId) {
        this.animalId = animalId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
