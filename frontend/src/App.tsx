import React, { useState } from 'react';
import { useAdmin } from './hooks/useAdmin';
import { adminService } from './api/adminService';
import './App.css';

type View = 'SELECTION' | 'ADMIN_CONNECT' | 'CUSTOMER_CONNECT' | 'DASHBOARD' | 'CREATE_ACCOUNT' | 'VIEW_COMPANIES' | 'CUSTOMER_DASHBOARD';
type DashboardMode = 'WELCOME' | 'LIST';

const App: React.FC = () => {
  const {
    connect,
    customerConnect,
    createAccount,
    fetchCompanies,
    fetchProjects,
    companies,
    projects,
    loading,
    error
  } = useAdmin();

  const [view, setView] = useState<View>('SELECTION');
  const [dashboardMode, setDashboardMode] = useState<DashboardMode>('WELCOME');
  const [connId, setConnId] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [custId, setCustId] = useState<string>('');
  const [compId, setCompId] = useState<number>(0);

  const resetPortal = () => {
    setConnId('');
    setEmail('');
    setCustId('');
    setCompId(0);
    setDashboardMode('WELCOME');
    setView('SELECTION');
  };

  // --- ADMIN HANDLERS ---
  const handleAdminConnect = async () => {
    try {
      const res = await connect(email);
      setConnId(res.connectionId);
      await fetchCompanies(res.connectionId);
      setView('DASHBOARD');
    } catch (e) { /* Error handled by hook */ }
  };

  const handleFormSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const selectedCompanyId = Number(formData.get('companyId'));
    try {
      const res = await createAccount({
        connectionId: connId,
        clientEmail: formData.get('clientEmail') as string,
        companyId: selectedCompanyId
      });

      alert("Account Created Successfully! Verifying session...");
      await adminService.getFreshSession(res.customerId, selectedCompanyId);
      setView('DASHBOARD');
    } catch (e) { console.error("Creation failed", e); }
  };

  // --- CUSTOMER HANDLERS ---
  const handleCustomerConnect = async () => {
    if (!custId || !email) {
      alert("Please fill in all fields");
      return;
    }
    try {
      const result = await customerConnect(custId, email);
      setCompId(result.companyId);
      const session = await adminService.getFreshSession(custId, result.companyId);
      setConnId(session.sessionId);
      setDashboardMode('WELCOME');
      setView('CUSTOMER_DASHBOARD');
    } catch (e: any) {
      console.error("Connection/Discovery failed", e);
    }
  };

  const handleAccessProjects = async () => {
    try {
      await fetchProjects(connId);
      setDashboardMode('LIST');
    } catch (e) {
      console.error("Fetch projects failed:", e);
    }
  };

  return (
    <div className="portal-container">
      <header className="portal-header">
        <h1>Enterprise Portal</h1>
        {view !== 'SELECTION' && (
          <button onClick={resetPortal} className="btn-logout">Logout / Exit</button>
        )}
      </header>

      {error && <p className="error-banner">{error}</p>}
      <hr style={{ opacity: 0.2, margin: '20px 0' }} />

      {/* ROLE SELECTION */}
      {view === 'SELECTION' && (
        <section style={{ textAlign: 'center' }}>
          <h2>Identify your role:</h2>
          <div className="role-selection-container">
            <button onClick={() => setView('ADMIN_CONNECT')} className="role-button">
              <span className="role-icon">🛡️</span>Admin Portal
            </button>
            <button onClick={() => setView('CUSTOMER_CONNECT')} className="role-button">
              <span className="role-icon">👤</span>Customer Login
            </button>
          </div>
        </section>
      )}

      {/* ADMIN CONNECT VIEW */}
      {view === 'ADMIN_CONNECT' && (
        <section>
          <h3>Admin Connection</h3>
          <label className="form-label">Admin Email</label>
          <input
            type="email"
            className="form-input"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="admin@enterprise.com"
          />
          <button onClick={handleAdminConnect} disabled={loading} className="btn-primary">
            {loading ? 'Connecting...' : 'Connect to Admin'}
          </button>
        </section>
      )}

      {/* ADMIN DASHBOARD */}
      {view === 'DASHBOARD' && (
        <section>
          <h3>Admin Dashboard</h3>
          <p>Connection ID: <code>{connId}</code></p>
          <div style={{ display: 'flex', gap: '10px' }}>
            <button onClick={() => setView('CREATE_ACCOUNT')} className="btn-primary">Create Customer Account</button>
            <button onClick={() => setView('VIEW_COMPANIES')} className="btn-primary">View Companies</button>
          </div>
        </section>
      )}

      {/* CREATE ACCOUNT VIEW */}
      {view === 'CREATE_ACCOUNT' && (
        <section>
          <button onClick={() => setView('DASHBOARD')} className="back-link">← Back</button>
          <h3>Create New Customer Account</h3>
          <form onSubmit={handleFormSubmit}>
            <label className="form-label">Client Email</label>
            <input name="clientEmail" type="email" required className="form-input" />

            <label className="form-label">Select Company</label>
            <select name="companyId" className="form-input" required>
              {companies.map((c: any) => (
                <option key={c.companyId} value={c.companyId}>{c.companyName}</option>
              ))}
            </select>

            <button type="submit" disabled={loading} className="btn-primary">
              {loading ? 'Creating...' : 'Create Account'}
            </button>
          </form>
        </section>
      )}

      {/* VIEW COMPANIES */}
      {view === 'VIEW_COMPANIES' && (
        <section>
          <button onClick={() => setView('DASHBOARD')} className="back-link">← Back</button>
          <h3>System Companies</h3>
          {companies.map((c: any) => (
            <div key={c.companyId} className="project-card">
              <strong>{c.companyName}</strong> (ID: {c.companyId})
            </div>
          ))}
        </section>
      )}

      {/* CUSTOMER CONNECT VIEW */}
      {view === 'CUSTOMER_CONNECT' && (
        <section>
          <h3>Customer Login</h3>
          <p style={{ fontSize: '0.9rem', color: '#666' }}>Enter your unique ID to discover your workspace.</p>

          <label className="form-label">Account ID (UUID)</label>
          <input placeholder="UUID" value={custId} onChange={(e) => setCustId(e.target.value)} className="form-input" />

          <label className="form-label">Email Address</label>
          <input type="email" placeholder="customer@email.com" value={email} onChange={(e) => setEmail(e.target.value)} className="form-input" />

          <button onClick={handleCustomerConnect} disabled={loading} className="btn-primary">
            {loading ? 'Discovering workspace...' : 'Login'}
          </button>
        </section>
      )}

      {/* CUSTOMER DASHBOARD */}
      {view === 'CUSTOMER_DASHBOARD' && (
        <section className="customer-panel">
          {dashboardMode === 'WELCOME' ? (
            <div style={{ textAlign: 'center' }}>
              <h2>Welcome back!</h2>
              <div className="workspace-info">
                <span style={{ fontSize: '1.2rem' }}>🏢</span>
                <p style={{ margin: '5px 0', fontWeight: 'bold' }}>Your Workspace Context:</p>
                <code>Company ID: {compId}</code>
              </div>
              <button onClick={handleAccessProjects} disabled={loading} className="btn-primary">
                {loading ? 'Fetching Projects...' : 'Access My Projects'}
              </button>
            </div>
          ) : (
            <div>
              <button onClick={() => setDashboardMode('WELCOME')} className="back-link">← Back</button>
              <h3>My Projects</h3>
              <div className="project-list">
                {projects && projects.length > 0 ? (
                  projects.map((p: any) => (
                    <div key={p.projectId} className="project-card">
                      <h4>{p.projectName}</h4>
                      <p>{p.projectDescription}</p>
                    </div>
                  ))
                ) : <p>No active projects found.</p>}
              </div>
            </div>
          )}
        </section>
      )}
    </div>
  );
};

export default App;