package com.weather.controller;

import com.weather.dto.WeatherResponse;
import com.weather.dto.ForecastResponse;
import com.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/weather/{city}")
    public ResponseEntity<?> getWeather(@PathVariable String city) {
        try {
            WeatherResponse weather = weatherService.getCurrentWeather(city);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("City not found: " + city);
        }
    }
    
    @GetMapping("/forecast/{city}")
    public ResponseEntity<?> getForecast(@PathVariable String city) {
        try {
            ForecastResponse forecast = weatherService.getForecast(city);
            return ResponseEntity.ok(forecast);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("City not found: " + city);
        }
    }
}