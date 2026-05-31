import { api } from "./client";
import type { Customer } from "./types";

export async function getCustomers(): Promise<Customer[]> {
    return (await api.get<Customer[]>("/customers")).data;
}
export async function createCustomer(companyName: string, email: string | null): Promise<Customer> {
    return (await api.post<Customer>("/customers", { companyName, email })).data;
}
export async function updateCustomer(customerId: number, companyName: string, email: string | null): Promise<Customer> {
    return (await api.put<Customer>(`/customers/${customerId}`, { companyName, email })).data;
}
export async function deleteCustomer(customerId: number): Promise<void> {
    await api.delete(`/customers/${customerId}`);
}