// Weather functionality
let currentCity = '';

async function searchWeather() {
    const city = document.getElementById('cityInput').value.trim();
    if (!city) {
        alert('Please enter a city name');
        return;
    }
    
    currentCity = city;
    showLoading(true);
    
    try {
        // Get current weather
        const weather = await API.getWeather(city);
        if (weather && !weather.startsWith && weather.cityName) {
            displayCurrentWeather(weather);
        } else {
            showError('City not found');
        }
        
        // Get forecast
        const forecast = await API.getForecast(city);
        if (forecast && forecast.forecast) {
            displayForecast(forecast);
        }
    } catch (error) {
        showError('Failed to fetch weather data');
    } finally {
        showLoading(false);
    }
}

function displayCurrentWeather(weather) {
    document.getElementById('cityName').textContent = weather.cityName;
    document.getElementById('temperature').textContent = `${Math.round(weather.temperature)}°C`;
    document.getElementById('condition').textContent = weather.condition;
    document.getElementById('humidity').textContent = `${weather.humidity}%`;
    document.getElementById('windSpeed').textContent = `${weather.windSpeed} km/h`;
    
    document.getElementById('currentWeather').style.display = 'block';
    document.getElementById('forecastSection').style.display = 'block';
    
    // Update favorite button
    const favBtn = document.getElementById('favoriteBtn');
    favBtn.onclick = () => addToFavorites(weather.cityName);
}

function displayForecast(forecast) {
    const grid = document.getElementById('forecastGrid');
    grid.innerHTML = '';
    
    forecast.forecast.forEach(day => {
        const card = document.createElement('div');
        card.className = 'forecast-card';
        card.innerHTML = `
            <strong>${day.date}</strong><br>
            <span style="font-size: 18px;">${Math.round(day.highTemp)}°C</span><br>
            <small>${day.condition}</small>
        `;
        grid.appendChild(card);
    });
}

async function addToFavorites(city) {
    try {
        const result = await API.addFavorite(city);
        if (result === 'City added to favorites') {
            alert(`${city} added to favorites!`);
            loadFavorites();
        } else if (result.includes('Maximum')) {
            alert('Maximum 3 favorite cities allowed!');
        } else if (result.includes('already')) {
            alert(`${city} is already in your favorites!`);
        } else {
            alert(result || 'Failed to add favorite');
        }
    } catch (error) {
        alert('Failed to add favorite');
    }
}

function showLoading(show) {
    document.getElementById('loading').style.display = show ? 'block' : 'none';
}

function showError(message) {
    alert(message);
}

// Event listeners
document.getElementById('searchBtn').addEventListener('click', searchWeather);
document.getElementById('cityInput').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') searchWeather();
});