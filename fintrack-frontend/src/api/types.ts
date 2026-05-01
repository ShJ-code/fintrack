export interface User {
    id: number;
    username: string;
    email: string;
    address?: string;
}

export interface Vendor {
    vendorId: number;
    userId: number;
    companyName: string;
}

export interface Bill {
    billId: number;
    vendorId: number;
    vendorName?: string;
    amount: number;
    dueDate: string;
    status: string;
    createdAt?: string;
}

export interface Customer {
    customerId: number;
    userId: number;
    companyName: string;
    email?: string;
}