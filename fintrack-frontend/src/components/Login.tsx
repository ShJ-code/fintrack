import { login as loginApi } from "../api/auth";
import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

const Login: React.FC = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      const { user, token } = await loginApi(email, password);
      login(user, token);
      navigate("/main");
    } catch (err: unknown) {
      const status = (err as { response?: { status?: number } })?.response?.status;
      setError(status === 401 ? "Invalid email or password" : "Login failed");
    } finally {
      setSubmitting(false);
    }
  };
  
  return (
    <div style={{ maxWidth: 400, margin: "0 auto", paddingTop: 50 }}>
      <h1>Login</h1>
      <form onSubmit={onSubmit}>
        <div>Email: <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required /></div>
        <div>Password: <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required /></div>
        {error && <div style={{ color: "red" }}>{error}</div>}
        <button type="submit" disabled={submitting}>{submitting ? "Logging in ..." : "Login"}</button>
      </form>
      <p>No account? <Link to="/register">Register</Link></p>
    </div>
  );
};

export default Login;
