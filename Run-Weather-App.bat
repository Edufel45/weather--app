@echo off
title Weather App - Full Stack Application
color 0A

echo ================================================
echo     🌤️  WEATHER APP - FULL STACK APPLICATION
echo ================================================
echo.
echo Starting Weather App...
echo.
echo This will:
echo   1. Start the Spring Boot Backend Server
echo   2. Open the application in your browser
echo.
echo ================================================
echo.

REM Set Java environment
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.19
set PATH=%JAVA_HOME%\bin;%PATH%

REM Navigate to backend
cd /d C:\Users\koech\Desktop\weather-app\backend

echo ✅ Java environment configured
echo ✅ Starting Spring Boot server...
echo.
echo ================================================
echo    Server will start at: http://localhost:8080
echo    Press Ctrl+C to stop the server
echo ================================================
echo.

REM Open browser after 5 seconds
start /b "" cmd /c "timeout /t 5 /nobreak > nul && start http://localhost:8080"

REM Run the Spring Boot application
C:\maven\bin\mvn spring-boot:run

pause