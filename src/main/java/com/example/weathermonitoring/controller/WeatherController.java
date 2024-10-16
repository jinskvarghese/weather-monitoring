package com.example.weathermonitoring.controller;

import com.example.weathermonitoring.model.DailyWeatherSummary;
import com.example.weathermonitoring.repository.DailyWeatherSummaryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class WeatherController {

    private final DailyWeatherSummaryRepository summaryRepository;

    public WeatherController(DailyWeatherSummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    @GetMapping("/daily-summaries")
    public String showDailySummaries(Model model) {
        LocalDate today = LocalDate.now();
        List<DailyWeatherSummary> summaries = summaryRepository.findByDate(today);
        model.addAttribute("summaries", summaries);
        return "dailySummaries";
    }
}
