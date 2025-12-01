package com.example.firmwise.controller;

import com.example.firmwise.database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ReportController {

    @FXML
    private PieChart expensePieChart;
    @FXML
    private BarChart<String, Number> incomeExpenseBarChart;

    @FXML
    public void initialize() {
        loadExpenseData();
        loadIncomeExpenseData();
    }

    private void loadExpenseData() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        String query = "SELECT category, SUM(amount) as total FROM expenses WHERE type = 'Expense' GROUP BY category";

        try (Connection conn = DatabaseHandler.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                pieData.add(new PieChart.Data(rs.getString("category"), rs.getDouble("total")));
            }
            expensePieChart.setData(pieData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadIncomeExpenseData() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Financial Overview");

        double totalIncome = 0;
        double totalExpense = 0;

        String query = "SELECT type, SUM(amount) as total FROM expenses GROUP BY type";
        try (Connection conn = DatabaseHandler.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String type = rs.getString("type");
                if (type == null)
                    type = "Expense"; // Legacy handling

                if ("Income".equals(type)) {
                    totalIncome += rs.getDouble("total");
                } else {
                    totalExpense += rs.getDouble("total");
                }
            }

            series.getData().add(new XYChart.Data<>("Income", totalIncome));
            series.getData().add(new XYChart.Data<>("Expense", totalExpense));

            incomeExpenseBarChart.getData().clear();
            incomeExpenseBarChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshData() {
        loadExpenseData();
        loadIncomeExpenseData();
    }
}
