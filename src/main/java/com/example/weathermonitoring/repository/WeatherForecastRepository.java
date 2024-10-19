package com.example.weathermonitoring.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.weathermonitoring.model.WeatherForecast;

public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
    List<WeatherForecast> findByCityAndForecastTimeBetween(String city, LocalDateTime start, LocalDateTime end);
}
