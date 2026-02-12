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
    companyName: string;
}

export interface CompanyListLookupItem {
    id: string;
    companyId: number;
    companyName: string;
    settingsId: string;
    connectionId: string;
    timestamp: number;
}

export interface CustomerSessionResponse {
    sessionId: string;
    customerId: string;
    companyId: number;
    lastUpdated: string;
}

export interface CreateCustomerAccountPayload {
    connectionId: string;
    clientEmail: string;
    companyId: number;
    companyName: string;
}

const api = axios.create({
    baseURL: 'http://localhost:8080',
});

export const adminService = {
    /**
     * Resolves the companyId using the customerId.
     * Hits the new endpoint we just built in the Resource.
     */
    getCompanyForCustomer: async (customerId: string): Promise<{ companyId: number; companyName: string }> => {
        console.log(`[Account Lookup] Resolving company for: ${customerId}`);
        const response = await api.get<CustomerAccountLookupResponse>(
            `/customeraccountlookup/${customerId}`
        );

        if (!response.data || !response.data.companyId) {
            throw new Error("Account not found or not registered to a company.");
        }

        console.log(`[Account Lookup] Resolved to Company ID: ${response.data.companyId}, Name: ${response.data.companyName}`);
        return { companyId: response.data.companyId, companyName: response.data.companyName };
    },

    connectAdmin: async (adminEmail: string): Promise<AdminConnectionResponse> => {
        const response = await api.post<AdminConnectionResponse>(
            '/adminconnection',
            { adminEmail }
        );
        return response.data;
    },

    CreateCustomerAccount: async (payload: CreateCustomerAccountPayload): Promise<CreateAccountResponse> => {
        // 1. Log the outgoing request
        console.log('🚀 [API] Sending CreateCustomerAccount request:', {
            url: '/createcustomeraccount',
            payload: payload
        });

        // Defensive check: Ensure companyName is present before hitting the API
        if (!payload.companyName) {
            console.error('❌ [API] Aborting CreateCustomerAccount: companyName is missing/empty', payload);
            throw new Error("Validation Failed: companyName is required.");
        }

        try {
            const response = await api.post<CreateAccountResponse>(
                '/createcustomeraccount',
                payload
            );

            // 2. Log the successful response
            console.log('✅ [API] CreateCustomerAccount Success:', response.data);

            return response.data;

        } catch (error: any) {
            // 3. Log the failure (crucial for catching those 400/500 errors)
            console.error('❌ [API] CreateCustomerAccount Failed:', {
                status: error.response?.status,
                data: error.response?.data,
                message: error.message
            });
            throw error;
        }
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
    },

    requestProjectDetails: async (sessionId: string, payload: { companyId: number, customerId: string, projectId: number }): Promise<void> => {
        await api.post(`/requestprojectdetails/${sessionId}`, payload);
    },

    fetchCompanyListLookup: async (): Promise<CompanyListLookupItem[]> => {
        console.log('[Admin Service] Fetching company list lookup');
        const response = await api.get<{ companies: CompanyListLookupItem[] }>('/companylistlookup');
        return response.data.companies;
    },

    requestCompanyUpdate: async (settingsId: string, connectionId: string): Promise<void> => {
        console.log(`[Admin Service] Requesting company list update for settings: ${settingsId}`);
        await api.post(`/requestcompanylistupdate/${settingsId}?connectionId=${connectionId}`);
    }
};