import type { AuthState, AuthAction } from "./types";

export const initialAuthState: AuthState = {
    user: null,
    token: null,
    status: "loading",
};

export function authReducer(state: AuthState, action: AuthAction): AuthState {
    switch (action.type) {
        case "LOAD":
            return action.payload
                ? { user: action.payload.user, token: action.payload.token, status: "authenticated" }
                : { user: null, token: null, status: "anonymous" };
        case "LOGIN":
            return { user: action.payload.user, token: action.payload.token, status: "authenticated" };
        case "LOGOUT":
            return { user: null, token: null, status: "anonymous" };
        default:
            return state;
    }
}