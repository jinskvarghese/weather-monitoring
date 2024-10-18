package com.example.weathermonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.weathermonitoring.model.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
