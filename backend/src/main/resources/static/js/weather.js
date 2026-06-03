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
        const weather = await API.getWeather(city);
        if (weather && weather.cityName) {
            displayCurrentWeather(weather);
        } else {
            showError('City not found');
        }
        
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
    const icon = getWeatherIcon(weather.condition);
    document.getElementById('cityName').innerHTML = `${icon} ${weather.cityName}`;
    document.getElementById('temperature').innerHTML = `${Math.round(weather.temperature)}°C`;
    document.getElementById('condition').innerHTML = weather.condition;
    document.getElementById('humidity').innerHTML = `${weather.humidity}%`;
    document.getElementById('windSpeed').innerHTML = `${weather.windSpeed} km/h`;
    
    document.getElementById('currentWeather').style.display = 'block';
    document.getElementById('forecastSection').style.display = 'block';
    
    const favBtn = document.getElementById('favoriteBtn');
    favBtn.onclick = () => addToFavorites(weather.cityName);
    
    updateWeatherBackground(weather.condition);
}

function displayForecast(forecast) {
    const grid = document.getElementById('forecastGrid');
    grid.innerHTML = '';
    
    forecast.forecast.forEach(day => {
        const card = document.createElement('div');
        card.className = 'forecast-card';
        const icon = getWeatherIcon(day.condition);
        card.innerHTML = `
            <strong>${day.date}</strong><br>
            <div style="font-size: 28px;">${icon}</div>
            <span style="font-size: 18px; font-weight: bold;">${Math.round(day.highTemp)}°C</span><br>
            <small>${day.condition}</small>
        `;
        grid.appendChild(card);
    });
}

function getWeatherIcon(condition) {
    const cond = condition.toLowerCase();
    if (cond.includes('rain')) return '🌧️';
    if (cond.includes('cloud')) return '☁️';
    if (cond.includes('clear') || cond.includes('sun')) return '☀️';
    if (cond.includes('snow')) return '❄️';
    if (cond.includes('thunder')) return '⛈️';
    if (cond.includes('mist') || cond.includes('fog')) return '🌫️';
    return '🌤️';
}

function updateWeatherBackground(condition) {
    const body = document.body;
    let animationDiv = document.getElementById('weatherAnimation');
    
    if (!animationDiv) {
        animationDiv = document.createElement('div');
        animationDiv.id = 'weatherAnimation';
        document.body.appendChild(animationDiv);
    }
    
    animationDiv.innerHTML = '';
    body.classList.remove('weather-bg-rain', 'weather-bg-clouds', 'weather-bg-sunny');
    
    const conditionLower = condition.toLowerCase();
    
    if (conditionLower.includes('rain') || conditionLower.includes('drizzle')) {
        body.classList.add('weather-bg-rain');
        for (let i = 0; i < 80; i++) {
            const drop = document.createElement('div');
            drop.className = 'rain-drop';
            drop.style.left = Math.random() * 100 + '%';
            drop.style.animationDuration = Math.random() * 1 + 0.5 + 's';
            drop.style.animationDelay = Math.random() * 5 + 's';
            animationDiv.appendChild(drop);
        }
    } 
    else if (conditionLower.includes('cloud') || conditionLower.includes('overcast')) {
        body.classList.add('weather-bg-clouds');
        for (let i = 0; i < 5; i++) {
            const cloud = document.createElement('div');
            cloud.className = 'cloud';
            cloud.style.width = Math.random() * 200 + 100 + 'px';
            cloud.style.height = Math.random() * 100 + 50 + 'px';
            cloud.style.top = Math.random() * 50 + '%';
            cloud.style.animationDuration = Math.random() * 20 + 10 + 's';
            cloud.style.animationDelay = Math.random() * 10 + 's';
            animationDiv.appendChild(cloud);
        }
    }
    else if (conditionLower.includes('clear') || conditionLower.includes('sun')) {
        body.classList.add('weather-bg-sunny');
        const sun = document.createElement('div');
        sun.className = 'sun';
        animationDiv.appendChild(sun);
    }
}

async function addToFavorites(city) {
    try {
        const result = await API.addFavorite(city);
        if (result === 'City added to favorites') {
            showSuccess(`${city} added to favorites!`);
            loadFavorites();
        } else if (result && result.includes('Maximum')) {
            showError('Maximum 3 favorite cities allowed!');
        } else if (result && result.includes('already')) {
            showError(`${city} is already in your favorites!`);
        } else {
            showError(result || 'Failed to add favorite');
        }
    } catch (error) {
        showError('Failed to add favorite');
    }
}

function showLoading(show) {
    const loader = document.getElementById('loading');
    if (loader) loader.style.display = show ? 'flex' : 'none';
}

function showError(message) {
    alert(message);
}

function showSuccess(message) {
    alert(message);
}

// Event listeners
if (document.getElementById('searchBtn')) {
    document.getElementById('searchBtn').addEventListener('click', searchWeather);
    document.getElementById('cityInput').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') searchWeather();
    });
}