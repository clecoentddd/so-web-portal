"use client"

import { UserPlus, Building2, Terminal, Users, FileText, ChevronRight } from "lucide-react"
import { Badge } from "@/components/ui/badge"

interface AdminDashboardProps {
  connId: string
  onCreateAccount: () => void
  onViewCompanies: () => void
  onViewCustomers: () => void
  onViewInvoiceMapping: () => void
  onLogout: () => void
  companies: any[]
}

export function AdminDashboard({
  connId,
  onCreateAccount,
  onViewCompanies,
  onViewCustomers,
  onViewInvoiceMapping,
  companies = [],
  onLogout,
}: AdminDashboardProps) {
  const lastUpdated = companies?.length > 0 ? companies[0].timestamp : null

  const formatTimestamp = (timestamp: number) => {
    return new Date(timestamp).toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }
  return (
    <div className="py-4">
      <div className="mb-6">
        <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">
          Control Panel
        </p>
        <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
          Admin Dashboard
        </h2>
      </div>

      {/* Connection status */}
      <div className="mb-6 flex items-center gap-3 border border-border bg-secondary/30 px-4 py-3">
        <Terminal className="h-4 w-4 text-muted-foreground" />
        <div className="flex flex-1 items-center gap-2 overflow-hidden">
          <span className="shrink-0 text-xs uppercase tracking-wider text-muted-foreground">
            Session
          </span>
          <code className="truncate font-mono text-xs text-[#FBBB10]">
            {connId}
          </code>
        </div>
        <Badge
          variant="outline"
          className="shrink-0 border-[#FBBB10]/30 text-[#FBBB10]"
        >
          Active
        </Badge>
      </div>

      {/* Actions */}
      <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
        <button
          type="button"
          onClick={onCreateAccount}
          className="group flex items-center gap-4 border border-border bg-secondary/50 px-5 py-5 transition-all duration-200 hover:border-[#FBBB10] hover:bg-secondary"
        >
          <div className="flex h-10 w-10 items-center justify-center border border-border bg-background transition-colors group-hover:border-[#FBBB10]/50">
            <UserPlus className="h-4 w-4 text-muted-foreground transition-colors group-hover:text-[#FBBB10]" />
          </div>
          <div className="text-left">
            <p className="text-sm font-semibold uppercase tracking-wider text-foreground">
              Create Account
            </p>
            <p className="text-xs text-muted-foreground">
              New customer setup
            </p>
          </div>
        </button>

        <button
          type="button"
          onClick={onViewCompanies}
          className="group flex items-center gap-4 border border-border bg-secondary/50 px-5 py-5 transition-all duration-200 hover:border-[#FBBB10] hover:bg-secondary"
        >
          <div className="flex h-10 w-10 items-center justify-center border border-border bg-background group-hover:border-[#FBBB10]/50">
            <Building2 className="h-5 w-5 text-muted-foreground group-hover:text-[#FBBB10]" />
          </div>
          <div className="flex-1 text-left">
            <h3 className="text-sm font-semibold text-foreground">Companies</h3>
            <p className="text-xs text-muted-foreground">
              {companies.length} business entities
            </p>
          </div>
          <div className="flex flex-col items-end gap-1">
            <div className="flex h-6 w-6 items-center justify-center border border-border bg-background group-hover:border-[#FBBB10]/50">
              <ChevronRight className="h-3.5 w-3.5 text-muted-foreground group-hover:text-[#FBBB10]" />
            </div>
            {lastUpdated && (
              <p className="text-[10px] text-muted-foreground whitespace-nowrap">
                {formatTimestamp(lastUpdated)}
              </p>
            )}
          </div>
        </button>

        <button
          type="button"
          onClick={() => {
            console.log("[AdminDashboard] 'Customers' button clicked. Navigating...");
            onViewCustomers();
          }}
          className="group flex items-center gap-4 border border-border bg-secondary/50 px-5 py-5 transition-all duration-200 hover:border-[#FBBB10] hover:bg-secondary"
        >
          <div className="flex h-10 w-10 items-center justify-center border border-border bg-background transition-colors group-hover:border-[#FBBB10]/50">
            <Users className="h-4 w-4 text-muted-foreground transition-colors group-hover:text-[#FBBB10]" />
          </div>
          <div className="text-left">
            <p className="text-sm font-semibold uppercase tracking-wider text-foreground">
              Customers
            </p>
            <p className="text-xs text-muted-foreground">
              See account list
            </p>
          </div>
        </button>

        <button
          type="button"
          onClick={onViewInvoiceMapping}
          className="group flex items-center gap-4 border border-border bg-secondary/50 px-5 py-5 transition-all duration-200 hover:border-[#FBBB10] hover:bg-secondary"
        >
          <div className="flex h-10 w-10 items-center justify-center border border-border bg-background transition-colors group-hover:border-[#FBBB10]/50">
            <FileText className="h-4 w-4 text-muted-foreground transition-colors group-hover:text-[#FBBB10]" />
          </div>
          <div className="text-left">
            <p className="text-sm font-semibold uppercase tracking-wider text-foreground">
              Invoice State Mapping
            </p>
            <p className="text-xs text-muted-foreground">
              View state mapping
            </p>
          </div>
        </button>
      </div>
    </div>
  )
}
