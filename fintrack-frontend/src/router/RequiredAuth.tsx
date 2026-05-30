import { Navigate } from "react-router-dom";
import type { ReactNode } from "react";
import { useAuth } from "../auth/AuthContext";

interface Props {
  children: ReactNode;
}

const RequireAuth = ({ children }: Props) => {
  const { state } = useAuth();
  if (state.status === "loading") return null;
  if (state.status === "anonymous") return <Navigate to="/login" replace />;
  return <>{children}</>;
};

export default RequireAuth;
