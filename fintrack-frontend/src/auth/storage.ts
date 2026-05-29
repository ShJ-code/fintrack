import type { User } from "../api/types";

const KEY = "fintrack.auth";

export interface PersistedAuth {
    user: User;
    token: string;
}

export const authStorage = {
    load(): PersistedAuth | null {
        const raw = localStorage.getItem(KEY);
        if (!raw) return null;
        try { return JSON.parse(raw) as PersistedAuth; }
        catch { localStorage.removeItem(KEY); return null; }
    },
    save(value: PersistedAuth) { localStorage.setItem(KEY, JSON.stringify(value)); },
    clear() { localStorage.removeItem(KEY); },
};