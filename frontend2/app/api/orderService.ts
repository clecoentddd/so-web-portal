import axios from 'axios';

export interface OrderInfo {
    orderId: number;
    companyId: number;
    projectId: number;
    reference: string;
    title: string;
    state: number;
    orderDate: string;
    startDate: string;
    endDate: string;
    totalExcludingTaxes: number;
    totalIncludingTaxes: number;
    totalVat: number;
}

export interface InvoiceInfo {
    invoiceId: number;
    companyId: number;
    projectId: number;
    orderId: number;
    reference: string;
    title: string;
    state: number;
    invoiceDate: string;
    dueDate?: string;
    performedDate?: string;
    totalExcludingTaxes: number;
    totalIncludingTaxes: number;
    totalVat: number;
}

export interface OrderListResponse {
    sessionId: string;
    companyId: number;
    orderList: OrderInfo[];
}

export interface InvoiceListResponse {
    sessionId: string;
    companyId: number;
    invoiceList: InvoiceInfo[];
}

export interface FetchOrdersPayload {
    sessionId: string;
    companyId: number;
    customerId: string;
    orders: []; // This seems to be unused, but keeping for consistency with original payload
}

const api = axios.create({
    // Use environment variables for API base URL
    baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080',
});

export const orderService = {
    /**
     * Query: Retrieves all orders for the session.
     */
    fetchAllOrders: async (sessionId: string): Promise<OrderInfo[]> => {
        try {
            const response = await api.get<OrderListResponse>(`/companyorderlist/${sessionId}`);
            return response.data.orderList || [];
        } catch (error: any) {
            if (error.response && error.response.status === 404) {
                return [];
            }
            throw error;
        }
    },
    /**
     * Query: Retrieves all invoices for the session.
     */
    fetchAllInvoices: async (sessionId: string): Promise<InvoiceInfo[]> => {
        try {
            const response = await api.get<InvoiceListResponse>(`/companyinvoices/${sessionId}`);
            return response.data.invoiceList || [];
        } catch (error: any) {
            if (error.response && error.response.status === 404) {
                return [];
            }
            throw error;
        }
    },
    /**
     * Helper: Triggers the fetch process from Boond (or marks them fetched).
     */
    fetchOrders: async (sessionId: string, payload: FetchOrdersPayload): Promise<void> => {
        await api.post(`/fetchorders/${sessionId}`, payload);
    }
};
