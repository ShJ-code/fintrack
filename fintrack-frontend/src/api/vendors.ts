import { api } from "./client";
import type { Vendor } from "./types";

export async function getVendors(userId: number): Promise<Vendor[]> {
    const res = await api.get<Vendor[]>("/vendors", { params: { userId } });
    return res.data;
}