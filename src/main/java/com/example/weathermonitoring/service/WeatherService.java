package com.example.weathermonitoring.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.weathermonitoring.model.Alert;
import com.example.weathermonitoring.model.DailyWeatherSummary;
import com.example.weathermonitoring.model.WeatherData;
import com.example.weathermonitoring.model.WeatherForecast;
import com.example.weathermonitoring.repository.AlertRepository;
import com.example.weathermonitoring.repository.DailyWeatherSummaryRepository;
import com.example.weathermonitoring.repository.WeatherDataRepository;
import com.example.weathermonitoring.repository.WeatherForecastRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Value("${openweathermap.forecast.url}")
    private String forecastApiUrl;

    @Autowired
    private DailyWeatherSummaryRepository dailyWeatherSummaryRepository;

    @Autowired
    private WeatherForecastRepository weatherForecastRepository;

    private final WeatherDataRepository weatherDataRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    private final List<String> cities = Arrays.asList("Delhi,in", "Mumbai,in", "Chennai,in", "Bangalore,in", "Kolkata,in", "Hyderabad,in");

    @Scheduled(fixedRateString = "${data.refresh.interval}")
    public void fetchWeatherData() {
        for (String city : cities) {
            String url = String.format("%s?q=%s&APPID=%s", apiUrl, city, apiKey);
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                try {
                    processWeatherData(response, city);
                    processForecastData(response, city);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
    public void fetchWeatherForecasts() {
        for (String city : cities) {
            String url = String.format("%s?q=%s&APPID=%s", forecastApiUrl, city, apiKey);
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                try {
                    processForecastData(response, city);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processWeatherData(String response, String city) throws IOException {
        JsonNode root = objectMapper.readTree(response);
        String mainCondition = root.get("weather").get(0).get("main").asText();
        double temperature = root.get("main").get("temp").asDouble() - 273.15; // Convert Kelvin to Celsius
        double feelsLike = root.get("main").get("feels_like").asDouble() - 273.15;
        int humidity = root.get("main").get("humidity").asInt();
        double windSpeed = root.get("wind").get("speed").asDouble();
        LocalDateTime timestamp = LocalDateTime.now();
    
        WeatherData weatherData = new WeatherData();
        weatherData.setCity(city);
        weatherData.setMainCondition(mainCondition);
        weatherData.setTemperature(temperature);
        weatherData.setFeelsLike(feelsLike);
        weatherData.setHumidity(humidity);
        weatherData.setWindSpeed(windSpeed);
        weatherData.setTimestamp(timestamp);
    
        weatherDataRepository.save(weatherData);
    }

    private void processForecastData(String response, String city) throws IOException {
        JsonNode root = objectMapper.readTree(response);
        JsonNode list = root.get("list");
    
        for (JsonNode forecastNode : list) {
            LocalDateTime forecastTime = LocalDateTime.parse(forecastNode.get("dt_txt").asText());
            String mainCondition = forecastNode.get("weather").get(0).get("main").asText();
            double temperature = forecastNode.get("main").get("temp").asDouble() - 273.15;
            double feelsLike = forecastNode.get("main").get("feels_like").asDouble() - 273.15;
            double humidity = forecastNode.get("main").get("humidity").asDouble();
            double windSpeed = forecastNode.get("wind").get("speed").asDouble();
    
            // Create and save the forecast data
            WeatherForecast forecast = new WeatherForecast();
            forecast.setCity(city);
            forecast.setMainCondition(mainCondition);
            forecast.setTemperature(temperature);
            forecast.setFeelsLike(feelsLike);
            forecast.setHumidity(humidity);
            forecast.setWindSpeed(windSpeed);
            forecast.setForecastTime(forecastTime);
    
            weatherForecastRepository.save(forecast);
        }
    }
    

    @Scheduled(cron = "0 30 21 * * ?")  // Runs every day at 9:30 PM
    public void rollupDailyWeatherSummary() {
        for (String city : cities) {
            LocalDate today = LocalDate.now();
            List<WeatherData> dailyData = weatherDataRepository.findByCityAndTimestampBetween(
                city, today.atStartOfDay(), today.plusDays(1).atStartOfDay());
    
            if (!dailyData.isEmpty()) {
                double avgTemp = dailyData.stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.0);
                double maxTemp = dailyData.stream().mapToDouble(WeatherData::getTemperature).max().orElse(0.0);
                double minTemp = dailyData.stream().mapToDouble(WeatherData::getTemperature).min().orElse(0.0);
                double avgHumidity = dailyData.stream().mapToDouble(WeatherData::getHumidity).average().orElse(0.0);
                double avgWindSpeed = dailyData.stream().mapToDouble(WeatherData::getWindSpeed).average().orElse(0.0);
                
                String dominantCondition = dailyData.stream()
                    .collect(Collectors.groupingBy(WeatherData::getMainCondition, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("Unknown");
    
                DailyWeatherSummary summary = new DailyWeatherSummary();
                summary.setCity(city);
                summary.setAvgTemperature(avgTemp);
                summary.setMaxTemperature(maxTemp);
                summary.setMinTemperature(minTemp);
                summary.setAvgHumidity(avgHumidity);
                summary.setAvgWindSpeed(avgWindSpeed);
                summary.setDominantCondition(dominantCondition);
                summary.setDate(today);
    
                dailyWeatherSummaryRepository.save(summary);
            }
        }
    }
    
    @Scheduled(cron = "0 0 6 * * ?")  // Runs every day at 6:00 AM
public void rollupForecastWeatherSummary() {
    for (String city : cities) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<WeatherForecast> forecastData = weatherForecastRepository.findByCityAndForecastTimeBetween(
            city, today.atStartOfDay(), tomorrow.atStartOfDay());

        if (!forecastData.isEmpty()) {
            double avgTemp = forecastData.stream().mapToDouble(WeatherForecast::getTemperature).average().orElse(0.0);
            double maxTemp = forecastData.stream().mapToDouble(WeatherForecast::getTemperature).max().orElse(0.0);
            double minTemp = forecastData.stream().mapToDouble(WeatherForecast::getTemperature).min().orElse(0.0);
            double avgHumidity = forecastData.stream().mapToDouble(WeatherForecast::getHumidity).average().orElse(0.0);
            double avgWindSpeed = forecastData.stream().mapToDouble(WeatherForecast::getWindSpeed).average().orElse(0.0);

            String dominantCondition = forecastData.stream()
                .collect(Collectors.groupingBy(WeatherForecast::getMainCondition, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("Unknown");

            DailyWeatherSummary summary = new DailyWeatherSummary();
            summary.setCity(city);
            summary.setAvgTemperature(avgTemp);
            summary.setMaxTemperature(maxTemp);
            summary.setMinTemperature(minTemp);
            summary.setAvgHumidity(avgHumidity);
            summary.setAvgWindSpeed(avgWindSpeed);
            summary.setDominantCondition(dominantCondition);
            summary.setDate(tomorrow);

            dailyWeatherSummaryRepository.save(summary);
        }
    }
}


    private final double temperatureThreshold = 35.0;
    private final int consecutiveExceeds = 2;
    private final Map<String, Integer> exceedCountMap = new HashMap<>();

    @Scheduled(fixedRateString = "${data.refresh.interval}")
    public void checkAlerts() {
        for (String city : cities) {
            List<WeatherData> recentData = weatherDataRepository.findTop2ByCityOrderByTimestampDesc(city);
            if (recentData.size() == 2 && recentData.get(0).getTemperature() > temperatureThreshold &&
                recentData.get(1).getTemperature() > temperatureThreshold) {

                int exceedCount = exceedCountMap.getOrDefault(city, 0) + 1;
                if (exceedCount >= consecutiveExceeds) {
                    triggerAlert(city);
                    exceedCountMap.put(city, 0); // Reset counter after alert
                } else {
                    exceedCountMap.put(city, exceedCount);
                }
            }
        }
    }

    @Autowired
    private AlertRepository alertRepository;

    private void triggerAlert(String city) {
        String message = "ALERT: High temperature detected in " + city;
        System.out.println(message);

        // Save alert to the database
        Alert alert = new Alert();
        alert.setCity(city);
        alert.setMessage(message);
        alert.setTimestamp(LocalDateTime.now());
        alertRepository.save(alert);
    }

}
