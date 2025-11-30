package com.example.firmwise.model;

public class WeatherData {
    private int temperature;
    private String description;
    private int humidity;
    private boolean isRainExpected;

    public WeatherData(int temperature, String description, int humidity, boolean isRainExpected) {
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.isRainExpected = isRainExpected;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public int getHumidity() {
        return humidity;
    }

    public boolean isRainExpected() {
        return isRainExpected;
    }
}
