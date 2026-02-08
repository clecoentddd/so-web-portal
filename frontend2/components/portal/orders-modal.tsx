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
import { Loader2, FileText, Calendar, AlertCircle, Receipt, Clock } from "lucide-react"
import { OrderInfo, InvoiceInfo } from "@/app/api/orderService"

interface OrdersModalProps {
    isOpen: boolean
    onClose: () => void
    loading: boolean
    error: string | null
    projectTitle: string
    manager?: string
    companyName?: string
    orders: OrderInfo[]
    invoices: InvoiceInfo[]
}

export function OrdersModal({
    isOpen,
    onClose,
    loading,
    error,
    projectTitle,
    manager,
    companyName,
    orders,
    invoices,
}: OrdersModalProps) {

    const formatCurrency = (amount: number) => {
        return new Intl.NumberFormat('fr-CH', {
            style: 'currency',
            currency: 'CHF',
        }).format(amount);
    }

    const getStateLabel = (state: number) => {
        switch (state) {
            case 0: return "Open";
            case 1: return "Signed";
            case 2: return "Closed";
            default: return `State ${state}`;
        }
    }

    const getInvoiceStateLabel = (state: number) => {
        switch (state) {
            case 1: return "Issued";
            case 2: return "Paid";
            default: return `State ${state}`;
        }
    }

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="max-w-5xl h-[90vh] flex flex-col bg-[#F8F9FA]">
                <DialogHeader className="border-b border-slate-200 pb-4 flex flex-row items-start justify-between space-y-0">
                    <div className="space-y-1">
                        <DialogTitle className="text-2xl flex items-center gap-2 font-bold text-slate-900">
                            <FileText className="h-6 w-6 text-[#FBBB10]" />
                            Project <span className="text-[#FBBB10]">{projectTitle}</span>
                        </DialogTitle>
                        <DialogDescription className="text-base text-slate-600">
                            Detailed breakdown of orders and financial tracking.
                        </DialogDescription>
                    </div>
                    <div className="flex flex-col gap-2 text-right pl-4 mr-8">
                        {companyName && (
                            <div>
                                <p className="text-[10px] uppercase tracking-wider text-slate-400 font-bold">Company</p>
                                <p className="text-sm font-semibold text-slate-700">{companyName}</p>
                            </div>
                        )}
                        {manager && (
                            <div>
                                <p className="text-[10px] uppercase tracking-wider text-slate-400 font-bold">Manager</p>
                                <p className="text-sm font-semibold text-slate-700">{manager}</p>
                            </div>
                        )}
                    </div>
                </DialogHeader>

                <div className="flex-1 overflow-hidden mt-4">
                    {loading && orders.length === 0 ? (
                        <div className="flex flex-col items-center justify-center h-full space-y-4">
                            <Loader2 className="h-10 w-10 animate-spin text-[#FBBB10]" />
                            <p className="text-slate-500 animate-pulse font-medium">Synchronizing with BoondManager...</p>
                        </div>
                    ) : error ? (
                        <div className="flex flex-col items-center justify-center h-full space-y-3 text-red-500">
                            <AlertCircle className="h-10 w-10" />
                            <p className="font-semibold">{error}</p>
                        </div>
                    ) : (
                        <ScrollArea className="h-full pr-4">
                            <div className="space-y-8 pb-10">
                                {orders.map((order) => {
                                    const orderInvoices = invoices.filter(i => i.orderId === order.orderId);

                                    return (
                                        <div
                                            key={order.orderId}
                                            className="bg-white border border-slate-200 rounded-xl shadow-sm overflow-hidden"
                                        >
                                            {/* ORDER SECTION */}
                                            <div className="p-6">
                                                <div className="flex flex-col lg:flex-row justify-between gap-6">
                                                    <div className="space-y-4 flex-1">
                                                        <div className="flex items-center gap-3 flex-wrap">
                                                            {/* FIXED: White text on dark background */}
                                                            <Badge className="bg-slate-800 hover:bg-slate-900 text-white font-mono px-2.5 py-1 text-xs">
                                                                {order.reference}
                                                            </Badge>
                                                            <h4 className="text-xl font-bold text-slate-900">{order.title}</h4>
                                                            <Badge variant="outline" className="border-[#FBBB10] text-[#FBBB10] font-semibold bg-[#FBBB10]/5">
                                                                {getStateLabel(order.state)}
                                                            </Badge>
                                                        </div>

                                                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm text-slate-500">
                                                            <div className="flex items-center gap-2">
                                                                <Calendar className="h-4 w-4 text-slate-400" />
                                                                <span className="font-medium">Period:</span> {order.startDate} — {order.endDate}
                                                            </div>
                                                            <div className="flex items-center gap-2">
                                                                <Clock className="h-4 w-4 text-slate-400" />
                                                                <span className="font-medium">Date:</span> {order.orderDate}
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div className="flex flex-col items-end justify-center px-8 py-4 bg-slate-50 rounded-xl border border-slate-100 min-w-[240px]">
                                                        <span className="text-[10px] uppercase font-bold text-slate-400 tracking-widest mb-1">Order Total (Excl. VAT)</span>
                                                        <span className="text-3xl font-black text-slate-900 leading-none">
                                                            {formatCurrency(order.totalExcludingTaxes)}
                                                        </span>
                                                        <div className="flex gap-4 mt-3 text-xs font-semibold text-slate-500">
                                                            <span>VAT: {formatCurrency(order.totalVat)}</span>
                                                            <span className="text-slate-900">Inc: {formatCurrency(order.totalIncludingTaxes)}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            {/* INVOICES SECTION (Off-white distinct area) */}
                                            <div className="bg-[#FAF9F6] border-t border-slate-100 p-6">
                                                <div className="flex items-center gap-2 mb-4">
                                                    <Receipt className="h-4 w-4 text-slate-400" />
                                                    <h5 className="text-xs font-black uppercase tracking-widest text-slate-400">Associated Invoices</h5>
                                                </div>

                                                {orderInvoices.length > 0 ? (
                                                    <div className="space-y-3">
                                                        {orderInvoices.map(invoice => (
                                                            <div
                                                                key={invoice.invoiceId}
                                                                className="flex flex-col md:flex-row md:items-center justify-between p-4 rounded-lg border border-slate-200 bg-white shadow-sm hover:border-slate-300 transition-all"
                                                            >
                                                                <div className="flex-1">
                                                                    <div className="flex items-center gap-3 mb-1">
                                                                        <span className="text-xs font-bold text-slate-900 bg-slate-100 px-2 py-0.5 rounded">
                                                                            {invoice.reference}
                                                                        </span>
                                                                        <span className="text-sm font-semibold text-slate-800">{invoice.title}</span>
                                                                    </div>
                                                                    <div className="flex gap-4 text-[11px] text-slate-500">
                                                                        <span><strong>Issued:</strong> {invoice.invoiceDate}</span>
                                                                        <span><strong>Performed:</strong> {invoice.performedDate || "-"}</span>
                                                                        <span><strong>Due:</strong> {invoice.dueDate}</span>
                                                                    </div>
                                                                </div>

                                                                <div className="flex items-center gap-6 mt-4 md:mt-0">
                                                                    <div className="text-right">
                                                                        <p className="text-[10px] text-slate-400 uppercase font-bold">Inc. VAT</p>
                                                                        <p className="font-bold text-slate-900">{formatCurrency(invoice.totalIncludingTaxes)}</p>
                                                                    </div>
                                                                    <Badge className={
                                                                        invoice.state === 2
                                                                            ? "bg-emerald-500 hover:bg-emerald-600 text-white border-none"
                                                                            : "bg-amber-500 hover:bg-amber-600 text-white border-none"
                                                                    }>
                                                                        {getInvoiceStateLabel(invoice.state)}
                                                                    </Badge>
                                                                </div>
                                                            </div>
                                                        ))}
                                                    </div>
                                                ) : (
                                                    <div className="text-center py-8 border-2 border-dashed border-slate-200 rounded-xl">
                                                        <p className="text-sm text-slate-400 font-medium">
                                                            {loading ? "Refreshing invoice data..." : "No invoices registered for this order."}
                                                        </p>
                                                    </div>
                                                )}
                                            </div>
                                        </div>
                                    )
                                })}
                            </div>
                        </ScrollArea>
                    )}
                </div>
            </DialogContent>
        </Dialog>
    )
}