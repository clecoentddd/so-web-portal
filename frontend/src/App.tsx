import React, { useState } from 'react'
import { useAdmin } from './hooks/useAdmin'

const App: React.FC = () => {
  const { connect, createAccount, loading, error } = useAdmin();
  const [connId, setConnId] = useState<string>('');
  const [email, setEmail] = useState<string>('');

  const handleConnect = async () => {
    try {
      const res = await connect(email);
      setConnId(res.connectionId);
    } catch (e) { /* Error handled by hook state */ }
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
    } catch (e) { /* Error handled by hook state */ }
  };

  return (
    <div style={{ maxWidth: '600px', margin: '40px auto', padding: '20px', fontFamily: 'Arial' }}>
      <h1>Axon Admin Panel (TS)</h1>

      {error && <p style={{ color: 'red', padding: '10px', border: '1px solid red' }}>{error}</p>}

      <section style={{ marginBottom: '20px' }}>
        <h3>1. Connect</h3>
        <input
          type="email"
          placeholder="admin@email.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <button onClick={handleConnect} disabled={loading || !email}>
          {loading ? 'Connecting...' : 'Connect'}
        </button>
      </section>

      {connId && (
        <section style={{ padding: '20px', backgroundColor: '#f4f4f4' }}>
          <h3>2. Create Account for {connId}</h3>
          <form onSubmit={handleFormSubmit}>
            <div style={{ marginBottom: '10px' }}>
              <input name="clientEmail" placeholder="Client Email" required />
            </div>
            <div style={{ marginBottom: '10px' }}>
              <input name="companyId" type="number" placeholder="Company ID" required />
            </div>
            <button type="submit" disabled={loading}>Create Account</button>
          </form>
        </section>
      )}
    </div>
  )
}

export default App