import { useState } from 'react';
// Note the 'type' keyword added for the interfaces
import {
  adminService,
  type CreateClientAccountPayload,
  type AdminConnectionResponse,
  type CreateAccountResponse
} from '../api/adminService';

export const useAdmin = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

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

  return { connect, createAccount, loading, error };
};