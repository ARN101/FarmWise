package com.example.firmwise.service;

import com.example.firmwise.database.DatabaseHandler;
import com.example.firmwise.model.Notification;
import com.example.firmwise.model.WeatherData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    public List<Notification> getNotifications() {
        List<Notification> notifications = new ArrayList<>();

        // 1. Check Vaccinations
        notifications.addAll(getVaccinationAlerts());

        // 2. Check Weather
        Notification weatherAlert = getWeatherAlert();
        if (weatherAlert != null) {
            notifications.add(weatherAlert);
        }

        return notifications;
    }

    private List<Notification> getVaccinationAlerts() {
        List<Notification> alerts = new ArrayList<>();
        String query = "SELECT v.vaccine_name, v.next_due_date, a.name " +
                "FROM vaccinations v " +
                "JOIN animals a ON v.animal_id = a.id " +
                "WHERE v.status != 'Completed'";

        try (Connection conn = DatabaseHandler.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            LocalDate today = LocalDate.now();
            LocalDate nextWeek = today.plusDays(7);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (rs.next()) {
                String dateStr = rs.getString("next_due_date");
                if (dateStr != null && !dateStr.isEmpty()) {
                    try {
                        LocalDate dueDate = LocalDate.parse(dateStr, formatter);

                        if (dueDate.isBefore(today)) {
                            alerts.add(new Notification(
                                    "Overdue Vaccination",
                                    String.format("%s for %s was due on %s", rs.getString("vaccine_name"),
                                            rs.getString("name"), dateStr),
                                    Notification.NotificationType.WARNING));
                        } else if (!dueDate.isAfter(nextWeek)) {
                            alerts.add(new Notification(
                                    "Upcoming Vaccination",
                                    String.format("%s for %s is due on %s", rs.getString("vaccine_name"),
                                            rs.getString("name"), dateStr),
                                    Notification.NotificationType.INFO));
                        }
                    } catch (Exception e) {
                        // Ignore parse errors
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alerts;
    }

    private Notification getWeatherAlert() {
        try {
            // Fetch location
            String city = "Dhaka";
            String country = "Bangladesh";
            try (Connection conn = DatabaseHandler.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT city, country FROM farm_profile LIMIT 1")) {
                if (rs.next()) {
                    city = rs.getString("city");
                    country = rs.getString("country");
                }
            }

            WeatherService weatherService = new WeatherService();
            WeatherData data = weatherService.getWeather(city, country);

            if (data != null && data.isRainExpected()) {
                return new Notification(
                        "Weather Alert",
                        "Rain is expected today in " + city + ". Take necessary precautions.",
                        Notification.NotificationType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
