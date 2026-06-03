package com.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testUserLogin() {
        // First register a user
        String registerBody = "{\"name\":\"LoginTest\",\"email\":\"logintest@test.com\",\"password\":\"123456\"}";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> registerRequest = new HttpEntity<>(registerBody, headers);
        
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(
            "/api/auth/register", 
            registerRequest, 
            String.class
        );
        
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Then try to login with correct credentials
        String loginBody = "{\"email\":\"logintest@test.com\",\"password\":\"123456\"}";
        HttpEntity<String> loginRequest = new HttpEntity<>(loginBody, headers);
        
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
            "/api/auth/login",
            loginRequest,
            String.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).contains("token");
        assertThat(loginResponse.getBody()).contains("logintest@test.com");
    }
    
    @Test
    public void testLoginWithWrongPassword() {
        // First register a user
        String registerBody = "{\"name\":\"WrongPassTest\",\"email\":\"wrongpass@test.com\",\"password\":\"123456\"}";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> registerRequest = new HttpEntity<>(registerBody, headers);
        
        restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        
        // Try to login with wrong password
        String loginBody = "{\"email\":\"wrongpass@test.com\",\"password\":\"wrongpassword\"}";
        HttpEntity<String> loginRequest = new HttpEntity<>(loginBody, headers);
        
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
            "/api/auth/login",
            loginRequest,
            String.class
        );

        // Should fail with 400 or 401
        assertThat(loginResponse.getStatusCode()).isNotEqualTo(HttpStatus.OK);
    }
}