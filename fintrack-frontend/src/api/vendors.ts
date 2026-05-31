import { api } from "./client";
import type { Vendor } from "./types";

export async function getVendors(): Promise<Vendor[]> {
    return (await api.get<Vendor[]>("/vendors")).data;
}
export async function createVendor(companyName: string): Promise<Vendor> {
    return (await api.post<Vendor>("/vendors", { companyName })).data;
}
export async function updateVendor(vendorId: number, companyName: string): Promise<Vendor> {
    return (await api.put<Vendor>(`/vendors/${vendorId}`, { companyName })).data;
}
export async function deleteVendor(vendorId: number): Promise<void> {
    await api.delete(`/vendors/${vendorId}`);
}