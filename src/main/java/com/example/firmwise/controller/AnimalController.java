package com.example.firmwise.controller;

import com.example.firmwise.database.DatabaseHandler;
import com.example.firmwise.model.Animal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnimalController {

    @FXML
    private TableView<Animal> animalTable;
    @FXML
    private TableColumn<Animal, Integer> colId;
    @FXML
    private TableColumn<Animal, String> colType;
    @FXML
    private TableColumn<Animal, String> colName;
    @FXML
    private TableColumn<Animal, String> colBreed;
    @FXML
    private TableColumn<Animal, Integer> colAge;
    @FXML
    private TableColumn<Animal, String> colHealth;

    @FXML
    private TextField typeField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField breedField;
    @FXML
    private TextField ageField;
    @FXML
    private ComboBox<String> healthBox;

    private ObservableList<Animal> animalList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colHealth.setCellValueFactory(new PropertyValueFactory<>("healthStatus"));

        healthBox.setItems(FXCollections.observableArrayList("Healthy", "Sick", "Injured"));

        loadAnimals();
    }

    private void loadAnimals() {
        animalList.clear();
        String query = "SELECT * FROM animals";
        try (Connection conn = DatabaseHandler.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                animalList.add(new Animal(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getString("breed"),
                        rs.getInt("age"),
                        rs.getString("health_status"),
                        rs.getString("entry_date"),
                        rs.getString("photo_path"),
                        rs.getString("notes")));
            }
            animalTable.setItems(animalList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addAnimal() {
        String query = "INSERT INTO animals (type, name, breed, age, health_status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHandler.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, typeField.getText());
            pstmt.setString(2, nameField.getText());
            pstmt.setString(3, breedField.getText());
            pstmt.setInt(4, Integer.parseInt(ageField.getText()));
            pstmt.setString(5, healthBox.getValue());

            pstmt.executeUpdate();
            loadAnimals();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        typeField.clear();
        nameField.clear();
        breedField.clear();
        ageField.clear();
        healthBox.getSelectionModel().clearSelection();
    }
}
