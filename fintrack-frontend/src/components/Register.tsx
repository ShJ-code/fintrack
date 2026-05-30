import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { register as registerApi } from "../api/auth";
import { useAuth } from "../auth/AuthContext";

const Register: React.FC = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);
    const navigate = useNavigate();
    const { login } = useAuth();

    const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);
        setSubmitting(true);
        try {
            const { user, token } = await registerApi(name, email, password);
            login(user, token);
            navigate("/main");
        } catch (err: unknown) {
            const message = (err as { response?: { data?: { error?: string } } })
                ?.response?.data?.error ?? "Registration failed";
            setError(message);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: "0 auto", paddingTop: 50 }}>
            <h1>Create an account</h1>
            <form onSubmit={onSubmit}>
                <div>Name: <input value={name} onChange={(e) => setName(e.target.value)} required /></div>
                <div>Email: <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required /></div>
                <div>Password: <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} minLength={8} required /></div>
                {error && <div style={{ color: "red" }}>{error}</div>}
                <button type="submit" disabled={submitting}>{submitting ? "Creating..." : "Register"}</button>
            </form>
            <p>Already have an account? <Link to="/login">Log in</Link></p>
        </div>
    );
};

export default Register;