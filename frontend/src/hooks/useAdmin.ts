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

export interface ProjectInfo {
  projectId: number;
  projectName: string;
  projectDescription: string;
}

export const useAdmin = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [companies, setCompanies] = useState<CompanyInfo[]>([]);
  const [projects, setProjects] = useState<ProjectInfo[]>([]);

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
   * CUSTOMER: The "Waterfall" discovery flow
   * Now includes automatic company resolution.
   */
  const customerConnect = async (customerId: string, email: string) => {
    setLoading(true);
    setError(null);
    try {
      // 1. Resolve Company ID first
      const companyId = await adminService.getCompanyForCustomer(customerId);

      // 2. Fire the connection command
      const authResponse = await fetch(`http://localhost:8080/clientaccountconnection/${customerId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ clientEmail: email }),
      });

      if (!authResponse.ok) {
        const errorText = await authResponse.text();
        throw new Error(`Connection failed: ${authResponse.status} ${errorText}`);
      }

      // Return the resolved companyId so App.tsx knows what to poll with
      return { success: true, companyId };
    } catch (err: any) {
      console.error("[Hook Log] Customer Connect Error:", err);
      setError(err.message || "Failed to resolve account or connect.");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  /**
   * FETCH PROJECTS: Gets the projects from the JPA projection
   */
  const fetchProjects = async (sessionId: string) => {
    setLoading(true);
    console.log(`[Hook Log] Requesting: http://localhost:8080/companyprojectlist/${sessionId}`);
    try {
      const response = await fetch(`http://localhost:8080/companyprojectlist/${sessionId}`);
      if (!response.ok) throw new Error(`Server returned ${response.status}`);

      const data = await response.json();
      console.log("[Hook Log] Received Projection Data:", data);

      setProjects(data.projectList || []);
    } catch (err: any) {
      console.error("[Hook Log] Fetch Error:", err);
      setError("Failed to load project list.");
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
    customerConnect,
    createAccount,
    fetchCompanies,
    fetchProjects,
    companies,
    projects,
    loading,
    error
  };
};