import React, { useEffect, useState } from 'react'
import api from '../services/api'
import DashboardLayout from '../layouts/DashboardLayout'
import Button from '../components/Button'

const Services = () => {
  const [services, setServices] = useState([])
  const [title, setTitle] = useState('')
  const [duration, setDuration] = useState(30)
  const [price, setPrice] = useState(20)

  const fetch = async () => {
    try {
      const res = await api.get('/api/services')
      setServices(res.data || [])
    } catch (err) {
      // ignore
    }
  }

  useEffect(()=>{fetch()}, [])

  const create = async (e) => {
    e.preventDefault()
    try {
      const res = await api.post('/api/services', { title, duration, price })
      setServices((s)=>[res.data, ...s])
      setTitle('')
    } catch(e){}
  }

  return (
    <DashboardLayout>
      <div className="grid md:grid-cols-2 gap-6">
        <div className="glass p-4">
          <h3 className="text-lg font-semibold mb-2">Create Service</h3>
          <form onSubmit={create} className="space-y-3">
            <input value={title} onChange={e=>setTitle(e.target.value)} placeholder="Service name" className="w-full p-2 rounded bg-slate-800 text-slate-100" />
            <div className="flex gap-2">
              <input value={duration} onChange={e=>setDuration(e.target.value)} type="number" className="p-2 rounded bg-slate-800 text-slate-100 w-1/2" />
              <input value={price} onChange={e=>setPrice(e.target.value)} type="number" className="p-2 rounded bg-slate-800 text-slate-100 w-1/2" />
            </div>
            <Button type="submit">Create service</Button>
          </form>
        </div>

        <div className="glass p-4">
          <h3 className="text-lg font-semibold mb-2">Services</h3>
          <ul className="space-y-2">
            {services.map(s=> (
              <li key={s.id} className="p-2 rounded bg-white/3 flex justify-between">
                <div>
                  <div className="font-medium">{s.title}</div>
                  <div className="text-sm text-slate-400">{s.duration} mins • ${s.price}</div>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default Services
