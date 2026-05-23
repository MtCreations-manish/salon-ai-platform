import React, { createContext, useContext, useEffect, useState } from 'react'
import api from '../services/api'

const AuthContext = createContext(null)

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [token, setToken] = useState(() => localStorage.getItem('token'))
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (token) {
      localStorage.setItem('token', token)
      // Optionally fetch user profile
      // api.get('/api/auth/me')...
    } else {
      localStorage.removeItem('token')
      setUser(null)
    }
  }, [token])

  const login = async (credentials) => {
    setLoading(true)
    try {
      const res = await api.post('/api/auth/login', credentials)
      const t = res.data?.token || res.data?.accessToken || res.data
      setToken(t)
      setUser(res.data?.user || null)
      setLoading(false)
      return { ok: true }
    } catch (err) {
      setLoading(false)
      return { ok: false, error: err.response?.data || err.message }
    }
  }

  const register = async (payload) => {
    setLoading(true)
    try {
      const res = await api.post('/api/auth/register', payload)
      setLoading(false)
      return { ok: true, data: res.data }
    } catch (err) {
      setLoading(false)
      return { ok: false, error: err.response?.data || err.message }
    }
  }

  const logout = () => {
    setToken(null)
    setUser(null)
    localStorage.removeItem('token')
  }

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)

export default AuthContext
