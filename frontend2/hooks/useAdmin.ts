import { useState } from 'react';
import {
  adminService,
  type CreateCustomerAccountPayload,
  type AdminConnectionResponse,
  type CreateAccountResponse
} from '../app/api/adminService';

// WAIT: I noticed the import path in the previous view_file was '../app/api/adminService'
// Let me correct that to the actual path I saw in view_file.

export interface CompanyInfo {
  companyId: number;
  companyName: string;
}

export interface CustomerAccount {
  customerId: string;
  clientEmail: string;
  companyId: number;
  companyName: string;
}

export interface ProjectInfo {
  projectId: number;
  reference: string;
  projectTitle: string;
  projectDescription: string;
  startDate?: string;
  endDate?: string;
  forecastEndDate?: string;
  status: string;
  manager: string;
}

export const useAdmin = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [companies, setCompanies] = useState<CompanyInfo[]>([]);
  const [customerAccounts, setCustomerAccounts] = useState<CustomerAccount[]>([]);
  const [projects, setProjects] = useState<ProjectInfo[]>([]);
  const [companyName, setCompanyName] = useState<string>("");

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
   * Helper to resolve company details by customer ID
   */
  const resolveCompany = async (customerId: string) => {
    try {
      const response = await fetch(`http://localhost:8080/customeraccountlookup/${customerId}`);
      if (!response.ok) throw new Error(`Account lookup failed: ${response.status}`);
      const accountData = await response.json();

      const companyResponse = await fetch(`http://localhost:8080/companylistlookup/${accountData.companyId}`);
      if (!companyResponse.ok) throw new Error(`Company lookup failed: ${companyResponse.status}`);
      const companyData = await companyResponse.json();

      setCompanyName(companyData.companyName);
      return { companyId: accountData.companyId, companyName: companyData.companyName };
    } catch (lookupErr: any) {
      console.error("[useAdmin] Lookup failed.", lookupErr);
      throw lookupErr;
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
      console.log(`[useAdmin] Step 1: Connecting to account...`);
      // 1. Fire the connection command
      const authResponse = await fetch(`http://localhost:8080/customeraccountconnection/${customerId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ clientEmail: email }),
      });

      if (!authResponse.ok) {
        const errorText = await authResponse.text();
        throw new Error(`Connection failed: ${authResponse.status} ${errorText}`);
      }

      console.log(`[useAdmin] Step 2: Resolving Company ID for customer ${customerId}...`);
      // 2. Resolve Company ID and Name via customeraccountlookup API
      try {
        const { companyId, companyName } = await resolveCompany(customerId);
        console.log(`[useAdmin] Resolved Company ID: ${companyId}, Name: ${companyName}`);

        // 3. Discovery Step: Wait for the session to be created/updated
        console.log(`[useAdmin] Step 3: Waiting for session projection...`);
        const session = await adminService.getFreshSession(customerId, companyId);

        return { success: true, companyId, companyName, sessionId: session.sessionId };
      } catch (err: any) {
        console.error("[useAdmin] Lookup or Session failed.", err);
        throw new Error("Could not find customer account details.");
      }
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
    if (!sessionId) {
      console.error("[Hook Log] Cannot fetch projects: sessionId is missing.");
      setError("Active session not found. Please log in again.");
      return;
    }

    setLoading(true);
    const url = `http://localhost:8080/companyprojectlist/${sessionId}`;
    console.log(`[Hook Log] Requesting: ${url}`);

    try {
      const response = await fetch(url);

      if (!response.ok) {
        // This will catch the 404 if the sessionId is invalid
        throw new Error(`Server returned ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      console.log("[Hook Log] Received Projection Data:", data);

      // Ensure we handle different naming conventions from backend
      setProjects(data.projectList || data.projects || []);
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
  const createAccount = async (data: CreateCustomerAccountPayload): Promise<CreateAccountResponse> => {
    setLoading(true);
    setError(null);

    // Ensure companyName is present by falling back to the hook state if missing
    const payload = {
      ...data,
      companyName: data.companyName || companyName
    };

    try {
      const res = await adminService.CreateCustomerAccount(payload);
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
   * FETCH: Gets the list of customer accounts
   */
  const fetchCustomerAccounts = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/customeraccountlist');
      if (!response.ok) throw new Error(`Server returned ${response.status}`);

      const json = await response.json();
      setCustomerAccounts(json.data || []);
      setError(null);
    } catch (err: any) {
      setError("Failed to load customer accounts");
    } finally {
      setLoading(false);
    }
  };

  /**
   * FETCH: Gets companies for the admin dropdown
   */
  const fetchCompanies = async (connectionId: string) => {
    setLoading(true);
    setError(null);
    try {
      console.log('[useAdmin] Fetching companies via lookup service');
      const data = await adminService.fetchCompanyListLookup();
      setCompanies(data);
      return data;
    } catch (err: any) {
      console.error('[useAdmin] Failed to fetch companies:', err);
      setError(err.message || 'Failed to fetch companies');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const requestCompaniesUpdate = async (settingsId: string, connectionId: string) => {
    setLoading(true);
    setError(null);
    try {
      await adminService.requestCompanyUpdate(settingsId, connectionId);
      console.log('[useAdmin] Company list update requested');
    } catch (err: any) {
      console.error('[useAdmin] Failed to request company update:', err);
      setError(err.message || 'Failed to request company update');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  /**
   * REQUEST PROJECT DETAILS: Triggers the request to the backend
   */
  const requestProjectDetails = async (sessionId: string, companyId: number, customerId: string, projectId: number) => {
    setLoading(true);
    setError(null);
    try {
      await adminService.requestProjectDetails(sessionId, { companyId, customerId, projectId });
    } catch (err: any) {
      console.error("[Hook Log] Request Details Error:", err);
      setError("Failed to request project details.");
    } finally {
      setLoading(false);
    }
  };

  return {
    connect,
    customerConnect,
    createAccount,
    fetchCompanies,
    requestCompaniesUpdate,
    fetchProjects,
    fetchCustomerAccounts,
    requestProjectDetails,
    companies,
    projects,
    customerAccounts,
    companyName,
    loading,
    error
  };
};