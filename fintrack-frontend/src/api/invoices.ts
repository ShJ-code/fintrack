import { api } from "./client";
import type { Invoice } from "./types";

type InvoiceRequest = Pick<Invoice, "customerId" | "amount" | "dueDate" | "status">;

function toInvoiceRequest(invoice: Invoice): InvoiceRequest {
    return {
        customerId: invoice.customerId,
        amount: invoice.amount,
        dueDate: invoice.dueDate,
        status: invoice.status,
    };
}

export async function getInvoices(): Promise<Invoice[]> {
    return (await api.get("/invoices")).data;
}
export async function createInvoice(invoice: Invoice): Promise<Invoice> {
    return (await api.post<Invoice>("/invoices", toInvoiceRequest(invoice))).data;
}
export async function updateInvoice(invoice: Invoice): Promise<Invoice> {
    return (await api.put<Invoice>(`/invoices/${invoice.invoiceId}`, toInvoiceRequest(invoice))).data;
}
export async function deleteInvoice(invoiceId: number): Promise<void> {
    await api.delete(`/invoices/${invoiceId}`);
}