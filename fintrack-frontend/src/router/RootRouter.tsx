import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import Login from "../components/Login.tsx";
// import MainPage from "../components/MainPage";

import RequireAuth from "./RequiredAuth.tsx";
import Main from "../components/main.tsx";

const RootRouter: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={
            <Login />
          }
        />
      </Routes>
      <Routes>
        <Route
          path="/main"
          element={
            <RequireAuth>
              {" "}
              <Main />{" "}
            </RequireAuth>
          }
        />
      </Routes>
    </Router>
  );
};

export default RootRouter;
