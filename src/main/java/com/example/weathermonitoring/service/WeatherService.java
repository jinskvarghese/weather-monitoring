package com.example.weathermonitoring.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.weathermonitoring.model.WeatherData;
import com.example.weathermonitoring.repository.WeatherDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    private final WeatherDataRepository weatherDataRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson's ObjectMapper

    public WeatherService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    // List of cities for weather data retrieval
    private final List<String> cities = Arrays.asList("Delhi,in", "Mumbai,in", "Chennai,in", "Bangalore,in", "Kolkata,in", "Hyderabad,in");

    // Scheduled method to fetch weather data at a fixed interval
    @Scheduled(fixedRateString = "${data.refresh.interval}")
    public void fetchWeatherData() {
        for (String city : cities) {
            String url = String.format("%s?q=%s&APPID=%s", apiUrl, city, apiKey);
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                try {
                    processWeatherData(response, city);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to process the fetched weather data
    private void processWeatherData(String response, String city) throws IOException {
        JsonNode root = objectMapper.readTree(response);
        String mainCondition = root.get("weather").get(0).get("main").asText();
        double temperature = root.get("main").get("temp").asDouble() - 273.15; // Convert Kelvin to Celsius
        double feelsLike = root.get("main").get("feels_like").asDouble() - 273.15; // Convert Kelvin to Celsius
        long timestamp = root.get("dt").asLong();

        WeatherData weatherData = new WeatherData();
        weatherData.setCity(city);
        weatherData.setMainCondition(mainCondition);
        weatherData.setTemperature(temperature);
        weatherData.setFeelsLike(feelsLike);
        weatherData.setTimestamp(timestamp);

        // Save the weather data to the database
        weatherDataRepository.save(weatherData);
    }
}
