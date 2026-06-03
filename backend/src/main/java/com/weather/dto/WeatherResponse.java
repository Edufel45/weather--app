package com.weather.dto;

public class WeatherResponse {
    private String cityName;
    private double temperature;
    private String condition;
    private int humidity;
    private double windSpeed;
    
    public WeatherResponse() {}
    
    public WeatherResponse(String cityName, double temperature, String condition, int humidity, double windSpeed) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.condition = condition;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }
    
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }
    
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
}