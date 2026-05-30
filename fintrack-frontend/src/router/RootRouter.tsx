import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import Login from "../components/Login.tsx";
import Register from "../components/Register";
import RequireAuth from "./RequiredAuth.tsx";
import Main from "../components/Main.tsx";

const RootRouter: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/main" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/main"
          element={<RequireAuth><Main /></RequireAuth>}
        />
        <Route path="*" element={<Navigate to="/main" replace />} />
      </Routes>
    </BrowserRouter>
  );
};

export default RootRouter;
