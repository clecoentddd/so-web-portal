"use client"

import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog"
import { Badge } from "@/components/ui/badge"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Loader2, FileText, Calendar, Euro, AlertCircle } from "lucide-react" // assuming Euro icon exists or use generic
import { OrderInfo } from "@/app/api/orderService"

interface OrdersModalProps {
    isOpen: boolean
    onClose: () => void
    loading: boolean
    error: string | null
    projectTitle: string
    orders: OrderInfo[]
}

export function OrdersModal({
    isOpen,
    onClose,
    loading,
    error,
    projectTitle,
    orders,
}: OrdersModalProps) {

    // Format currency
    const formatCurrency = (amount: number) => {
        return new Intl.NumberFormat('fr-FR', {
            style: 'currency',
            currency: 'EUR',
        }).format(amount);
    }

    // Helper for state color/label
    // Boond often uses integers for states. Mapping needs to be known. 
    // For now, displaying raw state or a generic badge.
    const getStateLabel = (state: number) => {
        // Example mapping - Adjust based on actual Boond states if known
        switch (state) {
            case 0: return "Open";
            case 1: return "Signed";
            case 2: return "Closed";
            default: return `State ${state}`;
        }
    }

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="max-w-3xl max-h-[85vh] flex flex-col">
                <DialogHeader>
                    <DialogTitle className="text-xl flex items-center gap-2">
                        <FileText className="h-5 w-5 text-[#FBBB10]" />
                        Orders for <span className="text-[#FBBB10]">{projectTitle}</span>
                    </DialogTitle>
                    <DialogDescription>
                        View all orders associated with this project.
                    </DialogDescription>
                </DialogHeader>

                <div className="flex-1 overflow-hidden mt-2">
                    {loading && orders.length === 0 ? (
                        <div className="flex flex-col items-center justify-center h-40 space-y-3">
                            <Loader2 className="h-8 w-8 animate-spin text-[#FBBB10]" />
                            <p className="text-sm text-muted-foreground animate-pulse">
                                Retrieving orders from BoondManager...
                            </p>
                        </div>
                    ) : error ? (
                        <div className="flex flex-col items-center justify-center h-40 space-y-3 text-red-500">
                            <AlertCircle className="h-8 w-8" />
                            <p className="text-sm">{error}</p>
                        </div>
                    ) : orders.length === 0 ? (
                        <div className="flex flex-col items-center justify-center h-40 space-y-3 text-muted-foreground">
                            <FileText className="h-8 w-8 opacity-20" />
                            <p className="text-sm">No orders found for this project.</p>
                        </div>
                    ) : (
                        <ScrollArea className="h-[50vh] pr-4">
                            <div className="space-y-3">
                                {orders.map((order) => (
                                    <div
                                        key={order.orderId}
                                        className="flex flex-col sm:flex-row gap-4 p-4 border border-border rounded-lg bg-card/50 hover:bg-accent/5 transition-colors"
                                    >
                                        <div className="flex-1 space-y-2">
                                            <div className="flex items-center justify-between">
                                                <div className="flex items-center gap-2">
                                                    <span className="font-mono text-xs text-muted-foreground bg-muted px-2 py-0.5 rounded">
                                                        {order.reference}
                                                    </span>
                                                    <h4 className="font-medium text-sm">{order.title}</h4>
                                                </div>
                                                <Badge variant="outline" className="text-xs">
                                                    {getStateLabel(order.state)}
                                                </Badge>
                                            </div>

                                            <div className="grid grid-cols-2 gap-x-8 gap-y-2 text-sm text-muted-foreground mt-2">
                                                <div className="flex items-center gap-2">
                                                    <Calendar className="h-3.5 w-3.5" />
                                                    <span className="text-xs">
                                                        {order.startDate} — {order.endDate}
                                                    </span>
                                                </div>
                                                <div className="flex items-center gap-2">
                                                    <span className="text-xs">Ordered: {order.orderDate}</span>
                                                </div>
                                            </div>
                                        </div>

                                        <div className="flex flex-col items-end justify-center min-w-[120px] border-t sm:border-t-0 sm:border-l border-border pt-3 sm:pt-0 sm:pl-4 mt-3 sm:mt-0">
                                            <span className="text-[10px] uppercase text-muted-foreground">Total excl. Tax</span>
                                            <span className="text-lg font-bold text-[#FBBB10]">
                                                {formatCurrency(order.totalExcludingTaxes)}
                                            </span>
                                            <div className="flex gap-3 mt-1 text-[10px] text-muted-foreground">
                                                <span>VAT: {formatCurrency(order.totalVat)}</span>
                                                <span>Inc: {formatCurrency(order.totalIncludingTaxes)}</span>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </ScrollArea>
                    )}
                </div>
            </DialogContent>
        </Dialog>
    )
}
