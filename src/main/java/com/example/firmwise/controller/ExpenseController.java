package com.example.firmwise.controller;

import com.example.firmwise.database.DatabaseHandler;
import com.example.firmwise.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ExpenseController {

    @FXML
    private TextField categoryField;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Integer> animalIdComboBox;
    @FXML
    private TextArea notesArea;
    @FXML
    private TableView<Expense> expenseTable;
    @FXML
    private TableColumn<Expense, String> categoryColumn;
    @FXML
    private TableColumn<Expense, Double> amountColumn;
    @FXML
    private TableColumn<Expense, String> dateColumn;
    @FXML
    private TableColumn<Expense, Integer> animalIdColumn;
    @FXML
    private TableColumn<Expense, String> notesColumn;
    @FXML
    private TableColumn<Expense, String> typeColumn;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private Label totalExpenseLabel;
    @FXML
    private Label netProfitLabel;
    @FXML
    private RadioButton expenseRadio;
    @FXML
    private RadioButton incomeRadio;

    private ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        animalIdColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        loadExpenses();
        loadAnimalIds();
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
            animalIdComboBox.setItems(animalIds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addExpense() {
        try {
            String category = categoryField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            Integer animalId = animalIdComboBox.getValue();
            String notes = notesArea.getText();
            String type = incomeRadio.isSelected() ? "Income" : "Expense";

            String query = "INSERT INTO expenses (category, amount, date, animal_id, notes, type) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseHandler.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, category);
                pstmt.setDouble(2, amount);
                pstmt.setString(3, date);
                if (animalId != null) {
                    pstmt.setInt(4, animalId);
                } else {
                    pstmt.setNull(4, java.sql.Types.INTEGER);
                }
                pstmt.setString(5, notes);
                pstmt.setString(6, type);
                pstmt.executeUpdate();

                loadExpenses();
                clearFields();
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid amount format");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadExpenses() {
        expenseList.clear();
        double totalIncome = 0;
        double totalExpense = 0;

        String query = "SELECT * FROM expenses";
        try (Connection conn = DatabaseHandler.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                double amount = rs.getDouble("amount");
                String type = rs.getString("type");

                // Handle legacy data where type might be null
                if (type == null)
                    type = "Expense";

                if ("Income".equals(type)) {
                    totalIncome += amount;
                } else {
                    totalExpense += amount;
                }

                expenseList.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("category"),
                        amount,
                        rs.getString("date"),
                        rs.getInt("animal_id") == 0 ? null : rs.getInt("animal_id"),
                        rs.getString("notes"),
                        type));
            }
            expenseTable.setItems(expenseList);

            totalIncomeLabel.setText(String.format("Total Income: %.2f", totalIncome));
            totalExpenseLabel.setText(String.format("Total Expense: %.2f", totalExpense));

            double profit = totalIncome - totalExpense;
            netProfitLabel.setText(String.format("Net Profit: %.2f", profit));
            if (profit >= 0) {
                netProfitLabel.setStyle("-fx-text-fill: green;");
            } else {
                netProfitLabel.setStyle("-fx-text-fill: red;");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        categoryField.clear();
        amountField.clear();
        datePicker.setValue(null);
        animalIdComboBox.getSelectionModel().clearSelection();
        notesArea.clear();
    }
}
