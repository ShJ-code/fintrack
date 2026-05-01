import axios from "axios";
import { getVendors } from "../api/vendors";

import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

interface Vendor {
  vendorId: number;
  userId: number;
  companyName: string;
}

const Main: React.FC = () => {
  const [vendors, setVendors] = useState<Vendor[]>([]);

  const fetchVendors = async (userId: number) => {
    try {
      const data = await getVendors(userId);
      setVendors(data);
      // const res = await axios.get(
      //   `http://localhost:8080/vendors?userId=${userId}`,
      // );
      // setVendors(res.data);
      // console.log(res.data);
    } catch (err) {
      alert(err);
    }
  };

  useEffect(() => {
    const user = localStorage.getItem("user");
    if (user) {
        fetchVendors(JSON.parse(user).id);
    }
  }, []);

  return (
    <>
      <table
        style={{ width: "100%", borderCollapse: "collapse", marginTop: 30 }}
      >
        <thead>
          <tr>
            <th style={{ border: "1px solid #ccc", padding: 8 }}>Vendor Id</th>
            <th style={{ border: "1px solid #ccc", padding: 8 }}>User Id</th>
            <th style={{ border: "1px solid #ccc", padding: 8 }}>
              Company Name
            </th>
          </tr>
        </thead>
        <tbody>
          {vendors.map((vendor: Vendor) => {
            return (
              <tr>
                <th style={{ border: "1px solid #ccc", padding: 8 }}>
                  {vendor.vendorId}
                </th>
                <th style={{ border: "1px solid #ccc", padding: 8 }}>
                  {vendor.userId}
                </th>
                <th style={{ border: "1px solid #ccc", padding: 8 }}>
                  {vendor.companyName}
                </th>
              </tr>
            );
          })}
        </tbody>
      </table>
    </>
  );
};
export default Main;
