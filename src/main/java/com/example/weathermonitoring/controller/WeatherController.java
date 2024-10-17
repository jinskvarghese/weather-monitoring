package com.example.weathermonitoring.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.weathermonitoring.model.DailyWeatherSummary;
import com.example.weathermonitoring.repository.DailyWeatherSummaryRepository;

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

    // @GetMapping("/weather-summary")
    // public String showWeatherSummary(Model model) {
    //     List<DailyWeatherSummary> summaries = summaryRepository.findAll();
    //     model.addAttribute("summaries", summaries);

    //     // Check if there's no data
    //     if (summaries.isEmpty()) {
    //         model.addAttribute("message", "No weather data available.");
    //     }
    //     return "weather-summary";
    // }

}
