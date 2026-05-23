const BASE_URL =
  "https://salon-ai-platform-4.onrender.com";
const SESSION_KEY = "ai_session_id";

function getSessionId() {
  if (typeof localStorage === "undefined") {
    return `ai-session-${Date.now()}`;
  }

  let sessionId = localStorage.getItem(SESSION_KEY);
  if (!sessionId) {
    sessionId = `ai-session-${Date.now()}`;
    localStorage.setItem(SESSION_KEY, sessionId);
  }

  return sessionId;
}

export async function sendMessage(
  message,
  sessionId
) {
  const effectiveSessionId = sessionId || getSessionId();

  const response =
    await fetch(
      `${BASE_URL}/api/ai/chat`,
      {
        method: "POST",

        headers: {
          "Content-Type":
            "application/json"
        },

        body: JSON.stringify({
          sessionId: effectiveSessionId,

          message
        })
      }
    );

  const data = await response.json();
  if (data?.sessionId) {
    localStorage.setItem(SESSION_KEY, data.sessionId);
  }

  return data;
}