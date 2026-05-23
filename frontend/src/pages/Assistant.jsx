import React, {
  useEffect,
  useRef,
  useState,
  useCallback,
} from "react";

import { sendMessage } from "../services/api";

const quickActions = [
  "Book Haircut",
  "Find Salon",
  "Check Availability",
  "My Booking",
];

export default function Assistant() {

  const [message, setMessage] =
    useState("");

  const [loading, setLoading] =
    useState(false);

  const [chat, setChat] = useState([
    {
      id: Date.now(),
      type: "ai",
      text:
        "Hi Manish 👋 How can I help you today?",
    },
  ]);

  const chatEndRef = useRef(null);

  const inputRef = useRef(null);

  // =====================================================
  // AUTO SCROLL
  // =====================================================

  useEffect(() => {

    chatEndRef.current?.scrollIntoView({
      behavior: "smooth",
    });

  }, [chat]);

  // =====================================================
  // KEEP INPUT FOCUS
  // =====================================================

  useEffect(() => {

    inputRef.current?.focus();

  }, [chat]);

  // =====================================================
  // SEND MESSAGE
  // =====================================================

  const handleSend = useCallback(
    async (quickMessage = null) => {

      const finalMessage =
        quickMessage || message;

      if (!finalMessage.trim()) return;

      const userMessage = {
        id: Date.now(),
        type: "user",
        text: finalMessage,
      };

      setChat((prev) => [
        ...prev,
        userMessage,
      ]);

      setMessage("");

      setLoading(true);

      try {

        const res = await sendMessage(finalMessage);

        const aiMessage = {
          id: Date.now() + 1,
          type: "ai",
          text:
            res?.reply ||
            "No response from AI",
        };

        setChat((prev) => [
          ...prev,
          aiMessage,
        ]);

      } catch (error) {

        setChat((prev) => [
          ...prev,
          {
            id: Date.now() + 2,
            type: "ai",
            text:
              "❌ Backend connection failed",
          },
        ]);

      }

      setLoading(false);

    },
    [message]
  );

  // =====================================================
  // ENTER KEY
  // =====================================================

  const handleKeyDown = (e) => {

    if (e.key === "Enter") {

      handleSend();

    }

  };

  return (

    <div className="relative h-screen overflow-hidden bg-[#f7f1ff]">

      {/* BACKGROUND */}

      <div className="absolute left-[-120px] top-[100px] h-[350px] w-[350px] rounded-full bg-pink-300/30 blur-3xl" />

      <div className="absolute right-[100px] top-[250px] h-[300px] w-[300px] rounded-full bg-blue-300/20 blur-3xl" />

      <div className="relative z-10 flex h-screen">

        {/* SIDEBAR */}

        <div className="hidden lg:flex w-[280px] flex-col border-r border-white/20 bg-white/10 backdrop-blur-2xl p-6">

          <h2 className="text-5xl font-bold text-[#4b3475]">

            Salon AI

          </h2>

          <p className="mt-2 text-[#7a689e]">

            AI Powered Assistant

          </p>

          <div className="mt-10 space-y-4">

            {quickActions.map((item) => (

              <button
                key={item}
                onClick={() =>
                  handleSend(item)
                }
                className="w-full rounded-2xl bg-white/40 px-5 py-5 text-left text-lg text-[#5c4c89] shadow-lg backdrop-blur-xl"
              >

                {item}

              </button>

            ))}

          </div>

        </div>

        {/* MAIN */}

        <div className="flex-1 p-4 md:p-8 overflow-hidden">

          <div className="flex h-full flex-col rounded-[40px] border border-white/30 bg-white/20 backdrop-blur-2xl shadow-[0_8px_32px_rgba(31,38,135,0.2)]">

            {/* TOP */}

            <div className="flex items-center justify-between p-6">

              <button className="h-12 w-12 rounded-full bg-white/40 shadow-lg">

                ☰

              </button>

              <button className="h-12 w-12 rounded-full bg-white/40 shadow-lg">

                ⚙

              </button>

            </div>

            {/* HERO */}

            <div className="px-6 text-center">

              <h1 className="text-5xl md:text-7xl font-bold text-[#47306b]">

                Hello, Manish!

              </h1>

              <p className="mt-5 text-[#75659d] text-xl">

                How can I help you today?

              </p>

            </div>

            {/* ORB */}

            <div className="relative mx-auto mt-10 flex h-60 w-60 items-center justify-center rounded-full bg-gradient-to-br from-blue-400 via-purple-400 to-pink-400 shadow-[0_0_80px_rgba(192,132,252,0.6)]">

              <div className="flex gap-6">

                <div className="h-14 w-5 rounded-full bg-white" />

                <div className="h-14 w-5 rounded-full bg-white" />

              </div>

            </div>

            {/* CHAT */}

            <div className="flex-1 overflow-y-auto px-6 py-6 space-y-5">

              {chat.map((item) => (

                <div
                  key={item.id}
                  className={`max-w-[85%] rounded-3xl px-5 py-4 shadow-xl
                  
                  ${
                    item.type === "user"
                      ? "ml-auto bg-gradient-to-r from-violet-500 to-purple-500 text-white"
                      : "bg-white/60 text-[#4b3475]"
                  }
                `}
                >

                  <div className="whitespace-pre-line text-sm md:text-base">

                    {item.text}

                  </div>

                </div>

              ))}

              {loading && (

                <div className="max-w-[85%] rounded-3xl bg-white/60 px-5 py-4 shadow-xl text-[#4b3475]">

                  ✨ Thinking...

                </div>

              )}

              <div ref={chatEndRef} />

            </div>

            {/* INPUT */}

            <div className="p-6">

              <div className="flex items-center gap-3 rounded-full bg-white/60 p-3 shadow-2xl backdrop-blur-2xl">

                <button className="flex h-10 w-10 items-center justify-center rounded-full bg-white text-2xl text-black">

                  +

                </button>

                <input
                  ref={inputRef}
                  type="text"
                  placeholder="Ask me anything..."
                  value={message}
                  onChange={(e) =>
                    setMessage(
                      e.target.value
                    )
                  }
                  onKeyDown={
                    handleKeyDown
                  }
                  className="flex-1 bg-transparent text-[#4b3475] outline-none placeholder:text-[#7e6aa8]"
                />

                <button
                  onClick={() =>
                    handleSend()
                  }
                  className="flex h-12 w-12 items-center justify-center rounded-full bg-gradient-to-r from-pink-400 to-purple-500 text-white shadow-xl"
                >

                  🎤

                </button>

              </div>

            </div>

          </div>

        </div>

      </div>

    </div>

  );

}