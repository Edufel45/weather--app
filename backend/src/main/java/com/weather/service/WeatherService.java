package com.weather.service;

import com.weather.dto.WeatherResponse;
import com.weather.dto.ForecastResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    
    @Value("${weather.api.key:fecc84e7d3769253ea2d0a427c3048c7}")
    private String apiKey;
    
    @Value("${weather.api.url:http://api.openweathermap.org/data/2.5}")
    private String apiUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public WeatherResponse getCurrentWeather(String city) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "/weather")
                    .queryParam("q", city)
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .build()
                    .toUriString();
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode json = objectMapper.readTree(response);
            
            String cityName = json.get("name").asText();
            double temp = json.get("main").get("temp").asDouble();
            String condition = json.get("weather").get(0).get("description").asText();
            int humidity = json.get("main").get("humidity").asInt();
            double windSpeed = json.get("wind").get("speed").asDouble();
            
            return new WeatherResponse(cityName, temp, condition, humidity, windSpeed);
        } catch (Exception e) {
            throw new RuntimeException("City not found: " + city);
        }
    }
    
    public ForecastResponse getForecast(String city) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "/forecast")
                    .queryParam("q", city)
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .build()
                    .toUriString();
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode json = objectMapper.readTree(response);
            
            String cityName = json.get("city").get("name").asText();
            JsonNode list = json.get("list");
            
            List<ForecastResponse.DailyForecast> forecasts = new ArrayList<>();
            String lastDate = "";
            
            for (JsonNode item : list) {
                String date = item.get("dt_txt").asText().split(" ")[0];
                if (!date.equals(lastDate) && forecasts.size() < 5) {
                    double temp = item.get("main").get("temp").asDouble();
                    String condition = item.get("weather").get(0).get("description").asText();
                    forecasts.add(new ForecastResponse.DailyForecast(date, temp, temp, condition));
                    lastDate = date;
                }
            }
            
            return new ForecastResponse(cityName, forecasts);
        } catch (Exception e) {
            throw new RuntimeException("City not found: " + city);
        }
    }
}