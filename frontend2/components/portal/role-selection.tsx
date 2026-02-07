"use client"

import { Shield, User } from "lucide-react"

interface RoleSelectionProps {
  onSelectAdmin: () => void
  onSelectCustomer: () => void
}

export function RoleSelection({
  onSelectAdmin,
  onSelectCustomer,
}: RoleSelectionProps) {
  return (
    <div className="py-4">
      <div className="mb-8 text-center">
        <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">
          Select access level
        </p>
        <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
          Identify your role
        </h2>
      </div>

      <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
        <button
          type="button"
          onClick={onSelectAdmin}
          className="group flex flex-col items-center gap-4 border border-border bg-secondary/50 px-6 py-8 transition-all duration-200 hover:border-[#FBBB10] hover:bg-secondary"
        >
          <div className="flex h-12 w-12 items-center justify-center border border-border bg-background transition-colors group-hover:border-[#FBBB10]/50 group-hover:bg-[#FBBB10]/5">
            <Shield className="h-5 w-5 text-muted-foreground transition-colors group-hover:text-[#FBBB10]" />
          </div>
          <div>
            <p className="text-sm font-semibold uppercase tracking-wider text-foreground">
              Admin Portal
            </p>
            <p className="mt-1 text-xs text-muted-foreground">
              Manage accounts & companies
            </p>
          </div>
        </button>

        <button
          type="button"
          onClick={onSelectCustomer}
          className="group flex flex-col items-center gap-4 border border-border bg-secondary/50 px-6 py-8 transition-all duration-200 hover:border-[#FBBB10] hover:bg-secondary"
        >
          <div className="flex h-12 w-12 items-center justify-center border border-border bg-background transition-colors group-hover:border-[#FBBB10]/50 group-hover:bg-[#FBBB10]/5">
            <User className="h-5 w-5 text-muted-foreground transition-colors group-hover:text-[#FBBB10]" />
          </div>
          <div>
            <p className="text-sm font-semibold uppercase tracking-wider text-foreground">
              Customer Login
            </p>
            <p className="mt-1 text-xs text-muted-foreground">
              Access your projects
            </p>
          </div>
        </button>
      </div>
    </div>
  )
}
