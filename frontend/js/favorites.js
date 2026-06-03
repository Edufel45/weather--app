// Favorites functionality
async function loadFavorites() {
    const grid = document.getElementById('favoritesGrid');
    grid.innerHTML = '<div class="loading" style="display:block;"><div class="spinner"></div></div>';
    
    try {
        const favorites = await API.getFavorites();
        displayFavorites(favorites);
    } catch (error) {
        grid.innerHTML = '<p style="text-align:center;">Failed to load favorites</p>';
    }
}

function displayFavorites(favorites) {
    const grid = document.getElementById('favoritesGrid');
    grid.innerHTML = '';
    
    if (!favorites || favorites.length === 0) {
        grid.innerHTML = '<p style="text-align:center; color:#666;">No favorites yet. Search for a city and click "Save to Favorites"!</p>';
        return;
    }
    
    favorites.forEach(fav => {
        const card = document.createElement('div');
        card.className = 'favorite-card';
        card.innerHTML = `
            <button class="remove-fav" data-city="${fav.cityName}">×</button>
            <h3>${fav.cityName}</h3>
            <div style="font-size: 24px; margin: 10px 0;">${Math.round(fav.temperature)}°C</div>
            <div>${fav.condition}</div>
            <div style="font-size: 12px; margin-top: 10px;">
                💧 ${fav.humidity}% | 💨 ${fav.windSpeed} km/h
            </div>
        `;
        grid.appendChild(card);
    });
    
    // Add remove event listeners
    document.querySelectorAll('.remove-fav').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.stopPropagation();
            const city = btn.getAttribute('data-city');
            await removeFavorite(city);
        });
    });
}

async function removeFavorite(city) {
    try {
        const result = await API.removeFavorite(city);
        if (result === 'City removed from favorites') {
            alert(`${city} removed from favorites`);
            loadFavorites();
            // Refresh current weather display if showing removed city
            if (currentCity === city) {
                document.getElementById('favoriteBtn').style.display = 'block';
            }
        } else {
            alert('Failed to remove favorite');
        }
    } catch (error) {
        alert('Failed to remove favorite');
    }
}