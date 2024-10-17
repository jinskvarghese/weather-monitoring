package com.example.weathermonitoring.repository;

// import org.hibernate.mapping.List;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.weathermonitoring.model.WeatherData;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    List<WeatherData> findByCityAndTimestampBetween(String city, LocalDateTime start, LocalDateTime end);
    List<WeatherData> findTop2ByCityOrderByTimestampDesc(String city);
    List<WeatherData> findByTimestampBetween(LocalDateTime start, LocalDateTime end);


}
