import { useState } from "react";

import Orb from "../components/Orb";
import ChatBubble from "../components/ChatBubble";
import BottomBar from "../components/BottomBar";
import QuickActions from "../components/QuickActions";

import { sendMessage } from "../services/api";

export default function Chat() {

  const [message, setMessage] =
    useState("");

  const [chat, setChat] =
    useState([]);

  const [loading, setLoading] =
    useState(false);

  const handleSend = async () => {

    if (!message.trim()) {
      return;
    }

    const userMessage = {
      type: "user",
      text: message
    };

    setChat((prev) => [
      ...prev,
      userMessage
    ]);

    setLoading(true);

    try {

      const response =
        await sendMessage(message);

      const aiMessage =
        response.userMessage ||
        response.message ||
        JSON.stringify(response);

      setChat((prev) => [
        ...prev,
        {
          type: "ai",
          text: aiMessage
        }
      ]);

    } catch (error) {

      setChat((prev) => [
        ...prev,
        {
          type: "ai",
          text:
            "Backend connection failed"
        }
      ]);

    } finally {

      setLoading(false);

      setMessage("");
    }
  };

  return (

    <div className="min-h-screen bg-[radial-gradient(circle_at_top_left,_rgba(99,102,241,0.16),_transparent_25%),radial-gradient(circle_at_bottom_right,_rgba(236,72,153,0.14),_transparent_20%)] bg-slate-950 text-white">

      <div className="mx-auto flex min-h-screen max-w-7xl flex-col px-4 py-10 sm:px-6 lg:px-8">

        <div className="grid gap-10 lg:grid-cols-[1fr_0.75fr] items-center">

          {/* LEFT */}

          <div className="space-y-6">

            <span className="inline-flex rounded-full border border-indigo-300/25 bg-white/10 px-4 py-1 text-xs uppercase tracking-[0.3em] text-indigo-100">
              Salon AI Assistant
            </span>

            <h1 className="text-4xl font-semibold leading-tight sm:text-5xl">
              Hello, How can I help you today?
            </h1>

            <p className="max-w-xl text-slate-300 leading-8 sm:text-lg">
              Book appointments, ask availability,
              discover salons, and manage your
              bookings using AI.
            </p>

            <QuickActions />

          </div>

          {/* RIGHT */}

          <div className="rounded-[44px] border border-white/10 bg-slate-900/80 p-5 shadow-[0_40px_120px_rgba(15,23,42,0.8)] backdrop-blur">

            <div className="flex items-center justify-between rounded-[32px] border border-white/10 bg-slate-900/80 px-5 py-4">

              <div>
                <h2 className="text-lg font-semibold">
                  AI Assistant
                </h2>

                <p className="text-sm text-slate-400">
                  Connected to backend
                </p>
              </div>

              <div className="h-3 w-3 rounded-full bg-emerald-400"></div>

            </div>

            <Orb />

            <div className="mt-8 h-[300px] overflow-y-auto space-y-4 px-1">

              {chat.map((item, index) => (

                <ChatBubble
                  key={index}
                  type={item.type}
                  text={item.text}
                />

              ))}

              {loading && (

                <div className="rounded-2xl bg-white/10 px-4 py-3 text-sm text-slate-300 w-fit">
                  Thinking...
                </div>

              )}

            </div>

            <BottomBar
              message={message}
              setMessage={setMessage}
              handleSend={handleSend}
            />

          </div>

        </div>

      </div>

    </div>
  );
}