import axios from "axios";
import { authStorage } from "../auth/storage";

const baseURL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

export const api = axios.create({
    baseURL,
    headers: { "Content-Type": "application/json" },
});

// Attach Authorization header if we have a token.
api.interceptors.request.use((config) => {
    const auth = authStorage.load();
    if (auth?.token) {
        config.headers.Authorization = `Bearer ${auth.token}`;
    }
    return config;
});

// On 401, drop credentials. AuthContext + RequireAuth will route us to /login.
api.interceptors.response.use(
    (res) => res,
    (err) => {
        if (err.response?.status === 401) {
            // Drop any cached identity.
            authStorage.clear();
            // Hard reload-style: easiest way to bounce back to /login through React Router.
            if (window.location.pathname !== "/login" && window.location.pathname !== "/register") {
                window.location.assign("/login");
            }
        }
        return Promise.reject(err);
    }
)