package com.weather.controller;

import com.weather.dto.WeatherResponse;
import com.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {
    
    @Autowired
    private WeatherService weatherService;
    
    // Simple in-memory storage for favorites per user
    private static final ConcurrentMap<String, List<String>> userFavorites = new ConcurrentHashMap<>();
    
    private String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
    
    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestBody String cityName) {
        String user = getCurrentUser();
        cityName = cityName.replace("\"", "");
        
        List<String> favorites = userFavorites.computeIfAbsent(user, k -> new ArrayList<>());
        
        if (favorites.size() >= 3) {
            return ResponseEntity.badRequest().body("Maximum 3 favorite cities allowed");
        }
        
        if (favorites.contains(cityName)) {
            return ResponseEntity.badRequest().body("City already in favorites");
        }
        
        favorites.add(cityName);
        return ResponseEntity.ok("City added to favorites");
    }
    
    @GetMapping
    public ResponseEntity<?> getFavorites() {
        String user = getCurrentUser();
        List<String> favorites = userFavorites.getOrDefault(user, new ArrayList<>());
        
        List<WeatherResponse> favoriteWeather = new ArrayList<>();
        for (String city : favorites) {
            try {
                WeatherResponse weather = weatherService.getCurrentWeather(city);
                favoriteWeather.add(weather);
            } catch (Exception e) {
                WeatherResponse error = new WeatherResponse();
                error.setCityName(city);
                error.setCondition("City not found");
                favoriteWeather.add(error);
            }
        }
        
        return ResponseEntity.ok(favoriteWeather);
    }
    
    @DeleteMapping("/{cityName}")
    public ResponseEntity<?> removeFavorite(@PathVariable String cityName) {
        String user = getCurrentUser();
        List<String> favorites = userFavorites.get(user);
        
        if (favorites != null && favorites.remove(cityName)) {
            return ResponseEntity.ok("City removed from favorites");
        }
        
        return ResponseEntity.badRequest().body("City not in favorites");
    }
}