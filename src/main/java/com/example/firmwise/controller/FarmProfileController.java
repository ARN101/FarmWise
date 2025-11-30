package com.example.firmwise.controller;

import com.example.firmwise.database.DatabaseHandler;
import com.example.firmwise.model.FarmProfile;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FarmProfileController {

    @FXML
    private TextField farmNameField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField countryField;

    @FXML
    public void initialize() {
        loadProfile();
    }

    private void loadProfile() {
        String query = "SELECT * FROM farm_profile LIMIT 1";
        try (Connection conn = DatabaseHandler.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                farmNameField.setText(rs.getString("farm_name"));
                cityField.setText(rs.getString("city"));
                countryField.setText(rs.getString("country"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveProfile() {
        String checkQuery = "SELECT count(*) FROM farm_profile";
        try (Connection conn = DatabaseHandler.getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                ResultSet rs = checkStmt.executeQuery()) {

            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                String insertQuery = "INSERT INTO farm_profile (farm_name, city, country) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, farmNameField.getText());
                    insertStmt.setString(2, cityField.getText());
                    insertStmt.setString(3, countryField.getText());
                    insertStmt.executeUpdate();
                }
            } else {
                String updateQuery = "UPDATE farm_profile SET farm_name = ?, city = ?, country = ? WHERE id = (SELECT id FROM farm_profile LIMIT 1)";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, farmNameField.getText());
                    updateStmt.setString(2, cityField.getText());
                    updateStmt.setString(3, countryField.getText());
                    updateStmt.executeUpdate();
                }
            }
            showAlert("Success", "Farm Profile Saved!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save profile.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
