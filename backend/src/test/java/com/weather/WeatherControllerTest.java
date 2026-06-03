package com.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getAuthToken() throws Exception {
        // Register user
        String registerBody = "{\"name\":\"WeatherTest\",\"email\":\"weathertest@test.com\",\"password\":\"123456\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> registerRequest = new HttpEntity<>(registerBody, headers);
        restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        
        // Login to get token
        String loginBody = "{\"email\":\"weathertest@test.com\",\"password\":\"123456\"}";
        HttpEntity<String> loginRequest = new HttpEntity<>(loginBody, headers);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
            "/api/auth/login", 
            loginRequest, 
            String.class
        );
        
        // Extract token from response
        JsonNode json = objectMapper.readTree(loginResponse.getBody());
        return json.get("token").asText();
    }

    @Test
    public void testGetWeather_RequiresAuthentication() throws Exception {
        // Without token - should return 401 or 403
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/weather/London",
            String.class
        );
        
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetWeather_WithValidToken() throws Exception {
        String token = getAuthToken();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> weatherResponse = restTemplate.exchange(
            "/api/weather/London",
            HttpMethod.GET,
            entity,
            String.class
        );
        
        assertThat(weatherResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(weatherResponse.getBody()).contains("cityName");
        assertThat(weatherResponse.getBody()).contains("temperature");
    }
    
    @Test
    public void testGetForecast_WithValidToken() throws Exception {
        String token = getAuthToken();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> forecastResponse = restTemplate.exchange(
            "/api/forecast/London",
            HttpMethod.GET,
            entity,
            String.class
        );
        
        assertThat(forecastResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forecastResponse.getBody()).contains("forecast");
    }
}