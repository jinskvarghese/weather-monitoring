package com.example.weathermonitoring.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DailyWeatherSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private double avgTemperature;
    private double maxTemperature;
    private double minTemperature;
    private String dominantCondition;
    private LocalDate date;
    private double avgHumidity;
    private double avgWindSpeed;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getAvgTemperature() {
        return avgTemperature;
    }

    public void setAvgTemperature(double avgTemperature) {
        this.avgTemperature = avgTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getDominantCondition() {
        return dominantCondition;
    }

    public void setDominantCondition(String dominantCondition) {
        this.dominantCondition = dominantCondition;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(double avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public double getAvgWindSpeed() {
        return avgWindSpeed;
    }

    public void setAvgWindSpeed(double avgWindSpeed) {
        this.avgWindSpeed = avgWindSpeed;
}
}
