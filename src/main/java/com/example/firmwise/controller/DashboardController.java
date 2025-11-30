package com.example.firmwise.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class DashboardController {

    @FXML
    private BorderPane mainLayout;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        loadView("animals.fxml");
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
    private void showDashboard() {
        // Placeholder for dashboard home
        // loadView("home.fxml");
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
}
