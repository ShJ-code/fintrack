import { useEffect, useState } from "react";
import { getPayments, type Payment } from "../api/payments";

const PaymentsPage = () => {
  const [payments, setPayments] = useState<Payment[]>([]);
  useEffect(() => { getPayments().then(setPayments); }, []);
  return (
    <section style={{ padding: 16 }}>
      <h1>Payments</h1>
      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th>Time</th><th>Amount</th><th>Method</th><th>Status</th><th>Reference</th>
          </tr>
        </thead>
        <tbody>
          {payments.map((p) => (
            <tr key={p.paymentId}>
              <td>{new Date(p.createdAt).toLocaleString()}</td>
              <td>${p.amount}</td>
              <td>{p.method}</td>
              <td>{p.status}{p.failureReason ? ` (${p.failureReason})` : ""}</td>
              <td>{p.externalRef ?? "—"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
};

export default PaymentsPage;