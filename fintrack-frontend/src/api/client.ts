import axios from "axios";

const baseURL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

export const api = axios.create({
    baseURL,
    headers: { "Content-Type": "application/json" },
});

// Single place to react to authentication failures globally.
api.interceptors.response.use(
    (res) => res,
    (err) => {
        if (err.response?.status === 401) {
            // Drop any cached identity.
            localStorage.removeItem("user");
        }
        return Promise.reject(err);
    }
)