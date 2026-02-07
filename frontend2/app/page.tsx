"use client"

import React from "react"
import { useAdmin } from "@/hooks/useAdmin"

import { useState } from "react"
import { PortalHeader } from "@/components/portal/portal-header"
import { RoleSelection } from "@/components/portal/role-selection"
import { AdminConnect } from "@/components/portal/admin-connect"
import { AdminDashboard } from "@/components/portal/admin-dashboard"
import { CreateAccount } from "@/components/portal/create-account"
import { ViewCompanies } from "@/components/portal/view-companies"
import { CustomerConnect } from "@/components/portal/customer-connect"
import { CustomerDashboard } from "@/components/portal/customer-dashboard"
import { ErrorBanner } from "@/components/portal/error-banner"
import { adminService } from "@/app/api/adminService"

type View =
  | "SELECTION"
  | "ADMIN_CONNECT"
  | "CUSTOMER_CONNECT"
  | "DASHBOARD"
  | "CREATE_ACCOUNT"
  | "VIEW_COMPANIES"
  | "CUSTOMER_DASHBOARD"
type DashboardMode = "WELCOME" | "LIST"

// Mock data and hooks for demonstration
// In production, replace with your actual useAdmin hook and adminService

export default function Page() {
  const {
    connect,
    customerConnect,
    createAccount,
    fetchCompanies,
    fetchProjects,
    companies,
    projects,
    loading,
    error,
  } = useAdmin()

  const [view, setView] = useState<View>("SELECTION")
  const [dashboardMode, setDashboardMode] = useState<DashboardMode>("WELCOME")
  const [connId, setConnId] = useState("")
  const [email, setEmail] = useState("")
  const [custId, setCustId] = useState("")
  const [compId, setCompId] = useState(0)

  const resetPortal = () => {
    setConnId("")
    setEmail("")
    setCustId("")
    setCompId(0)
    setDashboardMode("WELCOME")
    setView("SELECTION")
  }

  const handleAdminConnect = async () => {
    try {
      const res = await connect(email)
      setConnId(res.connectionId)
      await fetchCompanies(res.connectionId)
      setView("DASHBOARD")
    } catch {
      // Error handled by hook
    }
  }

  const handleFormSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    const formData = new FormData(e.currentTarget)
    const selectedCompanyId = Number(formData.get("companyId"))
    try {
      await createAccount({
        connectionId: connId,
        clientEmail: formData.get("clientEmail") as string,
        companyId: selectedCompanyId,
      })
      setView("DASHBOARD")
    } catch (err) {
      console.error("Creation failed", err)
    }
  }

  const handleCustomerConnect = async () => {
    if (!custId || !email) return
    try {
      // 1. Resolve Company & Fire Command
      const result = await customerConnect(custId, email)
      setCompId(result.companyId)

      // 2. DISCOVERY STEP (Missing): Wait for the projection to sync
      // This provides the sessionId (UUID) needed for the project list
      const session = await adminService.getFreshSession(custId, result.companyId);

      // 3. Set the Session ID so fetchProjects has a valid UUID
      setConnId(session.sessionId)

      setDashboardMode("WELCOME")
      setView("CUSTOMER_DASHBOARD")
    } catch (err) {
      console.error("Connection/Discovery failed", err)
    }
  }

  const handleAccessProjects = async () => {
    try {
      await fetchProjects(connId)
      setDashboardMode("LIST")
    } catch (err) {
      console.error("Fetch projects failed:", err)
    }
  }

  return (
    <main className="flex min-h-screen items-center justify-center p-4 md:p-8">
      {/* Background grid pattern */}
      <div className="pointer-events-none fixed inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:60px_60px]" />

      <div className="relative z-10 w-full max-w-xl">
        <div className="border border-border bg-card shadow-2xl shadow-black/50">
          <PortalHeader
            showLogout={view !== "SELECTION"}
            onLogout={resetPortal}
          />

          <div className="px-6 pb-8 md:px-8">
            {error && <ErrorBanner message={error} />}

            {view === "SELECTION" && (
              <RoleSelection
                onSelectAdmin={() => setView("ADMIN_CONNECT")}
                onSelectCustomer={() => setView("CUSTOMER_CONNECT")}
              />
            )}

            {view === "ADMIN_CONNECT" && (
              <AdminConnect
                email={email}
                setEmail={setEmail}
                loading={loading}
                onConnect={handleAdminConnect}
                onBack={() => setView("SELECTION")}
              />
            )}

            {view === "DASHBOARD" && (
              <AdminDashboard
                connId={connId}
                onCreateAccount={() => setView("CREATE_ACCOUNT")}
                onViewCompanies={() => setView("VIEW_COMPANIES")}
                onLogout={resetPortal}
              />
            )}

            {view === "CREATE_ACCOUNT" && (
              <CreateAccount
                companies={companies}
                loading={loading}
                onSubmit={handleFormSubmit}
                onBack={() => setView("DASHBOARD")}
              />
            )}

            {view === "VIEW_COMPANIES" && (
              <ViewCompanies
                companies={companies}
                onBack={() => setView("DASHBOARD")}
              />
            )}

            {view === "CUSTOMER_CONNECT" && (
              <CustomerConnect
                custId={custId}
                setCustId={setCustId}
                email={email}
                setEmail={setEmail}
                loading={loading}
                onConnect={handleCustomerConnect}
                onBack={() => setView("SELECTION")}
              />
            )}

            {view === "CUSTOMER_DASHBOARD" && (
              <CustomerDashboard
                compId={compId}
                sessionId={connId}
                customerId={custId}
                dashboardMode={dashboardMode}
                projects={projects}
                loading={loading}
                onAccessProjects={handleAccessProjects}
                onBackToWelcome={() => setDashboardMode("WELCOME")}
              />
            )}
          </div>
        </div>

        {/* Bottom accent line */}
        <div className="h-px w-full bg-#FBBB10] opacity-30" />
      </div>
    </main>
  )
}
