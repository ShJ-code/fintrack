import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

const linkStyle = ({ isActive }: { isActive: boolean }) => ({
  padding: "8px 16px", color: isActive ? "white" : "#bbb", textDecoration: "none",
});

const Layout = () => {
  const { state, logout } = useAuth();
  return (
    <div>
      <header style={{ display: "flex", padding: 12, borderBottom: "1px solid #444", alignItems: "center" }}>
        <strong style={{ marginRight: 24 }}>FinTrack</strong>
        <NavLink to="/bills"     style={linkStyle}>Bills</NavLink>
        <NavLink to="/vendors"   style={linkStyle}>Vendors</NavLink>
        <NavLink to="/customers" style={linkStyle}>Customers</NavLink>
        <NavLink to="/invoices"  style={linkStyle}>Invoices</NavLink>
        <NavLink to="/payments"  style={linkStyle}>Payments</NavLink>
        <span style={{ marginLeft: "auto", marginRight: 16 }}>{state.user?.username}</span>
        <button onClick={logout}>Logout</button>
      </header>
      <main><Outlet /></main>
    </div>
  );
};

export default Layout;