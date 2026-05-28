import type { User } from "../api/types";

export interface AuthState {
    user: User | null;
    token: string | null;
    status: "loading" | "authenticated" | "anonymous";
}

export type AuthAction =
    | { type: "LOAD"; payload: { user: User; token: string } | null }
    | { type: "LOGIN"; payload: { user: User; token: string } }
    | { type: "LOGOUT" };