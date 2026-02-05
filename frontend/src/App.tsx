import React, { useState } from 'react';
import { useAdmin } from './hooks/useAdmin';
import './App.css'; // This replaces all inline styles

type View = 'SELECTION' | 'ADMIN_CONNECT' | 'CUSTOMER_CONNECT' | 'DASHBOARD' | 'CREATE_ACCOUNT' | 'VIEW_COMPANIES' | 'CUSTOMER_DASHBOARD';

const App: React.FC = () => {
  const { connect, customerConnect, createAccount, fetchCompanies, companies, loading, error } = useAdmin();

  const [view, setView] = useState<View>('SELECTION');
  const [connId, setConnId] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [custId, setCustId] = useState<string>('');

  const resetPortal = () => {
    setConnId('');
    setEmail('');
    setCustId('');
    setView('SELECTION');
  };

  const handleAdminConnect = async () => {
    try {
      const res = await connect(email);
      setConnId(res.connectionId);
      await fetchCompanies(res.connectionId);
      setView('DASHBOARD');
    } catch (e) { /* Error in hook */ }
  };

  const handleFormSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    try {
      await createAccount({
        connectionId: connId,
        clientEmail: formData.get('clientEmail') as string,
        companyId: Number(formData.get('companyId'))
      });
      alert("Account Created Successfully!");
      setView('DASHBOARD');
    } catch (e) { /* Error in hook */ }
  };

  const handleCustomerConnect = async () => {
    try {
      await customerConnect(custId, email);
      setView('CUSTOMER_DASHBOARD');
    } catch (e) { /* Error in hook */ }
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

      {view === 'ADMIN_CONNECT' && (
        <section>
          <h3>Admin Authentication</h3>
          <label className="form-label">Admin Email</label>
          <input type="email" placeholder="admin@email.com" value={email} onChange={(e) => setEmail(e.target.value)} className="form-input" />
          <button onClick={handleAdminConnect} disabled={loading || !email} className="btn-primary">
            {loading ? 'Connecting...' : 'Connect to Boond'}
          </button>
        </section>
      )}

      {view === 'CUSTOMER_CONNECT' && (
        <section>
          <h3>Customer Login</h3>
          <label className="form-label">Account ID (UUID)</label>
          <input placeholder="UUID" value={custId} onChange={(e) => setCustId(e.target.value)} className="form-input" />
          <label className="form-label">Email Address</label>
          <input type="email" placeholder="customer@email.com" value={email} onChange={(e) => setEmail(e.target.value)} className="form-input" />
          <button onClick={handleCustomerConnect} disabled={loading || !email || !custId} className="btn-primary">
            {loading ? 'Verifying...' : 'Login'}
          </button>
        </section>
      )}

      {view === 'DASHBOARD' && (
        <section className="admin-panel">
          <h3>Admin Control Panel</h3>
          <p>Status: <strong>Connected</strong> ({email})</p>
          <div style={{ display: 'flex', gap: '10px' }}>
            <button onClick={() => setView('CREATE_ACCOUNT')} className="btn-success">+ Create Account</button>
            <button onClick={() => setView('VIEW_COMPANIES')} className="btn-secondary">View Companies ({companies.length})</button>
          </div>
        </section>
      )}

      {view === 'CREATE_ACCOUNT' && (
        <section>
          <button onClick={() => setView('DASHBOARD')} className="back-link">← Back to Dashboard</button>
          <h3>New Customer Registration</h3>
          <form onSubmit={handleFormSubmit}>
            <label className="form-label">Client Email</label>
            <input name="clientEmail" type="email" required className="form-input" />
            <label className="form-label">Assign Company</label>
            <select name="companyId" required className="form-input">
              <option value="">-- Choose Company --</option>
              {companies.map(c => <option key={c.companyId} value={c.companyId}>{c.companyName}</option>)}
            </select>
            <button type="submit" disabled={loading} className="btn-primary">Register Account</button>
          </form>
        </section>
      )}

      {view === 'VIEW_COMPANIES' && (
        <section>
          <button onClick={() => setView('DASHBOARD')} className="back-link">← Back</button>
          <h3>Company Directory</h3>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            {companies.map(c => (
              <li key={c.companyId} style={{ padding: '10px 0', borderBottom: '1px solid #eee' }}>
                {c.companyName} <code style={{ float: 'right' }}>{c.companyId}</code>
              </li>
            ))}
          </ul>
        </section>
      )}

      {view === 'CUSTOMER_DASHBOARD' && (
        <section className="customer-panel">
          <h2 style={{ color: '#0050b3' }}>Success!</h2>
          <p>You have accessed your private customer dashboard.</p>
          <p>Account ID: <code>{custId}</code></p>
        </section>
      )}
    </div>
  );
};

export default App;