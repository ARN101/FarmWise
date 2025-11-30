package com.example.firmwise.controller;

import com.example.firmwise.database.DatabaseHandler;
import com.example.firmwise.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;

public class ExpenseController {

    @FXML
    private TableView<Expense> expenseTable;
    @FXML
    private TableColumn<Expense, Integer> colId;
    @FXML
    private TableColumn<Expense, String> colCategory;
    @FXML
    private TableColumn<Expense, Double> colAmount;
    @FXML
    private TableColumn<Expense, String> colDate;
    @FXML
    private TableColumn<Expense, Integer> colAnimalId;

    @FXML
    private ComboBox<String> categoryBox;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Integer> animalIdBox;
    @FXML
    private TextArea notesArea;
    @FXML
    private Label totalExpensesLabel;

    private ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAnimalId.setCellValueFactory(new PropertyValueFactory<>("animalId"));

        categoryBox.setItems(FXCollections.observableArrayList("Feed", "Medicine", "Equipment", "Labor", "Other"));

        loadAnimalIds();
        loadExpenses();
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

    private void loadExpenses() {
        expenseList.clear();
        double total = 0;
        String query = "SELECT * FROM expenses";
        try (Connection conn = DatabaseHandler.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                double amount = rs.getDouble("amount");
                total += amount;
                expenseList.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("category"),
                        amount,
                        rs.getString("date"),
                        rs.getInt("animal_id") == 0 ? null : rs.getInt("animal_id"),
                        rs.getString("notes")));
            }
            expenseTable.setItems(expenseList);
            totalExpensesLabel.setText(String.format("Total Expenses: BDT %.2f", total));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addExpense() {
        String query = "INSERT INTO expenses (category, amount, date, animal_id, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHandler.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, categoryBox.getValue());
            pstmt.setDouble(2, Double.parseDouble(amountField.getText()));
            pstmt.setString(3, datePicker.getValue().toString());
            if (animalIdBox.getValue() != null) {
                pstmt.setInt(4, animalIdBox.getValue());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            pstmt.setString(5, notesArea.getText());

            pstmt.executeUpdate();
            loadExpenses();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Handle invalid number format
            System.err.println("Invalid amount format");
        }
    }

    private void clearFields() {
        categoryBox.getSelectionModel().clearSelection();
        amountField.clear();
        datePicker.setValue(null);
        animalIdBox.getSelectionModel().clearSelection();
        notesArea.clear();
    }
}
