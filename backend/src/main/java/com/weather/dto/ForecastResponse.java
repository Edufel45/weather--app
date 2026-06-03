package com.weather.dto;

import java.util.List;

public class ForecastResponse {
    private String cityName;
    private List<DailyForecast> forecast;
    
    public ForecastResponse() {}
    
    public ForecastResponse(String cityName, List<DailyForecast> forecast) {
        this.cityName = cityName;
        this.forecast = forecast;
    }
    
    public String getCityName() { return cityName; }
    public List<DailyForecast> getForecast() { return forecast; }
    
    public static class DailyForecast {
        private String date;
        private double highTemp;
        private double lowTemp;
        private String condition;
        
        public DailyForecast() {}
        
        public DailyForecast(String date, double highTemp, double lowTemp, String condition) {
            this.date = date;
            this.highTemp = highTemp;
            this.lowTemp = lowTemp;
            this.condition = condition;
        }
        
        public String getDate() { return date; }
        public double getHighTemp() { return highTemp; }
        public double getLowTemp() { return lowTemp; }
        public String getCondition() { return condition; }
    }
}