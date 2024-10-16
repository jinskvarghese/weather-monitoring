package com.example.weathermonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.weathermonitoring.model.WeatherData;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
}
