import { api } from "./client";

export interface Payment {
  paymentId: number;
  billId: number | null;
  invoiceId: number | null;
  amount: number;
  method: string;
  status: "pending" | "succeeded" | "failed";
  externalRef: string | null;
  failureReason: string | null;
  idempotencyKey: string;
  createdAt: string;
}

interface PayRequest {
  billId?: number;
  invoiceId?: number;
  method: string;
  idempotencyKey: string;
}

export async function getPayments(): Promise<Payment[]> {
  return (await api.get<Payment[]>("/payments")).data;
}

export async function pay(req: PayRequest): Promise<Payment> {
  return (await api.post<Payment>("/payments", req)).data;
}