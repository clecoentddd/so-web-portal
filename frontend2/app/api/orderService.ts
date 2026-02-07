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

export interface ReadModelResponse {
    sessionId: string;
    companyId: number;
    orderList: OrderInfo[];
}

const api = axios.create({
    baseURL: 'http://localhost:8080',
});

export const orderService = {
    /**
     * Query: Checks the Read Model for existing orders.
     */
    getOrders: async (sessionId: string, projectId: number, companyId: number): Promise<OrderInfo[] | null> => {
        try {
            const response = await api.get<ReadModelResponse>(
                `/companyorderlist/${sessionId}/project/${projectId}?companyId=${companyId}`
            );
            return response.data.orderList;
        } catch (error: any) {
            if (error.response && error.response.status === 404) {
                return null;
            }
            throw error;
        }
    }
};
