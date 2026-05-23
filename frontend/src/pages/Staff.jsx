import React, { useEffect, useState } from 'react'
import api from '../services/api'
import DashboardLayout from '../layouts/DashboardLayout'
import Button from '../components/Button'

const Staff = () => {
  const [staff, setStaff] = useState([])
  const [name, setName] = useState('')

  const fetch = async () => {
    try {
      const res = await api.get('/api/staff')
      setStaff(res.data || [])
    } catch (e) {}
  }

  useEffect(()=>{fetch()}, [])

  const create = async (e) => {
    e.preventDefault()
    try {
      const res = await api.post('/api/staff', { name })
      setStaff((s)=>[res.data, ...s])
      setName('')
    } catch (e){}
  }

  return (
    <DashboardLayout>
      <div className="grid md:grid-cols-2 gap-6">
        <div className="glass p-4">
          <h3 className="text-lg font-semibold mb-2">Create Staff</h3>
          <form onSubmit={create} className="space-y-3">
            <input value={name} onChange={e=>setName(e.target.value)} placeholder="Staff name" className="w-full p-2 rounded bg-slate-800 text-slate-100" />
            <Button type="submit">Create</Button>
          </form>
        </div>

        <div className="glass p-4">
          <h3 className="text-lg font-semibold mb-2">Staff</h3>
          <ul className="space-y-2">
            {staff.map(s=> (
              <li key={s.id} className="p-2 rounded bg-white/3">{s.name}</li>
            ))}
          </ul>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default Staff
