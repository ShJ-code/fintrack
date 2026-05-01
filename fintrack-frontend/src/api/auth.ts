import { api } from "./client";
import type { User } from "./types";

export async function login(email: string, password: string): Promise<User> {
    const res = await api.post<User>("/login", { email, password });
    return res.data;
}