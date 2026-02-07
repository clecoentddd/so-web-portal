"use client"

import { LogOut } from "lucide-react"
import { Button } from "@/components/ui/button"

interface PortalHeaderProps {
  showLogout: boolean
  onLogout: () => void
}

export function PortalHeader({ showLogout, onLogout }: PortalHeaderProps) {
  return (
    <header className="flex items-center justify-between border-b border-border px-6 py-5 md:px-8">
      <div className="flex items-center gap-3">
        <div className="flex h-8 w-8 items-center justify-center border border-[#FBBB10] bg-[#FBBB10]/10">
          <span className="text-xs font-bold tracking-widest text-[#FBBB10]">
            S
          </span>
        </div>
        <div>
          <h1 className="text-sm font-semibold uppercase tracking-[0.15em] text-foreground">
            Enterprise Portal
          </h1>
          <p className="text-xs uppercase tracking-widest text-muted-foreground">
            Socraft
          </p>
        </div>
      </div>
      {showLogout && (
        <Button
          variant="ghost"
          size="sm"
          onClick={onLogout}
          className="gap-2 text-xs uppercase tracking-wider text-muted-foreground hover:text-[#FBBB10] hover:bg-transparent"
        >
          <LogOut className="h-3.5 w-3.5" />
          <span className="sr-only md:not-sr-only">Exit</span>
        </Button>
      )}
    </header>
  )
}
