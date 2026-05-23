import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const Login = () => {
  const { login } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setLoading(true)
    const res = await login({ username: email, password })
    setLoading(false)
    if (res.ok) {
      navigate('/dashboard')
    } else {
      setError(res.error || 'Login failed')
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-6">
      <div className="w-full max-w-md glass p-6">
        <h2 className="text-2xl font-semibold mb-4">Sign in to Salon AI</h2>
        <form onSubmit={submit} className="space-y-4">
          <div>
            <label className="block text-sm text-slate-300">Email</label>
            <input
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="mt-1 w-full rounded-md p-2 bg-slate-800 text-slate-100"
            />
          </div>

          <div>
            <label className="block text-sm text-slate-300">Password</label>
            <input
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              type="password"
              className="mt-1 w-full rounded-md p-2 bg-slate-800 text-slate-100"
            />
          </div>

          {error && <div className="text-red-400 text-sm">{String(error)}</div>}

          <div className="flex items-center justify-between">
            <button className="px-4 py-2 bg-indigo-600 text-white rounded-md" disabled={loading}>
              {loading ? 'Signing in...' : 'Sign in'}
            </button>
            <a href="/register" className="text-sm text-slate-300 hover:underline">Create account</a>
          </div>
        </form>
      </div>
    </div>
  )
}

export default Login
