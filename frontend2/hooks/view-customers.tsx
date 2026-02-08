"use client"

import { useState } from "react"
import { ArrowLeft, Users, Mail, Building, Copy, Check, Search } from "lucide-react"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { CustomerAccount } from "@/hooks/useAdmin"

interface ViewCustomersProps {
    accounts: CustomerAccount[]
    onBack: () => void
}

export function ViewCustomers({ accounts, onBack }: ViewCustomersProps) {
    const [copiedId, setCopiedId] = useState<string | null>(null)
    const [searchTerm, setSearchTerm] = useState("")

    const handleCopy = (id: string) => {
        navigator.clipboard.writeText(id)
        setCopiedId(id)
        setTimeout(() => setCopiedId(null), 2000)
    }

    const filteredAccounts = accounts.filter(account =>
        account.clientEmail.toLowerCase().includes(searchTerm.toLowerCase()) ||
        account.customerId.toLowerCase().includes(searchTerm.toLowerCase()) ||
        String(account.companyId).includes(searchTerm)
    )

    return (
        <div className="py-4">
            <button
                type="button"
                onClick={onBack}
                className="mb-6 flex items-center gap-2 text-xs uppercase tracking-wider text-muted-foreground transition-colors hover:text-[#FBBB10]"
            >
                <ArrowLeft className="h-3.5 w-3.5" />
                Back to Dashboard
            </button>

            <div className="mb-6 flex items-center justify-between">
                <div>
                    <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">
                        Directory
                    </p>
                    <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
                        Customer Accounts
                    </h2>
                </div>
                <Badge
                    variant="outline"
                    className="border-border text-muted-foreground"
                >
                    {accounts.length} total
                </Badge>
            </div>

            <div className="relative mb-6">
                <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                    placeholder="Search by email, ID or company..."
                    className="pl-9 border-border bg-secondary/30 text-foreground placeholder:text-muted-foreground/50 focus-visible:ring-[#FBBB10] focus-visible:ring-offset-0"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            <div className="space-y-2">
                {filteredAccounts.map((account) => (
                    <div
                        key={account.customerId}
                        className="flex flex-col gap-2 border border-border bg-secondary/30 px-5 py-4 transition-colors hover:border-border hover:bg-secondary/50 sm:flex-row sm:items-center sm:justify-between"
                    >
                        <div className="flex items-center gap-3">
                            <div className="flex h-9 w-9 items-center justify-center border border-border bg-background">
                                <Mail className="h-4 w-4 text-muted-foreground" />
                            </div>
                            <div>
                                <p className="text-sm font-medium text-foreground">
                                    {account.clientEmail}
                                </p>
                                <div className="flex items-center gap-2">
                                    <p className="font-mono text-xs text-muted-foreground">
                                        ID: {account.customerId}
                                    </p>
                                    <button
                                        onClick={() => handleCopy(account.customerId)}
                                        className="text-muted-foreground hover:text-[#FBBB10] transition-colors"
                                        title="Copy ID"
                                    >
                                        {copiedId === account.customerId ? <Check className="h-3 w-3" /> : <Copy className="h-3 w-3" />}
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div className="flex items-center gap-2 mt-2 sm:mt-0">
                            <Badge variant="secondary" className="flex items-center gap-1 text-xs font-normal">
                                <Building className="h-3 w-3" />
                                Company {account.companyId}
                            </Badge>
                        </div>
                    </div>
                ))}
                {filteredAccounts.length === 0 && (
                    <div className="py-8 text-center text-sm text-muted-foreground border border-dashed border-border">
                        No accounts found matching "{searchTerm}"
                    </div>
                )}
            </div>
        </div>
    )
}