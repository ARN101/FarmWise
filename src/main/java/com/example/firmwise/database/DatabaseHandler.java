package com.example.firmwise.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    private static final String DB_URL = "jdbc:sqlite:farmwise.db";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                createTables();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void createTables() {
        String createAnimalsTable = "CREATE TABLE IF NOT EXISTS animals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," +
                "name TEXT," +
                "breed TEXT," +
                "age INTEGER," +
                "health_status TEXT," +
                "entry_date TEXT," +
                "photo_path TEXT," +
                "notes TEXT" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createAnimalsTable);

            String createVaccinationsTable = "CREATE TABLE IF NOT EXISTS vaccinations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "animal_id INTEGER," +
                    "vaccine_name TEXT," +
                    "date_given TEXT," +
                    "next_due_date TEXT," +
                    "status TEXT," +
                    "notes TEXT," +
                    "FOREIGN KEY(animal_id) REFERENCES animals(id)" +
                    ")";
            stmt.execute(createVaccinationsTable);

            String createExpensesTable = "CREATE TABLE IF NOT EXISTS expenses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "category TEXT," +
                    "amount REAL," +
                    "date TEXT," +
                    "animal_id INTEGER," +
                    "notes TEXT," +
                    "type TEXT," +
                    "FOREIGN KEY(animal_id) REFERENCES animals(id)" +
                    ")";
            stmt.execute(createExpensesTable);

            // Migration: Add 'type' column if it doesn't exist (for existing databases)
            try {
                stmt.execute("ALTER TABLE expenses ADD COLUMN type TEXT");
                stmt.execute("UPDATE expenses SET type = 'Expense' WHERE type IS NULL");
            } catch (SQLException e) {
                // Column likely exists or other error, safe to ignore for this update
            }

            String createFarmProfileTable = "CREATE TABLE IF NOT EXISTS farm_profile (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "farm_name TEXT," +
                    "city TEXT," +
                    "country TEXT" +
                    ")";
            stmt.execute(createFarmProfileTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
