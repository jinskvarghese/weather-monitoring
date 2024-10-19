package com.example.weathermonitoring.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.weathermonitoring.model.Alert;
import com.example.weathermonitoring.model.DailyWeatherSummary;
import com.example.weathermonitoring.model.WeatherForecast;
import com.example.weathermonitoring.repository.AlertRepository;
import com.example.weathermonitoring.repository.DailyWeatherSummaryRepository;
import com.example.weathermonitoring.service.WeatherService;


@Controller
public class WeatherController {

    private final DailyWeatherSummaryRepository summaryRepository;

    // @Autowired
    public WeatherController(DailyWeatherSummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    @GetMapping("/daily-summaries")
    public String showDailySummaries(Model model) {
        LocalDate today = LocalDate.now();
        List<DailyWeatherSummary> summaries = summaryRepository.findByDate(today);
        model.addAttribute("summaries", summaries);

        // Check if there's no data
        if (summaries.isEmpty()) {
            model.addAttribute("message", "No weather data available for today.");
        }
        return "dailySummaries";
    }

    @Autowired
    private AlertRepository alertRepository;

    @GetMapping("/alerts")
    public String showAlerts(Model model) {
        List<Alert> alerts = alertRepository.findAll(); // Fetch alerts from the repository
    
        // Convert the list of Alert objects into a list of formatted strings
        List<String> formattedAlerts = alerts.stream()
            .map(alert -> {
                String city = alert.getCity();
                String message = alert.getMessage();
                String timestamp = alert.getTimestamp().toString(); // Convert LocalDateTime to String
    
                return "City: " + city + ", Message: " + message + ", Timestamp: " + timestamp;
            })
            .collect(Collectors.toList());
    
        model.addAttribute("alerts", formattedAlerts);
        return "alerts"; // The Thymeleaf template name for the alerts page
    }
    
    @GetMapping("/weather-summary")
    public String showWeatherSummary(Model model) {
        List<DailyWeatherSummary> summaries = summaryRepository.findAll();
        model.addAttribute("summaries", summaries);

        // Check if there's no data
        if (summaries.isEmpty()) {
            model.addAttribute("message", "No weather data available.");
        }
        return "weatherSummary";
    }

        @Autowired
    private WeatherService weatherService;

    @GetMapping("/forecast")
    public String getWeatherForecast(Model model) {
        List<WeatherForecast> forecasts = weatherService.getLatestForecasts();
        model.addAttribute("forecasts", forecasts);
        return "forecast";
    }

}
