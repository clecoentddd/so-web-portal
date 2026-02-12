"use client"
import { useState, useEffect } from "react"

import { ArrowLeft, Building2, RefreshCw } from "lucide-react"
import { Badge } from "@/components/ui/badge"
import { useAdmin } from "@/hooks/useAdmin"
import { CompanyListLookupItem } from "@/app/api/adminService"

interface ViewCompaniesProps {
  connectionId: string
  onBack: () => void
}

export function ViewCompanies({ connectionId, onBack }: ViewCompaniesProps) {
  const { fetchCompanies, requestCompaniesUpdate, loading } = useAdmin()
  const [companies, setCompanies] = useState<CompanyListLookupItem[]>([])
  const [lastUpdated, setLastUpdated] = useState<number | null>(null)

  const loadCompanies = async () => {
    try {
      const data = await fetchCompanies(connectionId)
      setCompanies(data)
      if (data.length > 0) {
        setLastUpdated(data[0].timestamp)
      }
    } catch (err) {
      console.error("Failed to load companies", err)
    }
  }

  useEffect(() => {
    loadCompanies()
  }, [])

  const handleRequestUpdate = async () => {
    if (companies.length > 0 && companies[0].settingsId) {
      try {
        await requestCompaniesUpdate(companies[0].settingsId, connectionId)
        // Wait a bit then refresh
        setTimeout(loadCompanies, 2000)
      } catch (err) {
        console.error("Failed to request update", err)
      }
    } else {
      // If no companies, we might not have settingsId. 
      // In this system settingsId is usually fixed or deterministic from connection.
      // For now, let's assume we need at least one entry to get the settingsId.
      console.warn("No settingsId available to request update")
    }
  }

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
        <ArrowLeft className="h-3.5 w-3.5" />
        Back to Dashboard
      </button>

      <div className="mb-6 flex items-center justify-between">
        <div>
          <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">
            Directory
          </p>
          <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
            List Of Companies
          </h2>
        </div>
        <div className="flex flex-col items-end gap-2">
          <Badge
            variant="outline"
            className="border-border text-muted-foreground"
          >
            {companies.length} total
          </Badge>
          {lastUpdated && (
            <p className="text-[10px] text-muted-foreground">
              Updated {formatTimestamp(lastUpdated)}
            </p>
          )}
        </div>
      </div>

      {/* Request Update button */}
      <div className="mb-6">
        <button
          type="button"
          onClick={handleRequestUpdate}
          disabled={loading}
          className="group flex items-center gap-2 border border-border bg-secondary/50 px-4 py-2 transition-all duration-200 hover:border-[#FBBB10] hover:bg-secondary disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <RefreshCw className={`h-4 w-4 text-muted-foreground transition-colors group-hover:text-[#FBBB10] ${loading ? 'animate-spin' : ''}`} />
          <span className="text-sm text-foreground">
            {loading ? 'Updating...' : 'Request Update'}
          </span>
        </button>
      </div>

      <div className="space-y-2">
        {companies.length === 0 && (
          <div className="flex flex-col items-center justify-center py-12 border border-dashed border-border bg-secondary/10">
            <Building2 className="h-8 w-8 text-muted-foreground/50 mb-3" />
            <p className="text-sm text-muted-foreground">No companies found.</p>
          </div>
        )}
        {companies.map((c) => (
          <div
            key={c.id}
            className="flex items-center gap-4 border border-border bg-secondary/30 px-5 py-4 transition-colors hover:border-border hover:bg-secondary/50"
          >
            <div className="flex h-9 w-9 items-center justify-center border border-border bg-background">
              <Building2 className="h-4 w-4 text-muted-foreground" />
            </div>
            <div className="flex-1">
              <p className="text-sm font-medium text-foreground">
                {c.companyName}
              </p>
              <p className="font-mono text-xs text-muted-foreground">
                ID: {c.companyId}
              </p>
            </div>
            <div className="h-1.5 w-1.5 bg-[#FBBB10] opacity-50" />
          </div>
        ))}
      </div>
    </div>
  )
}
