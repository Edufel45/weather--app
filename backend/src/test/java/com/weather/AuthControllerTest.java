package com.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testUserRegistration() {
        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
        String requestBody = "{\"name\":\"Test User\",\"email\":\"" + uniqueEmail + "\",\"password\":\"password123\"}";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/register", 
            request, 
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("User registered successfully!");
    }
    
    @Test
    public void testDuplicateEmailRegistration() {
        String requestBody = "{\"name\":\"Duplicate User\",\"email\":\"duplicate@test.com\",\"password\":\"password123\"}";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // First registration
        restTemplate.postForEntity("/api/auth/register", request, String.class);
        
        // Second registration with same email
        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/register", 
            request, 
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email is already taken");
    }
}