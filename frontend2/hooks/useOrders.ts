import { useState } from 'react';
import { orderService, type OrderInfo } from '../app/api/orderService';

export const useOrders = () => {
    const [orders, setOrders] = useState<OrderInfo[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const getProjectOrders = async (sessionId: string, projectId: number, companyId: number, customerId: string) => {
        setLoading(true);
        setError(null);
        setOrders([]);

        try {
            // Step 1: Query the Read Model
            console.log(`[useOrders] Querying Read Model for Project ${projectId}...`);
            let data = await orderService.getOrders(sessionId, projectId, companyId);

            if (data && data.length > 0) {
                console.log(`[useOrders] Data found in Read Model.`);
                setOrders(data);
                return;
            }

            // Step 2: If Data is missing (null or empty), Trigger Command
            console.log(`[useOrders] Data not found. Triggering Fetch Command...`);
            await orderService.fetchOrders(sessionId, {
                sessionId,
                companyId,
                customerId,
                orders: []
            });

            // Step 3: Poll for data
            console.log(`[useOrders] Command sent. Polling for results...`);

            // Simple polling logic: Try 3 times with 2s delay
            const MAX_RETRIES = 5;
            const DELAY_MS = 2000;

            for (let i = 0; i < MAX_RETRIES; i++) {
                await new Promise(resolve => setTimeout(resolve, DELAY_MS));

                console.log(`[useOrders] Polling attempt ${i + 1}...`);
                data = await orderService.getOrders(sessionId, projectId, companyId);

                if (data && data.length > 0) {
                    console.log(`[useOrders] Data retrieved after polling.`);
                    setOrders(data);
                    return;
                }
            }

            // If we reach here, we didn't get data in time. 
            // Depending on requirements, we can show an empty list or an error, 
            // or just leave it empty if there really are no orders.
            console.log(`[useOrders] Polling finished. No orders found (or timeout).`);
            setOrders([]);

        } catch (err: any) {
            console.error(`[useOrders] Error:`, err);
            setError("Failed to load orders. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return {
        orders,
        loading,
        error,
        getProjectOrders
    };
};
