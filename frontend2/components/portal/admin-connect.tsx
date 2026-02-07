"use client"

import { ArrowLeft, Loader2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"

interface AdminConnectProps {
  email: string
  setEmail: (email: string) => void
  loading: boolean
  onConnect: () => void
  onBack: () => void
}

export function AdminConnect({
  email,
  setEmail,
  loading,
  onConnect,
  onBack,
}: AdminConnectProps) {
  return (
    <div className="py-4">
      <button
        type="button"
        onClick={onBack}
        className="mb-6 flex items-center gap-2 text-xs uppercase tracking-wider text-muted-foreground transition-colors hover:text-[#FBBB10]"
      >
        <ArrowLeft className="h-3.5 w-3.5" />
        Back
      </button>

      <div className="mb-6">
        <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">
          Authentication
        </p>
        <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
          Admin Connection
        </h2>
      </div>

      <div className="space-y-4">
        <div className="space-y-2">
          <Label
            htmlFor="admin-email"
            className="text-xs uppercase tracking-wider text-muted-foreground"
          >
            Admin Email
          </Label>
          <Input
            id="admin-email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="admin@enterprise.com"
            className="border-border bg-secondary/50 text-foreground placeholder:text-muted-foreground/50 focus-visible:ring-[#FBBB10] focus-visible:ring-offset-0"
          />
        </div>

        <Button
          onClick={onConnect}
          disabled={loading || !email}
          className="w-full bg-[#FBBB10] text-[#111827] text-xs font-semibold uppercase tracking-[0.15em] hover:bg-[#FBBB10]/90 hover:shadow-[0_0_20px_rgba(251,187,16,0.25)] disabled:bg-muted disabled:text-muted-foreground"
        >
          {loading ? (
            <>
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              Connecting...
            </>
          ) : (
            "Connect to Admin"
          )}
        </Button>
      </div>
    </div>
  )
}
