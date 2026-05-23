import React, { useEffect, useState } from 'react'
import api from '../services/api'
import DashboardLayout from '../layouts/DashboardLayout'
import Button from '../components/Button'

const Salons = () => {
  const [salons, setSalons] = useState([])
  const [name, setName] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const fetch = async () => {
    setLoading(true)
    try {
      const res = await api.get('/api/salons')
      setSalons(res.data || [])
    } catch (err) {
      setError('Failed to load salons')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetch() }, [])

  const createSalon = async (e) => {
    e.preventDefault()
    try {
      const res = await api.post('/api/salons', { name })
      setSalons((s) => [res.data, ...s])
      setName('')
    } catch (err) {
      setError('Failed to create')
    }
  }

  return (
    <DashboardLayout>
      <div className="grid md:grid-cols-2 gap-6">
        <div className="glass p-4">
          <h3 className="text-lg font-semibold mb-2">Create Salon</h3>
          <form onSubmit={createSalon} className="space-y-3">
            <input value={name} onChange={(e)=>setName(e.target.value)} placeholder="Salon name" className="w-full p-2 rounded bg-slate-800 text-slate-100" />
            <div>
              <Button type="submit">Create</Button>
            </div>
          </form>
        </div>

        <div className="glass p-4">
          <h3 className="text-lg font-semibold mb-2">Salons</h3>
          {loading && <div className="text-sm text-slate-300">Loading...</div>}
          {error && <div className="text-sm text-red-400">{error}</div>}
          <ul className="space-y-2">
            {salons.map((s) => (
              <li key={s.id} className="p-2 rounded bg-white/3">{s.name}</li>
            ))}
          </ul>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default Salons
