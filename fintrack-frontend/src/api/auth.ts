import { api } from "./client";
import type { User } from "./types";

export interface AuthResult {
    user: User;
    token: string;
}

export async function login(email: string, password: string): Promise<AuthResult> {
    const res = await api.post<{ id: number; name: string; email: string; token: string }>(
        "/login", { email, password }
    );
    return {
        user: { id: res.data.id, username: res.data.name, email: res.data.email },
        token: res.data.token,
    };
}

export async function register(name: string, email: string, password: string): Promise<AuthResult> {
    const res = await api.post<{ id: number; name: string; email: string; token: string }>(
        "/register", { name, email, password }
    );
    return {
        user: { id: res.data.id, username: res.data.name, email: res.data.email },
        token: res.data.token,
    };
}