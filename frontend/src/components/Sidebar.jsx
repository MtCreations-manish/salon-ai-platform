import React from 'react'
import { NavLink } from 'react-router-dom'

const items = [
  { to: '/dashboard/salons', label: 'Salons' },
  { to: '/dashboard/services', label: 'Services' },
  { to: '/dashboard/staff', label: 'Staff' },
  { to: '/dashboard/bookings', label: 'Bookings' },
]

const Sidebar = () => {
  return (
    <aside className="hidden md:flex flex-col w-64 p-6 glass h-screen sticky top-0">
      <div className="mb-6">
        <div className="text-2xl font-semibold">Salon AI</div>
        <div className="text-sm text-slate-400">Manager</div>
      </div>

      <nav className="flex-1 space-y-2">
        {items.map((it) => (
          <NavLink
            key={it.to}
            to={it.to}
            className={({ isActive }) =>
              `block px-3 py-2 rounded-md text-sm font-medium hover:bg-white/5 ${
                isActive ? 'bg-white/5' : 'text-slate-200'
              }`
            }
          >
            {it.label}
          </NavLink>
        ))}
      </nav>

      <div className="mt-6 text-sm text-slate-400">v1.0 • AI Assistant</div>
    </aside>
  )
}

export default Sidebar
