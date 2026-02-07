"use client"

import React from "react"

import { ArrowLeft, Loader2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { useState } from "react"

interface CreateAccountProps {
  companies: { companyId: number; companyName: string }[]
  loading: boolean
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void
  onBack: () => void
}

export function CreateAccount({
  companies,
  loading,
  onSubmit,
  onBack,
}: CreateAccountProps) {
  const [selectedCompanyId, setSelectedCompanyId] = useState<string>("")

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

      <div className="mb-6">
        <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">
          Account Management
        </p>
        <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
          Create Customer Account
        </h2>
      </div>

      <form onSubmit={onSubmit} className="space-y-5">
        <div className="space-y-2">
          <Label
            htmlFor="clientEmail"
            className="text-xs uppercase tracking-wider text-muted-foreground"
          >
            Client Email
          </Label>
          <Input
            id="clientEmail"
            name="clientEmail"
            type="email"
            required
            placeholder="client@company.com"
            className="border-border bg-secondary/50 text-foreground placeholder:text-muted-foreground/50 focus-visible:ring-[#FBBB10] focus-visible:ring-offset-0"
          />
        </div>

        <div className="space-y-2">
          <Label className="text-xs uppercase tracking-wider text-muted-foreground">
            Assign to Company
          </Label>
          <input type="hidden" name="companyId" value={selectedCompanyId} />
          <Select
            value={selectedCompanyId}
            onValueChange={setSelectedCompanyId}
            required
          >
            <SelectTrigger className="border-border bg-secondary/50 text-foreground focus:ring-[#FBBB10] focus:ring-offset-0">
              <SelectValue placeholder="Select a company" />
            </SelectTrigger>
            <SelectContent className="border-border bg-card text-foreground">
              {companies.map((c) => (
                <SelectItem
                  key={c.companyId}
                  value={String(c.companyId)}
                  className="text-foreground focus:bg-secondary focus:text-foreground"
                >
                  {c.companyName}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        <Button
          type="submit"
          disabled={loading}
          className="w-full bg-[#FBBB10] text-[#111827] text-xs font-semibold uppercase tracking-[0.15em] hover:bg-[#FBBB10]/90 hover:shadow-[0_0_20px_rgba(251,187,16,0.25)] disabled:bg-muted disabled:text-muted-foreground"
        >
          {loading ? (
            <>
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              Creating...
            </>
          ) : (
            "Create Account"
          )}
        </Button>
      </form>
    </div>
  )
}
