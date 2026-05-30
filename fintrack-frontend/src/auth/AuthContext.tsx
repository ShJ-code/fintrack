import { createContext, useContext, useEffect, useReducer, type ReactNode } from "react";
import { authReducer, initialAuthState } from "./reducer";
import { authStorage } from "./storage";
import type { AuthState } from "./types";
import type { User } from "../api/types";

interface AuthContextValue {
    state: AuthState;
    login: (user: User, token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [state, dispatch] = useReducer(authReducer, initialAuthState);

    useEffect(() => {
        dispatch({ type: "LOAD", payload: authStorage.load() });
    }, []);

    useEffect(() => {
        if (state.status === "authenticated" && state.user && state.token) {
            authStorage.save({ user: state.user, token: state.token });
        } else if (state.status === "anonymous") {
            authStorage.clear();
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