package com.example.weathermonitoring;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WeatherMonitoringApplicationTests {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Test
    public void contextLoads() {
        assertNotNull(apiKey, "API key should not be null");
    }
}
