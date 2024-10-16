package com.example.weathermonitoring.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.weathermonitoring.model.DailyWeatherSummary;

public interface DailyWeatherSummaryRepository extends JpaRepository<DailyWeatherSummary, Long> {
    List<DailyWeatherSummary> findByCityAndDate(String city, LocalDate date);
    List<DailyWeatherSummary> findByDate(LocalDate date);
}
