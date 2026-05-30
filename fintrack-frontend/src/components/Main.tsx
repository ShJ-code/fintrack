import { getVendors } from "../api/vendors";
import { useEffect, useState } from "react";
import { useAuth } from "../auth/AuthContext";
import type { Vendor } from "../api/types";

const Main: React.FC = () => {
  const [vendors, setVendors] = useState<Vendor[]>([]);
  const { state, logout } = useAuth();

  useEffect(() => {
    getVendors().then(setVendors).catch((err) => alert(err));
  }, []);

  return (
    <>
      <header style={{ display: "flex", justifyContent: "space-between", padding: 16 }}>
        <span>Welcome, {state.user?.username}</span>
        <button onClick={logout}>Logout</button>
      </header>
      <table style={{ width: "100%", borderCollapse: "collapse", marginTop: 30 }}>
        <thead>
          <tr>
            <th style={{ border: "1px solid #ccc", padding: 8 }}>Vendor Id</th>
            <th style={{ border: "1px solid #ccc", padding: 8 }}>Company Name</th>
          </tr>
        </thead>
        <tbody>
          {vendors.map((v) => (
            <tr key={v.vendorId}>
              <td style={{ border: "1px solid #ccc", padding: 8 }}>{v.vendorId}</td>
              <td style={{ border: "1px solid #ccc", padding: 8 }}>{v.companyName}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
};
export default Main;
