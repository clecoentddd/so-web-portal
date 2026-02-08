"use client"

import { ArrowLeft, FolderOpen, Loader2, Building2, Info, CheckCircle2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { useAdmin } from "@/hooks/useAdmin"
import { useOrders } from "@/hooks/useOrders"
import { useState, useEffect } from "react"
import { OrdersModal } from "./orders-modal"

interface Project {
  projectId: number
  reference: string
  projectTitle: string
  projectDescription: string
  startDate?: string
  endDate?: string
  forecastEndDate?: string
  status: string
  manager: string
}

interface CustomerDashboardProps {
  compId: number
  companyName: string
  sessionId: string
  customerId: string
  dashboardMode: "WELCOME" | "LIST"
  projects: Project[]
  loading: boolean
  onAccessProjects: () => void
  onBackToWelcome: () => void
}

export function CustomerDashboard({
  compId,
  companyName,
  sessionId,
  customerId,
  dashboardMode,
  projects,
  loading,
  onAccessProjects,
  onBackToWelcome,
}: CustomerDashboardProps) {
  console.log("[CustomerDashboard] Rendered. Props:", { compId, companyName, sessionId, customerId });

  const { requestProjectDetails, resolveCompany, loading: requestLoading } = useAdmin()
  const { fetchSessionData, allOrders, allInvoices, loading: ordersLoading, error: ordersError } = useOrders()
  const [requestedProjects, setRequestedProjects] = useState<Record<number, boolean>>({})
  const [isOrdersModalOpen, setIsOrdersModalOpen] = useState(false)
  const [selectedProjectTitle, setSelectedProjectTitle] = useState("")
  const [selectedProjectId, setSelectedProjectId] = useState<number | null>(null)
  const [selectedProjectManager, setSelectedProjectManager] = useState("")
  const [displayName, setDisplayName] = useState(companyName)

  // Fallback: If companyName prop is missing, try to resolve it using the hook
  useEffect(() => {
    if (companyName) {
      setDisplayName(companyName)
    } else if (customerId && !displayName) {
      console.log("[CustomerDashboard] companyName prop missing. Attempting to resolve...");
      resolveCompany(customerId).then(data => {
        if (data) setDisplayName(data.companyName)
      }).catch(err => console.error("Failed to resolve company name in dashboard", err));
    }
  }, [companyName, customerId, resolveCompany, displayName])

  const handleRequestDetails = async (project: Project) => {
    setSelectedProjectTitle(project.projectTitle)
    setSelectedProjectManager(project.manager)
    setSelectedProjectId(project.projectId)
    setIsOrdersModalOpen(true)
    // Fetch (or poll) data for the whole session. 
    // The hook handles the polling loop.
    await fetchSessionData(sessionId, compId, customerId)
  }

  // Client-side Join: Filter global state by the selected project
  const projectOrders = selectedProjectId ? allOrders.filter(o => o.projectId === selectedProjectId) : []
  const projectInvoices = selectedProjectId ? allInvoices.filter(i => i.projectId === selectedProjectId) : []

  if (dashboardMode === "WELCOME") {
    return (
      <div className="py-4">
        <div className="mb-8 text-center">
          <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center border border-[#FBBB10]/30 bg-[#FBBB10]/5">
            <Building2 className="h-6 w-6 text-[#FBBB10]" />
          </div>
          <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">
            Welcome back
          </p>
          <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
            Your Workspace
          </h2>
        </div>

        {/* Workspace info card */}
        <div className="mb-6 border border-border bg-secondary/30 px-5 py-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs uppercase tracking-wider text-muted-foreground">
                Company
              </p>
              <code className="mt-1 block font-mono text-sm text-[#FBBB10]">
                {displayName || "Loading..."}
              </code>
            </div>
            <Badge
              variant="outline"
              className="border-[#FBBB10]/30 text-[#FBBB10]"
            >
              Connected
            </Badge>
          </div>
        </div>

        <Button
          onClick={onAccessProjects}
          disabled={loading}
          className="w-full bg-[#FBBB10] text-[#111827] text-xs font-semibold uppercase tracking-[0.15em] hover:bg-[#FBBB10]/90 hover:shadow-[0_0_20px_rgba(251,187,16,0.25)] disabled:bg-muted disabled:text-muted-foreground"
        >
          {loading ? (
            <>
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              Fetching Projects...
            </>
          ) : (
            <>
              <FolderOpen className="mr-2 h-4 w-4" />
              Access My Projects
            </>
          )}
        </Button>
      </div>
    )
  }

  return (
    <div className="py-4">
      <button
        type="button"
        onClick={onBackToWelcome}
        className="mb-6 flex items-center gap-2 text-xs uppercase tracking-wider text-muted-foreground transition-colors hover:text-[#FBBB10]"
      >
        <ArrowLeft className="h-3.5 w-3.5" />
        Back to Workspace
      </button>

      <div className="mb-6 flex items-center justify-between">
        <div>
          <p className="text-xs uppercase tracking-[0.2em] text-[#FBBB10] font-semibold">
            Workspace - {displayName}
          </p>
          <h2 className="mt-2 text-lg font-semibold tracking-tight text-foreground">
            My Projects
          </h2>
        </div>
        {projects.length > 0 && (
          <Badge
            variant="outline"
            className="border-border text-muted-foreground"
          >
            {projects.length} project{projects.length !== 1 ? "s" : ""}
          </Badge>
        )}
      </div>

      <div className="space-y-2">
        {projects.length > 0 ? (
          projects.map((p) => (
            <div
              key={p.projectId}
              className="group border border-border bg-secondary/30 px-5 py-4 transition-colors hover:border-[#FBBB10]/30 hover:bg-secondary/50"
            >
              <div className="flex items-start justify-between gap-3">
                <div className="flex-1">
                  <div className="flex items-center gap-2">
                    <h4 className="text-sm font-semibold text-foreground">
                      {p.projectTitle}
                    </h4>
                    <Badge variant="outline" className="h-5 px-1.5 text-[10px] uppercase border-[#FBBB10]/20 text-[#FBBB10]/70">
                      {p.status}
                    </Badge>
                  </div>
                  <p className="mt-1 text-sm leading-relaxed text-muted-foreground">
                    {p.projectDescription}
                  </p>

                  <div className="mt-3 flex flex-wrap gap-x-4 gap-y-1">
                    <div className="flex flex-col">
                      <span className="text-[10px] uppercase tracking-wider text-muted-foreground/50">Manager</span>
                      <span className="text-xs text-muted-foreground">{p.manager || "-"}</span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-[10px] uppercase tracking-wider text-muted-foreground/50">Start Date</span>
                      <span className="text-xs text-muted-foreground">{p.startDate || "-"}</span>
                    </div>
                    {p.endDate ? (
                      <div className="flex flex-col">
                        <span className="text-[10px] uppercase tracking-wider text-muted-foreground/50">End Date</span>
                        <span className="text-xs text-muted-foreground">{p.endDate}</span>
                      </div>
                    ) : (
                      <div className="flex flex-col">
                        <span className="text-[10px] uppercase tracking-wider text-muted-foreground/50">Forecast End</span>
                        <span className="text-xs text-muted-foreground">{p.forecastEndDate || "-"}</span>
                      </div>
                    )}
                  </div>
                </div>
                <div className="mt-1 h-1.5 w-1.5 shrink-0 bg-[#FBBB10] opacity-0 transition-opacity group-hover:opacity-100" />
              </div>
              <div className="mt-4 flex items-center justify-between gap-2 border-t border-border/50 pt-3">
                <span className="font-mono text-[10px] text-muted-foreground/40 uppercase tracking-tighter">
                  REF: {p.reference}
                </span>

                <Button
                  size="sm"
                  variant="ghost"
                  disabled={requestLoading || requestedProjects[p.projectId]}
                  onClick={() => handleRequestDetails(p)}
                  className="h-7 px-2 text-[10px] font-semibold uppercase tracking-wider text-[#FBBB10] hover:bg-[#FBBB10]/10 hover:text-[#FBBB10]"
                >
                  {requestedProjects[p.projectId] ? (
                    <>
                      <CheckCircle2 className="mr-1 h-3 w-3" />
                      Requested
                    </>
                  ) : (
                    <>
                      <Info className="mr-1 h-3 w-3" />
                      Project Details
                    </>
                  )}
                </Button>
              </div>
            </div>
          ))
        ) : (
          <div className="border border-dashed border-border py-12 text-center">
            <FolderOpen className="mx-auto mb-3 h-8 w-8 text-muted-foreground/40" />
            <p className="text-sm text-muted-foreground">
              No active projects found.
            </p>
          </div>
        )}
      </div>


      <OrdersModal
        isOpen={isOrdersModalOpen}
        onClose={() => setIsOrdersModalOpen(false)}
        loading={ordersLoading}
        error={ordersError}
        projectTitle={selectedProjectTitle}
        manager={selectedProjectManager}
        companyName={displayName}
        orders={projectOrders}
        invoices={projectInvoices}
      />
    </div >
  )
}
