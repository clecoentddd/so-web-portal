"use client"

import { ArrowLeft, Building2 } from "lucide-react"
import { Badge } from "@/components/ui/badge"

interface ViewCompaniesProps {
  companies: { companyId: number; companyName: string }[]
  onBack: () => void
}

export function ViewCompanies({ companies, onBack }: ViewCompaniesProps) {
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
            System Companies
          </h2>
        </div>
        <Badge
          variant="outline"
          className="border-border text-muted-foreground"
        >
          {companies.length} total
        </Badge>
      </div>

      <div className="space-y-2">
        {companies.map((c) => (
          <div
            key={c.companyId}
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
