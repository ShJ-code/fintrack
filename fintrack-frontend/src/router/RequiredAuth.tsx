import React from "react";
import { Navigate, useLocation } from "react-router-dom";

interface RequireAuthProps {
  children: any;
}

const RequireAuth: React.FC<RequireAuthProps> = ({children}) => {
  const login = localStorage.getItem("user");
  if (!login) {
    return <Navigate to="/login" />;
  }
  return <>{children}</>;
};

export default RequireAuth;
