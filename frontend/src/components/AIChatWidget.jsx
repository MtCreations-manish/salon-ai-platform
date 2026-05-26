import React, { useEffect, useRef, useState } from 'react'
import { MessageCircle, Send, Sparkles, X } from 'lucide-react'
import { sendMessage } from '../services/api'

const ChatBubble = ({ from, text, options = [], onOptionClick }) => {
  const isAI = from === 'ai'
  return (
    <div className={`max-w-[80%] ${isAI ? 'self-start' : 'self-end'} mb-2`}> 
      <div
        className={`whitespace-pre-line rounded-lg px-4 py-3 text-sm leading-6 shadow-sm ${
          isAI
            ? 'bg-[#f9efe1] text-[#221d18]'
            : 'bg-[#d6a65d] text-[#15120f]'
        }`}
      >
        {text}
      </div>
      {isAI && options.length > 0 && (
        <div className="mt-2 flex flex-wrap gap-2">
          {options.map((option) => (
            <button
              key={option}
              onClick={() => onOptionClick(option)}
              className="rounded-full border border-[#d6a65d]/40 bg-[#221d18] px-3 py-1.5 text-xs font-semibold text-[#f9efe1] transition hover:bg-[#2d261f]"
            >
              {option}
            </button>
          ))}
        </div>
      )}
    </div>
  )
}

const AIChatWidget = ({ salonId }) => {
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

  const handleSend = async (overrideText = '') => {
    const finalInput = overrideText || input
    if (!finalInput.trim()) return
    const userMsg = { from: 'user', text: finalInput }
    setMessages((m) => [...m, userMsg])
    setInput('')
    setLoading(true)
    try {
      const res = await sendMessage(userMsg.text, sessionId, salonId)
      if (res.sessionId && res.sessionId !== sessionId) {
        setSessionId(res.sessionId)
        localStorage.setItem('ai_session_id', res.sessionId)
      }
      const aiText =
        res.reply || res.message || res.data?.response || 'No response'
      setMessages((m) => [...m, { from: 'ai', text: aiText, options: res.options || [] }])
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
    <div className="fixed inset-x-4 bottom-5 z-[100] flex justify-end pointer-events-none md:inset-x-auto md:right-5 md:bottom-6">
      <div className="flex w-full max-w-[430px] flex-col items-end pointer-events-auto">
        <button
          onClick={() => setOpen((s) => !s)}
          className="inline-flex min-h-14 items-center justify-center gap-2 rounded-full bg-[#15120f] px-5 text-sm font-semibold text-[#f9efe1] shadow-2xl shadow-black/25 transition hover:bg-[#2a241f] active:scale-[0.98]"
          aria-label="Toggle AI chat"
          aria-expanded={open}
        >
          <Sparkles className="h-4 w-4 text-[#d6a65d]" />
          Book with AI
          <MessageCircle className="h-4 w-4" />
        </button>

        <div className={`mt-3 w-full ${open ? 'block' : 'hidden'}`}>
          <div className="flex h-[78vh] max-h-[560px] min-h-[420px] flex-col rounded-lg border border-[#2b241e]/10 bg-[#15120f] p-4 shadow-2xl shadow-black/30">
            <div className="mb-3 flex items-center justify-between border-b border-[#f9efe1]/10 pb-3">
              <div>
                <p className="text-sm font-semibold text-[#f9efe1]">Salon AI concierge</p>
                <p className="text-xs text-[#b8aa97]">Fast booking, staff assignment, live slots.</p>
              </div>
              <button
                onClick={() => setOpen(false)}
                className="inline-flex h-9 w-9 items-center justify-center rounded-full border border-[#f9efe1]/10 bg-[#221d18] text-[#f9efe1] transition hover:bg-[#2d261f]"
                aria-label="Close chat widget"
              >
                <X className="h-4 w-4" />
              </button>
            </div>

            <div ref={listRef} className="flex-1 overflow-y-auto space-y-3 pr-1 custom-scrollbar">
              {messages.length === 0 ? (
                <div className="rounded-lg bg-[#221d18] p-4 text-sm leading-6 text-[#d8cabb]">
                  Hi, I can reserve your salon appointment. Tell me the service and preferred time.
                </div>
              ) : (
                messages.map((m, idx) => (
                  <ChatBubble
                    key={idx}
                    from={m.from}
                    text={m.text}
                    options={m.options}
                    onOptionClick={(option) => handleSend(option)}
                  />
                ))
              )}
            </div>

            <div className="mt-3 border-t border-[#f9efe1]/10 pt-3">
              <textarea
                rows={2}
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={handleKeyDown}
                placeholder="Book haircut tomorrow at 5 PM"
                className="w-full rounded-lg border border-[#f9efe1]/10 bg-[#0f0d0b] px-4 py-3 text-[#f9efe1] outline-none transition placeholder:text-[#8f8170] focus:border-[#d6a65d] focus:ring-2 focus:ring-[#d6a65d]/20"
              />
              <div className="mt-3 flex items-center justify-between gap-3">
                <div className="text-xs text-[#b8aa97]">{salonId ? 'Salon selected' : 'Marketplace booking'}</div>
                <button
                  onClick={handleSend}
                  className="inline-flex items-center justify-center gap-2 rounded-lg bg-[#d6a65d] px-4 py-2 text-sm font-semibold text-[#15120f] transition hover:bg-[#e4b96d] disabled:cursor-not-allowed disabled:opacity-60"
                  disabled={loading}
                >
                  {loading ? 'Thinking...' : 'Send'}
                  <Send className="h-4 w-4" />
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
