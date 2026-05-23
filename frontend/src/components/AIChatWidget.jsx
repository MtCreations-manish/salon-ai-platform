import React, { useEffect, useRef, useState } from 'react'
import { sendMessage } from '../services/api'

const ChatBubble = ({ from, text }) => {
  const isAI = from === 'ai'
  return (
    <div className={`max-w-[80%] ${isAI ? 'self-start' : 'self-end'} mb-2`}> 
      <div className={`${isAI ? 'ai-bubble-ai' : 'ai-bubble-user'} px-4 py-2 rounded-lg shadow-sm`}>{text}</div>
    </div>
  )
}

const AIChatWidget = () => {
  const [open, setOpen] = useState(false)
  const [messages, setMessages] = useState([])
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(false)
  const [sessionId, setSessionId] = useState(() => {
    if (typeof localStorage === 'undefined') {
      return `ai-session-${Date.now()}`
    }

    const existing = localStorage.getItem('ai_session_id')

    if (existing) {
      return existing
    }

    const newId =
      typeof crypto !== 'undefined' && crypto.randomUUID
        ? crypto.randomUUID()
        : `ai-session-${Date.now()}`

    localStorage.setItem('ai_session_id', newId)

    return newId
  })

  const listRef = useRef(null)

  useEffect(() => {
    if (!open) return
    // optional: load cached history from localStorage
    const cache = localStorage.getItem('ai_history')
    if (cache) setMessages(JSON.parse(cache))
  }, [open])

  useEffect(() => {
    localStorage.setItem('ai_history', JSON.stringify(messages))
    // auto-scroll
    if (listRef.current) {
      listRef.current.scrollTop = listRef.current.scrollHeight
    }
  }, [messages])

  const handleSend = async () => {
    if (!input.trim()) return
    const userMsg = { from: 'user', text: input }
    setMessages((m) => [...m, userMsg])
    setInput('')
    setLoading(true)
    try {
      const res = await sendMessage(userMsg.text, sessionId)
      const aiText =
        res.reply || res.message || res.data?.response || 'No response'
      setMessages((m) => [...m, { from: 'ai', text: aiText }])
    } catch (err) {
      setMessages((m) => [...m, { from: 'ai', text: 'Error: failed to reach AI service.' }])
    } finally {
      setLoading(false)
    }
  }

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  return (
    <div className="fixed inset-x-4 bottom-4 z-50 flex justify-end md:inset-x-auto md:right-4 md:bottom-6">
      <div className="flex flex-col items-end">
        <button
          onClick={() => setOpen((s) => !s)}
          className="inline-flex h-14 w-14 items-center justify-center rounded-full bg-indigo-600 shadow-2xl shadow-indigo-500/20 text-base font-semibold text-white transition hover:bg-indigo-500"
          aria-label="Toggle AI chat"
          aria-expanded={open}
        >
          AI
        </button>

        <div className={`mt-3 w-full max-w-lg ${open ? 'block' : 'hidden'}`}>
          <div className="glass-2 flex h-[88vw] max-h-[520px] flex-col rounded-[28px] border border-white/10 p-4 shadow-2xl md:h-[420px]">
            <div className="mb-3 flex items-center justify-between border-b border-white/10 pb-3">
              <div>
                <p className="text-sm font-semibold text-white">Salon AI</p>
                <p className="text-xs text-slate-400">Tap Enter to send, Shift+Enter for a new line.</p>
              </div>
              <button
                onClick={() => setOpen(false)}
                className="rounded-full border border-white/10 bg-slate-900/80 px-2 py-1 text-xs text-slate-300 transition hover:bg-slate-800"
                aria-label="Close chat widget"
              >
                Close
              </button>
            </div>

            <div ref={listRef} className="flex-1 overflow-y-auto space-y-3 pr-1 custom-scrollbar">
              {messages.length === 0 ? (
                <div className="text-sm text-slate-400">Ask the AI assistant to help book a slot.</div>
              ) : (
                messages.map((m, idx) => <ChatBubble key={idx} from={m.from} text={m.text} />)
              )}
            </div>

            <div className="mt-3 border-t border-white/10 pt-3">
              <textarea
                rows={2}
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={handleKeyDown}
                placeholder="Try: Book beard trim tomorrow at 5 PM"
                className="w-full rounded-3xl border border-slate-700 bg-slate-950 px-4 py-3 text-slate-100 outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/20"
              />
              <div className="mt-3 flex items-center justify-between gap-3">
                <div className="text-xs text-slate-400">Enter to send</div>
                <button
                  onClick={handleSend}
                  className="inline-flex items-center justify-center rounded-3xl bg-indigo-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-indigo-500 disabled:cursor-not-allowed disabled:opacity-60"
                  disabled={loading}
                >
                  {loading ? 'Thinking…' : 'Send'}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AIChatWidget
