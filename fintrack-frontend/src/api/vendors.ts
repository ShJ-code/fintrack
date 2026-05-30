import { api } from "./client";
import type { Vendor } from "./types";

export async function getVendors(): Promise<Vendor[]> {
    const res = await api.get<Vendor[]>("/vendors");
    return res.data;
}