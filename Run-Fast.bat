@echo off
title Weather App - Fast Mode
color 0A
echo ================================================
echo     🌤️  WEATHER APP - FAST MODE
echo ================================================
echo.
echo Starting pre-built JAR (faster than Maven)...
echo.

cd /d C:\Users\koech\Desktop\weather-app\backend
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.19
set PATH=%JAVA_HOME%\bin;%PATH%

start http://localhost:8080

echo Server starting at: http://localhost:8080
echo.
java -jar target/*.jar

pause