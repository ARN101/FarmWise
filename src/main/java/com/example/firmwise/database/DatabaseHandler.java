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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
