import axios from 'axios';

/**
 * Interfaces representing your backend responses.
 */
export interface AdminConnectionResponse {
    connectionId: string;
}

export interface CreateAccountResponse {
    customerId: string;
}

// NEW: Matching the specific lookup ReadModel we added in Kotlin
export interface CustomerAccountLookupResponse {
    customerId: string;
    companyId: number;
}

export interface CustomerSessionResponse {
    sessionId: string;
    customerId: string;
    companyId: number;
    lastUpdated: string;
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
     * Resolves the companyId using the customerId.
     * Hits the new endpoint we just built in the Resource.
     */
    getCompanyForCustomer: async (customerId: string): Promise<number> => {
        console.log(`[Account Lookup] Resolving company for: ${customerId}`);
        const response = await api.get<CustomerAccountLookupResponse>(
            `/customeraccountlookup/${customerId}`
        );

        if (!response.data || !response.data.companyId) {
            throw new Error("Account not found or not registered to a company.");
        }

        console.log(`[Account Lookup] Resolved to Company ID: ${response.data.companyId}`);
        return response.data.companyId;
    },

    connectAdmin: async (adminEmail: string): Promise<AdminConnectionResponse> => {
        const response = await api.post<AdminConnectionResponse>(
            '/adminconnection',
            { adminEmail }
        );
        return response.data;
    },

    createClientAccount: async (payload: CreateClientAccountPayload): Promise<CreateAccountResponse> => {
        const response = await api.post<CreateAccountResponse>(
            '/createclientaccount',
            payload
        );
        return response.data;
    },

    lookupSession: async (customerId: string, companyId: number): Promise<CustomerSessionResponse | null> => {
        try {
            console.log(`[Lookup] Fetching: /customersessions/lookup/${customerId}/${companyId}`);
            const response = await api.get<any>(
                `/customersessions/lookup/${customerId}/${companyId}`
            );

            // UNWRAPPING: Your logs show the record is inside response.data.data
            const record = response.data?.data || response.data;

            console.log("[Lookup] Extracted Record:", record);
            return record as CustomerSessionResponse;
        } catch (error: any) {
            console.warn(`[Lookup] Failed for ${customerId}. Status: ${error.response?.status}`);
            return null;
        }
    },

    getFreshSession: async (customerId: string, companyId: number): Promise<CustomerSessionResponse> => {
        const MAX_RETRIES = 20; // Increased to 40 seconds total
        const DELAY = 2000;

        for (let i = 0; i < MAX_RETRIES; i++) {
            const record = await adminService.lookupSession(customerId, companyId);

            // Verify the specific fields are present
            if (record && record.sessionId && record.lastUpdated) {
                const dbTime = new Date(record.lastUpdated).getTime();
                const now = Date.now();
                const age = now - dbTime;

                console.log(`[Check] Attempt ${i + 1}: ID ${record.sessionId}, Age ${age}ms`);

                // If the session was updated in the last 2 minutes (generous for lag)
                if (age < 120000) {
                    return record;
                }
                console.warn("[Check] ❌ Session found but is stale. Waiting for projection...");
            } else {
                console.log(`[Check] ⏳ Attempt ${i + 1}: Record not ready...`);
            }

            await new Promise(res => setTimeout(res, DELAY));
        }

        throw new Error("Timeout: The background projection did not update in time.");
    }
};