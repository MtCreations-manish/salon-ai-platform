import React, { useEffect, useState } from 'react'
import api from '../services/api'
import DashboardLayout from '../layouts/DashboardLayout'
import Button from '../components/Button'

const Bookings = () => {
  const [bookings, setBookings] = useState([])
  const [customer, setCustomer] = useState('')
  const [serviceId, setServiceId] = useState('')
  const [staffId, setStaffId] = useState('')
  const [time, setTime] = useState('')

  const fetch = async () => {
    try {
      const res = await api.get('/api/bookings')
      setBookings(res.data || [])
    } catch (e) {}
  }

  useEffect(()=>{fetch()}, [])

  const create = async (e) => {
    e.preventDefault()
    try {
      const res = await api.post('/api/bookings', { customer, serviceId, staffId, time })
      setBookings((b)=>[res.data, ...b])
      setCustomer('')
    } catch (e){}
  }

  return (
    <DashboardLayout>
      <div className="grid md:grid-cols-2 gap-6">
        <div className="glass p-4">
          <h3 className="text-lg font-semibold mb-2">Create Booking</h3>
          <form onSubmit={create} className="space-y-3">
            <input value={customer} onChange={e=>setCustomer(e.target.value)} placeholder="Customer name" className="w-full p-2 rounded bg-slate-800 text-slate-100" />
            <input value={time} onChange={e=>setTime(e.target.value)} placeholder="ISO datetime" className="w-full p-2 rounded bg-slate-800 text-slate-100" />
            <Button type="submit">Create booking</Button>
          </form>
        </div>

        <div className="glass p-4">
          <h3 className="text-lg font-semibold mb-2">Bookings</h3>
          <ul className="space-y-2">
            {bookings.map(b=> (
              <li key={b.id} className="p-2 rounded bg-white/3">
                <div className="font-medium">{b.customer}</div>
                <div className="text-sm text-slate-400">{b.time}</div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default Bookings
