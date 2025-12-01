package com.example.firmwise.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class DashboardController {

    @FXML
    private BorderPane mainLayout;

    @FXML
    private StackPane contentArea;

    @FXML
    private Label cityLabel;
    @FXML
    private Label weatherLabel;
    @FXML
    private Label tempLabel;
    @FXML
    private Label rainLabel;
    @FXML
    private Label humidityLabel;

    @FXML
    public void initialize() {
        loadWeather();
        loadView("home.fxml");
    }

    @FXML
    private void showLivestock() {
        loadView("animals.fxml");
    }

    @FXML
    private void showVaccinations() {
        loadView("vaccinations.fxml");
    }

    @FXML
    private void showExpenses() {
        loadView("expenses.fxml");
    }

    @FXML
    private void showFarmProfile() {
        loadView("farm_profile.fxml");
    }

    @FXML
    private void showReports() {
        loadView("reports.fxml");
    }

    @FXML
    private void showDashboard() {
        loadView("home.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/firmwise/" + fxmlFile));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWeather() {
        new Thread(() -> {
            String city = "Dhaka";
            String country = "Bangladesh";

            try (java.sql.Connection conn = com.example.firmwise.database.DatabaseHandler.getConnection();
                    java.sql.Statement stmt = conn.createStatement();
                    java.sql.ResultSet rs = stmt.executeQuery("SELECT city, country FROM farm_profile LIMIT 1")) {

                if (rs.next()) {
                    city = rs.getString("city");
                    country = rs.getString("country");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            com.example.firmwise.service.WeatherService service = new com.example.firmwise.service.WeatherService();
            com.example.firmwise.model.WeatherData data = service.getWeather(city, country);

            if (data != null) {
                String finalCity = city;
                javafx.application.Platform.runLater(() -> {
                    cityLabel.setText(finalCity);
                    weatherLabel.setText(data.getDescription());
                    tempLabel.setText(data.getTemperature() + "°C");
                    rainLabel.setText(data.isRainExpected() ? "Rain: Yes" : "Rain: No");
                    humidityLabel.setText("Humidity: " + data.getHumidity() + "%");
                });
            }
        }).start();
    }
}
