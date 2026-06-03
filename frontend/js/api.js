const API_BASE = 'http://localhost:8080/api';

class API {
    static getToken() {
        return localStorage.getItem('token');
    }
    
    static setToken(token) {
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(token));
    }
    
    static clearToken() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }
    
    static async request(endpoint, options = {}) {
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };
        
        const token = this.getToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        
        const response = await fetch(`${API_BASE}${endpoint}`, {
            ...options,
            headers
        });
        
        if (response.status === 401 || response.status === 403) {
            this.clearToken();
            window.location.href = '/frontend/login.html';
            return null;
        }
        
        const text = await response.text();
        try {
            return JSON.parse(text);
        } catch {
            return text;
        }
    }
    
    static async register(name, email, password) {
        return this.request('/auth/register', {
            method: 'POST',
            body: JSON.stringify({ name, email, password })
        });
    }
    
    static async login(email, password) {
        const result = await this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        
        if (result && result.token) {
            this.setToken(result.token);
        }
        return result;
    }
    
    static async getWeather(city) {
        return this.request(`/weather/${encodeURIComponent(city)}`);
    }
    
    static async getForecast(city) {
        return this.request(`/forecast/${encodeURIComponent(city)}`);
    }
    
    static async addFavorite(city) {
        return this.request('/favorites', {
            method: 'POST',
            body: JSON.stringify(city)
        });
    }
    
    static async getFavorites() {
        return this.request('/favorites');
    }
    
    static async removeFavorite(city) {
        return this.request(`/favorites/${encodeURIComponent(city)}`, {
            method: 'DELETE'
        });
    }
}