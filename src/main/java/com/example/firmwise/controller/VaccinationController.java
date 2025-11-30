package com.example.firmwise.controller;

import com.example.firmwise.database.DatabaseHandler;
import com.example.firmwise.model.Vaccination;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.*;
import java.time.LocalDate;

public class VaccinationController {

    @FXML
    private TableView<Vaccination> vaccinationTable;
    @FXML
    private TableColumn<Vaccination, Integer> colId;
    @FXML
    private TableColumn<Vaccination, Integer> colAnimalId;
    @FXML
    private TableColumn<Vaccination, String> colVaccine;
    @FXML
    private TableColumn<Vaccination, String> colDateGiven;
    @FXML
    private TableColumn<Vaccination, String> colNextDue;
    @FXML
    private TableColumn<Vaccination, String> colStatus;

    @FXML
    private ComboBox<Integer> animalIdBox;
    @FXML
    private TextField vaccineField;
    @FXML
    private DatePicker dateGivenPicker;
    @FXML
    private DatePicker nextDuePicker;
    @FXML
    private TextArea notesArea;

    private ObservableList<Vaccination> vaccinationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAnimalId.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        colVaccine.setCellValueFactory(new PropertyValueFactory<>("vaccineName"));
        colDateGiven.setCellValueFactory(new PropertyValueFactory<>("dateGiven"));
        colNextDue.setCellValueFactory(new PropertyValueFactory<>("nextDueDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Custom cell factory for status color coding
        colStatus.setCellFactory(new Callback<TableColumn<Vaccination, String>, TableCell<Vaccination, String>>() {
            @Override
            public TableCell<Vaccination, String> call(TableColumn<Vaccination, String> param) {
                return new TableCell<Vaccination, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            if (item.equals("Overdue")) {
                                setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            } else if (item.equals("Due Soon")) {
                                setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                            } else {
                                setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            }
                        }
                    }
                };
            }
        });

        loadAnimalIds();
        loadVaccinations();
    }

    private void loadAnimalIds() {
        ObservableList<Integer> animalIds = FXCollections.observableArrayList();
        String query = "SELECT id FROM animals";
        try (Connection conn = DatabaseHandler.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                animalIds.add(rs.getInt("id"));
            }
            animalIdBox.setItems(animalIds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadVaccinations() {
        vaccinationList.clear();
        String query = "SELECT * FROM vaccinations";
        try (Connection conn = DatabaseHandler.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                vaccinationList.add(new Vaccination(
                        rs.getInt("id"),
                        rs.getInt("animal_id"),
                        rs.getString("vaccine_name"),
                        rs.getString("date_given"),
                        rs.getString("next_due_date"),
                        rs.getString("status"),
                        rs.getString("notes")));
            }
            vaccinationTable.setItems(vaccinationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addVaccination() {
        String query = "INSERT INTO vaccinations (animal_id, vaccine_name, date_given, next_due_date, status, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHandler.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            LocalDate nextDue = nextDuePicker.getValue();
            String status = calculateStatus(nextDue);

            pstmt.setInt(1, animalIdBox.getValue());
            pstmt.setString(2, vaccineField.getText());
            pstmt.setString(3, dateGivenPicker.getValue().toString());
            pstmt.setString(4, nextDue.toString());
            pstmt.setString(5, status);
            pstmt.setString(6, notesArea.getText());

            pstmt.executeUpdate();
            loadVaccinations();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String calculateStatus(LocalDate nextDue) {
        LocalDate today = LocalDate.now();
        if (nextDue.isBefore(today)) {
            return "Overdue";
        } else if (nextDue.isBefore(today.plusDays(7))) {
            return "Due Soon";
        } else {
            return "Up to Date";
        }
    }

    private void clearFields() {
        animalIdBox.getSelectionModel().clearSelection();
        vaccineField.clear();
        dateGivenPicker.setValue(null);
        nextDuePicker.setValue(null);
        notesArea.clear();
    }
}
