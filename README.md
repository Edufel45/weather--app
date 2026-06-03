# 🌤️ Weather App - Full Stack Application

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Tests](https://img.shields.io/badge/Tests-7%20Passing-success.svg)](https://github.com/Edufel45/weather--app/actions)

> A complete weather application with user authentication, real-time weather data, 5-day forecasts, and favorite cities management.



## 📋 Table of Contents
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Quick Start](#-quick-start)
- [API Endpoints](#-api-endpoints)
- [Project Structure](#-project-structure)
- [Screenshots](#-screenshots)
- [Testing](#-testing)
- [License](#-license)

## ✨ Features

### 🔐 Authentication
- User registration with name, email, password
- Secure login with JWT tokens
- Password encryption using BCrypt
- Protected API endpoints

### 🌤️ Weather
- Search current weather by city name
- Display temperature, humidity, wind speed
- Real-time weather conditions
- 5-day weather forecast

### ⭐ Favorites
- Save up to 3 favorite cities
- View all saved cities with current weather
- Remove cities from favorites
- Per-user favorites management

### 🎨 Frontend
- Responsive design (mobile + desktop)
- Glass morphism UI effects
- Video background on auth pages
- Dynamic weather backgrounds (rain/clouds/sun)
- Loading states and error handling

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| **Backend** | Java 17, Spring Boot 3.1.5 |
| **Security** | JWT, Spring Security, BCrypt |
| **Database** | H2 (in-memory) |
| **Frontend** | HTML5, CSS3, Vanilla JavaScript |
| **API** | OpenWeatherMap API |
| **Build Tool** | Maven |
| **Testing** | JUnit 5, Spring Boot Test |

##  Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.9+

### Installation

**1. Clone the repository**
```bash
git clone https://github.com/Edufel45/weather--app.git
cd weather--app