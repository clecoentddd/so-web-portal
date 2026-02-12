"use client"

import { useState, useEffect } from "react"
import { ChevronLeft, RefreshCw } from "lucide-react"

interface InvoiceStateMapping {
    id: string
    settingsId: string
    code: number
    label: string
    connectionId: string
    timestamp: number
}

interface ViewInvoiceMappingProps {
    connectionId: string
    onBack: () => void
}

export function ViewInvoiceMapping({ connectionId, onBack }: ViewInvoiceMappingProps) {
    const [invoiceMappings, setInvoiceMappings] = useState<InvoiceStateMapping[]>([])
    const [isLoading, setIsLoading] = useState(false)
    const [lastUpdated, setLastUpdated] = useState<number | null>(null)

    const requestInvoiceMappingUpdate = async () => {
        setIsLoading(true)
        try {
            const response = await fetch(
                `http://localhost:8080/requestinvoicestatemappingupdate/${connectionId}`,
                {
                    method: 'POST',
                    headers: {
                        'accept': '*/*',
                    },
                    body: '',
                }
            )

            if (response.ok) {
                console.log('[ViewInvoiceMapping] Invoice state mapping update requested')
                // Wait a moment then fetch results
                setTimeout(() => {
                    fetchInvoiceMapping()
                }, 1000)
            } else {
                console.error('[ViewInvoiceMapping] Failed to request invoice mapping update')
                setIsLoading(false)
            }
        } catch (error) {
            console.error('[ViewInvoiceMapping] Error requesting invoice mapping update:', error)
            setIsLoading(false)
        }
    }

    const fetchInvoiceMapping = async () => {
        try {
            const response = await fetch('http://localhost:8080/invoicestatemapping', {
                method: 'GET',
                headers: {
                    'accept': '*/*',
                },
            })

            if (response.ok) {
                const data: InvoiceStateMapping[] = await response.json()
                setInvoiceMappings(data)

                // Set last updated timestamp from the first item (they all have the same timestamp)
                if (data.length > 0) {
                    setLastUpdated(data[0].timestamp)
                }
            }
        } catch (error) {
            console.error('[ViewInvoiceMapping] Error fetching invoice mapping:', error)
        } finally {
            setIsLoading(false)
        }
    }

    // Initial fetch on mount
    useEffect(() => {
        fetchInvoiceMapping()
    }, [])

    const formatTimestamp = (timestamp: number) => {
        return new Date(timestamp).toLocaleString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        })
    }

    return (
        <div className="py-4">
            <button
                type="button"
                onClick={onBack}
                className="mb-6 flex items-center gap-2 text-xs uppercase tracking-wider text-muted-foreground transition-colors hover:text-[#FBBB10]"
            >
                <ChevronLeft className="h-3.5 w-3.5" />
                Back to Dashboard
            </button>

            <div className="mb-6">
                <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">
                    Configuration
                </p>
                <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
                    Invoice State Mapping
                </h2>
            </div>

            {/* Request Update button */}
            <div className="mb-6">
                <button
                    type="button"
                    onClick={requestInvoiceMappingUpdate}
                    disabled={isLoading}
                    className="group flex items-center gap-2 border border-border bg-secondary/50 px-4 py-2 transition-all duration-200 hover:border-[#FBBB10] hover:bg-secondary disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    <RefreshCw className={`h-4 w-4 text-muted-foreground transition-colors group-hover:text-[#FBBB10] ${isLoading ? 'animate-spin' : ''}`} />
                    <span className="text-sm text-foreground">
                        {isLoading ? 'Updating...' : 'Request Update'}
                    </span>
                </button>
            </div>

            {/* Invoice State Mapping Display */}
            {invoiceMappings.length > 0 ? (
                <div className="border border-border bg-secondary/30 p-4">
                    <div className="mb-3 flex items-center justify-between">
                        <h3 className="text-sm font-semibold uppercase tracking-wider text-foreground">
                            Status Codes
                        </h3>
                        {lastUpdated && (
                            <p className="text-xs text-muted-foreground">
                                Updated on {formatTimestamp(lastUpdated)}
                            </p>
                        )}
                    </div>

                    <div className="overflow-x-auto">
                        <table className="w-full text-sm">
                            <thead>
                                <tr className="border-b border-border">
                                    <th className="pb-2 text-left text-xs font-semibold uppercase tracking-wider text-muted-foreground">
                                        Code
                                    </th>
                                    <th className="pb-2 text-left text-xs font-semibold uppercase tracking-wider text-muted-foreground">
                                        Label
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {invoiceMappings.map((mapping) => (
                                    <tr key={mapping.id} className="border-b border-border/50">
                                        <td className="py-2 font-mono text-xs text-foreground">
                                            {mapping.code}
                                        </td>
                                        <td className="py-2 text-xs text-foreground">
                                            {mapping.label}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            ) : (
                <div className="border border-border bg-secondary/30 p-8 text-center">
                    <p className="text-sm text-muted-foreground">
                        {isLoading ? 'Loading invoice state mapping...' : 'No invoice state mapping found. Click "Request Update" to fetch the latest data.'}
                    </p>
                </div>
            )}
        </div>
    )
}
