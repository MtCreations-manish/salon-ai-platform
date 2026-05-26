import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "";
const SESSION_KEY = "ai_session_id";

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 12000,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

function getSessionId() {
  if (typeof localStorage === "undefined") {
    return `ai-session-${Date.now()}`;
  }

  let sessionId = localStorage.getItem(SESSION_KEY);
  if (!sessionId) {
    sessionId =
      typeof crypto !== "undefined" && crypto.randomUUID
        ? crypto.randomUUID()
        : `ai-session-${Date.now()}`;
    localStorage.setItem(SESSION_KEY, sessionId);
  }

  return sessionId;
}

export async function sendMessage(message, sessionId, salonId) {
  const effectiveSessionId = sessionId || getSessionId();
  const response = await api.post("/api/ai/chat", {
    sessionId: effectiveSessionId,
    salonId,
    message,
  });
  return response.data;
}

export default api;
