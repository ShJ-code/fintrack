import { createContext, useContext, useEffect, useReducer, type ReactNode } from "react";
import { authReducer, initialAuthState } from "./reducer";
import type { AuthState, AuthAction } from "./types";
import type { User } from "../api/types";

interface AuthContextValue {
    state: AuthState;
    login: (user: User, token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const STORAGE_KEY = "fintrack.auth";

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [state, dispatch] = useReducer(authReducer, initialAuthState);

    useEffect(() => {
        const raw = localStorage.getItem(STORAGE_KEY);
        if (raw) {
            try {
                const parsed = JSON.parse(raw) as { user: User; token: string };
                dispatch({ type: "LOAD", payload: parsed });
                return;
            } catch {
                localStorage.removeItem(STORAGE_KEY);
            }
        }
        dispatch({ type: "LOAD", payload: null });
    }, []);

    useEffect(() => {
        if (state.status === "authenticated" && state.user && state.token) {
            localStorage.setItem(
                STORAGE_KEY,
                JSON.stringify({ user: state.user, token: state.token }),
            );
        } else if (state.status === "anonymous") {
            localStorage.removeItem(STORAGE_KEY);
        }
    }, [state]);

    const value: AuthContextValue = {
        state,
        login: (user, token) => dispatch({ type: "LOGIN", payload: { user, token } }),
        logout: () => dispatch({ type: "LOGOUT" }),
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export function useAuth(): AuthContextValue {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuth must be used inside <AuthProvider>");
    return ctx;
}