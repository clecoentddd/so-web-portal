import { useState } from 'react';
import {
  adminService,
  type CreateClientAccountPayload,
  type AdminConnectionResponse,
  type CreateAccountResponse
} from '../api/adminService';

export interface CompanyInfo {
  companyId: number;
  companyName: string;
}

export const useAdmin = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [companies, setCompanies] = useState<CompanyInfo[]>([]);

  /**
   * ADMIN: Connects and returns connectionId
   */
  const connect = async (email: string): Promise<AdminConnectionResponse> => {
    setLoading(true);
    setError(null);
    try {
      const res = await adminService.connectAdmin(email);
      return res;
    } catch (err: any) {
      const errorMessage = err.response?.data?.error || 'Connection failed';
      setError(errorMessage);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  /**
   * CUSTOMER: Connects using UUID and Email
   * Matches your Backend: @PostMapping("/clientaccountconnection/{id}")
   */
  const customerConnect = async (customerId: string, clientEmail: string): Promise<void> => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`http://localhost:8080/clientaccountconnection/${customerId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ clientEmail })
      });

      if (!response.ok) {
        // This handles your CommandException from the Aggregate
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || 'Access Denied: Invalid credentials');
      }

      // Success (200 OK with empty body)
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  /**
   * ADMIN: Creates a client account
   */
  const createAccount = async (data: CreateClientAccountPayload): Promise<CreateAccountResponse> => {
    setLoading(true);
    setError(null);
    try {
      const res = await adminService.createClientAccount(data);
      return res;
    } catch (err: any) {
      const errorMessage = err.response?.data?.error || 'Account creation failed';
      setError(errorMessage);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  /**
   * FETCH: Gets companies for the admin dropdown
   */
  const fetchCompanies = async (connectionId: string, retryCount = 0) => {
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/listofcompanies/${connectionId}`);
      if (!response.ok) throw new Error(`Server returned ${response.status}`);

      const data = await response.json();
      const hasCompanies = data.listOfCompanies && data.listOfCompanies.length > 0;

      if (!hasCompanies && retryCount < 2) {
        setTimeout(() => fetchCompanies(connectionId, retryCount + 1), 1000);
        return;
      }

      setCompanies(data.listOfCompanies || []);
      setError(null);
    } catch (err: any) {
      setError("Failed to load companies");
    } finally {
      setLoading(false);
    }
  };

  return {
    connect,
    customerConnect, // New function exported
    createAccount,
    fetchCompanies,
    companies,
    loading,
    error
  };
};