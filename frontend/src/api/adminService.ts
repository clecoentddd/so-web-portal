import axios from 'axios';

/**
 * Interfaces representing your backend responses.
 * These match the OpenAPI schema you provided.
 */
export interface AdminConnectionResponse {
    connectionId: string;
}

export interface CreateAccountResponse {
    customerId: string;
}

export interface CreateClientAccountPayload {
    connectionId: string;
    clientEmail: string;
    companyId: number;
}

const api = axios.create({
    baseURL: 'http://localhost:8080',
});

export const adminService = {
    /**
     * Sends the ToConnectCommand to the backend.
     * @param adminEmail - The email of the admin trying to connect.
     */
    connectAdmin: async (adminEmail: string): Promise<AdminConnectionResponse> => {
        const response = await api.post<AdminConnectionResponse>(
            '/adminconnection',
            { adminEmail }
        );
        return response.data;
    },

    /**
     * Sends the CreateAccountCommand to the backend.
     * @param payload - Object containing connectionId, clientEmail, and companyId.
     */
    createClientAccount: async (payload: CreateClientAccountPayload): Promise<CreateAccountResponse> => {
        const response = await api.post<CreateAccountResponse>(
            '/createclientaccount',
            payload
        );
        return response.data;
    }
};