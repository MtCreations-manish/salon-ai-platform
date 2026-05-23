import React, {
  useEffect,
  useRef,
  useState,
} from "react";

import { sendMessage } from "../services/api";

// ======================================================
// QUICK ACTIONS
// ======================================================

const quickActions = [
  "Book Haircut",
  "Find Salon",
  "Check Availability",
  "My Booking",
];

// ======================================================
// GLASS CARD
// ======================================================

const GlassCard = ({
  type,
  text,
}) => {

  return (

    <div
      className={`max-w-[85%] rounded-3xl px-5 py-4 shadow-xl backdrop-blur-xl animate-fadeIn
        
        ${
          type === "user"
            ? "ml-auto bg-gradient-to-r from-violet-500 to-purple-500 text-white"
            : "bg-white/60 text-[#4b3475]"
        }
      `}
    >

      <div className="whitespace-pre-line text-sm md:text-base">

        {text}

      </div>

    </div>

  );

};

// ======================================================
// QUICK ACTION BUTTON
// ======================================================

const QuickAction = ({
  title,
  onClick,
}) => {

  return (

    <button
      onClick={onClick}
      className="rounded-2xl bg-white/40 p-4 text-left text-[#5c4c89] shadow-lg backdrop-blur-xl transition-all hover:scale-[1.02]"
    >

      {title}

    </button>

  );

};

// ======================================================
// AI ORB
// ======================================================

const AiOrb = () => {

  return (

    <div className="relative mx-auto mt-10 flex h-60 w-60 items-center justify-center rounded-full bg-gradient-to-br from-blue-400 via-purple-400 to-pink-400 shadow-[0_0_80px_rgba(192,132,252,0.6)] animate-pulse">

      <div className="flex gap-6">

        <div className="h-14 w-5 rounded-full bg-white" />

        <div className="h-14 w-5 rounded-full bg-white" />

      </div>

    </div>

  );

};

// ======================================================
// BACKGROUND
// ======================================================

const FloatingBackground = () => {

  return (

    <>
      <div className="absolute left-[-120px] top-[100px] h-[350px] w-[350px] rounded-full bg-pink-300/30 blur-3xl" />

      <div className="absolute right-[100px] top-[250px] h-[300px] w-[300px] rounded-full bg-blue-300/20 blur-3xl" />

      <div className="absolute bottom-[50px] right-[250px] h-[250px] w-[250px] rounded-full bg-purple-300/20 blur-3xl" />
    </>

  );

};

// ======================================================
// MAIN COMPONENT
// ======================================================

export default function Assistant() {

  const [message, setMessage] =
    useState("");

  const [loading, setLoading] =
    useState(false);

  const [chat, setChat] = useState([
    {
      type: "ai",
      text:
        "Hi Manish 👋 How can I help you today?",
    },
  ]);

  const chatEndRef = useRef(null);

  // ======================================================
  // AUTO SCROLL
  // ======================================================

  useEffect(() => {

    if (chatEndRef.current) {

      chatEndRef.current.scrollIntoView({
        behavior: "smooth",
      });

    }

  }, [chat]);

  // ======================================================
  // SEND MESSAGE
  // ======================================================

  const handleSend = async (
    quickMessage = null
  ) => {

    const finalMessage =
      quickMessage || message;

    if (!finalMessage.trim()) return;

    // USER MESSAGE

    setChat((prev) => [
      ...prev,
      {
        type: "user",
        text: finalMessage,
      },
    ]);

    setMessage("");

    setLoading(true);

    try {

      const res = await sendMessage(finalMessage);

      setChat((prev) => [
        ...prev,
        {
          type: "ai",
          text:
            res?.reply ||
            "No response from AI",
        },
      ]);

    } catch (error) {

      console.log(error);

      setChat((prev) => [
        ...prev,
        {
          type: "ai",
          text:
            "❌ Backend connection failed",
        },
      ]);

    }

    setLoading(false);

  };

  // ======================================================
  // ENTER KEY
  // ======================================================

  const handleKeyDown = (e) => {

    if (e.key === "Enter") {

      handleSend();

    }

  };

  // ======================================================
  // UI
  // ======================================================

  return (

    <div className="relative h-screen overflow-hidden bg-[#f7f1ff]">

      <FloatingBackground />

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
                className="w-full rounded-2xl bg-white/40 px-5 py-5 text-left text-lg text-[#5c4c89] shadow-lg backdrop-blur-xl transition-all hover:scale-[1.02]"
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

              <button className="h-12 w-12 rounded-full bg-white/40 shadow-lg backdrop-blur-xl">

                ☰

              </button>

              <button className="h-12 w-12 rounded-full bg-white/40 shadow-lg backdrop-blur-xl">

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

            <AiOrb />

            {/* MOBILE QUICK ACTION */}

            <div className="grid grid-cols-2 gap-4 px-6 lg:hidden">

              {quickActions.map((item) => (

                <QuickAction
                  key={item}
                  title={item}
                  onClick={() =>
                    handleSend(item)
                  }
                />

              ))}

            </div>

            {/* CHAT */}

            <div className="flex-1 overflow-y-auto px-6 py-6 space-y-5">

              {chat.map((item, index) => (

                <GlassCard
                  key={index}
                  type={item.type}
                  text={item.text}
                />

              ))}

              {loading && (

                <GlassCard
                  type="ai"
                  text="✨ Thinking..."
                />

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