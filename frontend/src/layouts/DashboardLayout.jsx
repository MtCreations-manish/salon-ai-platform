import React from 'react'
import Sidebar from '../components/Sidebar'
import Navbar from '../components/Navbar'
import AIChatWidget from '../components/AIChatWidget'

const DashboardLayout = ({ children }) => {
  return (
    <div className="min-h-screen flex bg-gradient-to-br from-slate-900 to-slate-800">
      <Sidebar />
      <div className="flex-1 p-4 md:p-8">
        <Navbar />
        <main className="mt-6">{children}</main>
      </div>
      <AIChatWidget />
    </div>
  )
}

export default DashboardLayout
