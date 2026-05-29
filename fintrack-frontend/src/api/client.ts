import axios from "axios";

const baseURL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

export const api = axios.create({
    baseURL,
    headers: { "Content-Type": "application/json" },
});

const STORAGE_KEY = "fintrack.auth";

// Attach Authorization header if we have a token.
api.interceptors.request.use((config) => {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (raw) {
        try {
            const { token } = JSON.parse(raw) as { token?: string };
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        } catch {  }
    }
    return config;
});

// On 401, drop credentials. AuthContext + RequireAuth will route us to /login.
api.interceptors.response.use(
    (res) => res,
    (err) => {
        if (err.response?.status === 401) {
            // Drop any cached identity.
            localStorage.removeItem(STORAGE_KEY);
            // Hard reload-style: easiest way to bounce back to /login through React Router.
            if (window.location.pathname !== "/login" && window.location.pathname !== "/register") {
                window.location.assign("/login");
            }
        }
        return Promise.reject(err);
    }
)