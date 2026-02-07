"use client"

import { ArrowLeft, Loader2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"

interface CustomerConnectProps {
  custId: string
  setCustId: (id: string) => void
  email: string
  setEmail: (email: string) => void
  loading: boolean
  onConnect: () => void
  onBack: () => void
}

export function CustomerConnect({
  custId,
  setCustId,
  email,
  setEmail,
  loading,
  onConnect,
  onBack,
}: CustomerConnectProps) {
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
          Workspace Access
        </p>
        <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
          Customer Login
        </h2>
        <p className="mt-2 text-sm leading-relaxed text-muted-foreground">
          Enter your credentials to discover your workspace.
        </p>
      </div>

      <div className="space-y-4">
        <div className="space-y-2">
          <Label
            htmlFor="cust-id"
            className="text-xs uppercase tracking-wider text-muted-foreground"
          >
            Account ID (UUID)
          </Label>
          <Input
            id="cust-id"
            value={custId}
            onChange={(e) => setCustId(e.target.value)}
            placeholder="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
            className="border-border bg-secondary/50 font-mono text-sm text-foreground placeholder:text-muted-foreground/50 focus-visible:ring-[#FBBB10] focus-visible:ring-offset-0"
          />
        </div>

        <div className="space-y-2">
          <Label
            htmlFor="cust-email"
            className="text-xs uppercase tracking-wider text-muted-foreground"
          >
            Email Address
          </Label>
          <Input
            id="cust-email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="customer@email.com"
            className="border-border bg-secondary/50 text-foreground placeholder:text-muted-foreground/50 focus-visible:ring-[#FBBB10] focus-visible:ring-offset-0"
          />
        </div>

        <Button
          onClick={onConnect}
          disabled={loading || !custId || !email}
          className="w-full bg-[#FBBB10] text-[#111827] text-xs font-semibold uppercase tracking-[0.15em] hover:bg-[#FBBB10]/90 hover:shadow-[0_0_20px_rgba(251,187,16,0.25)] disabled:bg-muted disabled:text-muted-foreground"
        >
          {loading ? (
            <>
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              Discovering workspace...
            </>
          ) : (
            "Login"
          )}
        </Button>
      </div>
    </div>
  )
}
