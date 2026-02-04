import { useState } from 'react';
// Note the 'type' keyword added for the interfaces
import {
  adminService,
  type CreateClientAccountPayload,
  type AdminConnectionResponse,
  type CreateAccountResponse
} from '../api/adminService';

export interface CompanyInfo {
  companyId: number;   // Maps to Kotlin Long
  companyName: string; // Maps to Kotlin String
}

// Optional: If your API returns the full object we saw earlier
export interface ListOfCompaniesFetchedResponse {
  connectionId: string;
  adminEmail: string;
  listOfCompanies: CompanyInfo[];
}

export const useAdmin = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [companies, setCompanies] = useState<CompanyInfo[]>([]);

  /**
   * Connects the admin and returns a Promise with the connectionId
   */
  const connect = async (email: string): Promise<AdminConnectionResponse> => {
    setLoading(true);
    setError(null);
    try {
      const res = await adminService.connectAdmin(email);
      setLoading(false);
      return res;
    } catch (err: any) {
      const errorMessage = err.response?.data?.error || 'Connection failed';
      setError(errorMessage);
      setLoading(false);
      throw err;
    }
  };

  /**
   * Creates a client account using the connectionId, email, and companyId
   */
  const createAccount = async (data: CreateClientAccountPayload): Promise<CreateAccountResponse> => {
    setLoading(true);
    setError(null);
    try {
      const res = await adminService.createClientAccount(data);
      setLoading(false);
      return res;
    } catch (err: any) {
      const errorMessage = err.response?.data?.error || 'Account creation failed';
      setError(errorMessage);
      setLoading(false);
      throw err;
    }
  };

  const fetchCompanies = async (connectionId: string, retryCount = 0) => {
    console.log(`[FetchCompanies] Attempt ${retryCount + 1} for connectionId: ${connectionId}`);
    setLoading(true);

    try {
      const response = await fetch(`http://localhost:8080/listofcompanies/${connectionId}`);

      if (!response.ok) {
        console.error(`[FetchCompanies] HTTP Error: ${response.status} ${response.statusText}`);
        throw new Error(`Server returned ${response.status}`);
      }

      const data = await response.json();
      console.log("[FetchCompanies] Data received from server:", data);

      const hasCompanies = data.listOfCompanies && data.listOfCompanies.length > 0;

      if (!hasCompanies && retryCount < 2) {
        console.warn(`[FetchCompanies] List is empty. Retrying in 1s... (${retryCount + 1}/2)`);
        setTimeout(() => fetchCompanies(connectionId, retryCount + 1), 1000);
        return;
      }

      if (!hasCompanies) {
        console.error("[FetchCompanies] Maximum retries reached. No companies found in projection.");
      } else {
        console.log(`[FetchCompanies] Successfully loaded ${data.listOfCompanies.length} companies.`);
      }

      setCompanies(data.listOfCompanies || []);
      setError(null);
    } catch (err: any) {
      console.error("[FetchCompanies] Fetch operation failed:", err.message);
      setError("Failed to load companies");
    } finally {
      // Only set loading to false if we aren't planning another retry
      if (retryCount >= 2 || (companies && companies.length > 0)) {
        setLoading(false);
      }
    }
  };

  return { connect, createAccount, loading, error, fetchCompanies, companies };
};