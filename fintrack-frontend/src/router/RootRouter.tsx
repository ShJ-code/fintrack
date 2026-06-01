import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import Login from "../components/Login";
import Register from "../components/Register";
import RequireAuth from "./RequiredAuth";
import Layout from "../components/Layout";
import BillsPage from "../components/BillsPage";
import VendorsPage from "../components/VendorsPage";
import CustomersPage from "../components/CustomersPage";
import InvoicesPage from "../components/InvoicesPage";
import PaymentsPage from "../components/PaymentsPage";

const RootRouter = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/login"    element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route element={<RequireAuth><Layout /></RequireAuth>} >
        <Route path="/"          element={<Navigate to="/bills" replace />} />
        <Route path="/bills"     element={<BillsPage />} />
        <Route path="/vendors"   element={<VendorsPage />} />
        <Route path="/customers" element={<CustomersPage />} />
        <Route path="/invoices"  element={<InvoicesPage />} />
        <Route path="/payments"  element={<PaymentsPage />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  </BrowserRouter>
);

export default RootRouter;
