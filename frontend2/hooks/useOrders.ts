import { useState } from 'react';
import { orderService, type OrderInfo, type InvoiceInfo, type FetchOrdersPayload } from '../app/api/orderService';

export const useOrders = () => {
    const [allOrders, setAllOrders] = useState<OrderInfo[]>([]);
    const [allInvoices, setAllInvoices] = useState<InvoiceInfo[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchSessionData = async (sessionId: string, companyId: number, customerId: string) => {
        setLoading(true);
        setError(null);
        setAllOrders([]);
        setAllInvoices([]);

        try {
            // Step 1: Initial Query to check if data exists
            console.log(`[useOrders] Checking existing data for session ${sessionId}...`);
            const initialOrders = await orderService.fetchAllOrders(sessionId);
            const initialInvoices = await orderService.fetchAllInvoices(sessionId);

            if (initialOrders.length > 0 || initialInvoices.length > 0) {
                console.log(`[useOrders] Data found immediately.`);
                setAllOrders(initialOrders);
                setAllInvoices(initialInvoices);
                setLoading(false); // Show data immediately
                // Even if found, we might want to continue polling if the process is ongoing,
                // but for now, if data is missing, we definitely trigger the command.
            }

            if (initialOrders.length === 0 && initialInvoices.length === 0) {
                // Step 2: If Data is missing, Trigger Command to start backend fetch
                console.log(`[useOrders] No data found. Triggering Fetch Command...`);
                const payload: FetchOrdersPayload = {
                    sessionId,
                    companyId,
                    customerId,
                    orders: []
                };
                await orderService.fetchOrders(sessionId, payload);
            }

            // Step 3: Poll for data in a loop
            console.log(`[useOrders] Starting 3-second polling loop...`);

            // We loop for a fixed number of attempts to simulate "while user is active" 
            // without creating an infinite loop that can't be stopped easily in this function scope.
            // 20 attempts * 3 seconds = 60 seconds of active polling.
            const MAX_POLLS = 20;
            const POLL_INTERVAL_MS = 3000;

            for (let i = 0; i < MAX_POLLS; i++) {
                await new Promise(resolve => setTimeout(resolve, POLL_INTERVAL_MS));

                console.log(`[useOrders] Polling attempt ${i + 1}/${MAX_POLLS}...`);
                const orders = await orderService.fetchAllOrders(sessionId);
                const invoices = await orderService.fetchAllInvoices(sessionId);

                setAllOrders(orders);
                setAllInvoices(invoices);

                // Stop loading spinner after first poll so user sees the data (or empty state)
                setLoading(false);
            }

        } catch (err: any) {
            console.error(`[useOrders] Error:`, err);
            setError(err.response?.data?.message || "An unexpected error occurred while fetching orders.");
        } finally {
            setLoading(false);
        }
    };

    return {
        allOrders,
        allInvoices,
        loading,
        error,
        fetchSessionData
    };
};
