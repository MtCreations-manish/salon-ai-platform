import React, { useState } from 'react'
import api from '../services/api'

const Diagnostics = () => {
  const [loading, setLoading] = useState(false)
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)

  const run = async () => {
    setLoading(true)
    setError(null)
    setData(null)
    try {
      const res = await api.get('/api/salons')
      setData(res.data)
    } catch (err) {
      // axios error handling
      if (err.response) {
        setError({ type: 'response', status: err.response.status, data: err.response.data })
      } else if (err.request) {
        setError({ type: 'request', message: 'No response received (possible CORS or network error)', detail: err.message })
      } else {
        setError({ type: 'other', message: err.message })
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen p-6">
      <div className="max-w-3xl mx-auto glass p-6">
        <h2 className="text-xl font-semibold mb-2">Backend Diagnostics</h2>
        <p className="text-sm text-slate-300 mb-4">Checks connectivity to <code>/api/salons</code> and shows errors (including CORS).</p>

        <div className="flex gap-2 mb-4">
          <button onClick={run} className="px-4 py-2 bg-indigo-600 text-white rounded" disabled={loading}>
            {loading ? 'Running...' : 'Run diagnostics'}
          </button>
        </div>

        <div>
          {error && (
            <div className="bg-red-800 p-3 rounded text-sm">
              <div className="font-medium">Error</div>
              <pre className="mt-2 text-xs">{JSON.stringify(error, null, 2)}</pre>
            </div>
          )}

          {data && (
            <div className="bg-slate-800 p-3 rounded text-sm">
              <div className="font-medium">Response</div>
              <pre className="mt-2 text-xs">{JSON.stringify(data, null, 2)}</pre>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default Diagnostics
