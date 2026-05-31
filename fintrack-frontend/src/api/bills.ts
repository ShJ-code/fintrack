import { api } from "./client";
import type { Bill } from "./types";

type BillRequest = Pick<Bill, "vendorId" | "amount" | "dueDate" | "status">;

function toBillRequest(bill: Bill): BillRequest {
    return {
        vendorId: bill.vendorId,
        amount: bill.amount,
        dueDate: bill.dueDate,
        status: bill.status,
    };
}

export async function getBills(): Promise<Bill[]> {
    return (await api.get("/bills")).data;
}
export async function createBill(bill: Bill): Promise<Bill> {
    return (await api.post<Bill>("/bills", toBillRequest(bill))).data;
}
export async function updateBill(bill: Bill): Promise<Bill> {
    return (await api.put<Bill>(`/bills/${bill.billId}`, toBillRequest(bill))).data;
}
export async function deleteBill(billId: number): Promise<void> {
    await api.delete(`/bills/${billId}`);
}