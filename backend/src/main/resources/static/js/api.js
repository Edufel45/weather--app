const API_BASE = '/api';

class API {
    static getToken() {
        return localStorage.getItem('token');
    }
    
    static setToken(token) {
        localStorage.setItem('token', token);
    }
    
    static clearToken() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }
    
    static getUser() {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
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
        
        try {
            const response = await fetch(`${API_BASE}${endpoint}`, {
                ...options,
                headers
            });
            
            if (response.status === 401 || response.status === 403) {
                this.clearToken();
                window.location.href = '/login.html';
                return null;
            }
            
            const text = await response.text();
            try {
                return JSON.parse(text);
            } catch {
                return text;
            }
        } catch (error) {
            console.error('API Error:', error);
            return { error: 'Network error' };
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
            localStorage.setItem('user', JSON.stringify({ email: result.email, name: result.name }));
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