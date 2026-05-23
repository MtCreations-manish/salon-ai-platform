import React from 'react'
import { useAuth } from '../context/AuthContext'

const Navbar = () => {
  const { user, logout } = useAuth()

  return (
    <header className="flex items-center justify-between">
      <div className="flex items-center gap-4">
        <button className="md:hidden p-2 rounded bg-white/5">Menu</button>
        <h1 className="text-xl font-semibold">Dashboard</h1>
      </div>

      <div className="flex items-center gap-4">
        <div className="text-sm text-slate-300">{user?.username || 'Admin'}</div>
        <button
          onClick={logout}
          className="px-3 py-1 bg-red-500 text-white rounded-md text-sm hover:opacity-90"
        >
          Logout
        </button>
      </div>
    </header>
  )
}

export default Navbar
