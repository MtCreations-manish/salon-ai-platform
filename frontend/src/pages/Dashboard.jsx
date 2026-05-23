import React, {
  useEffect,
  useRef,
  useState
} from "react";
import { sendMessage } from "../services/api";

const Dashboard = () => {

  const [message, setMessage] =
    useState("");

  const [loading, setLoading] =
    useState(false);

  const [sessionId, setSessionId] =
    useState(null);

  const [chat, setChat] =
    useState([
      {
        id: 1,
        type: "ai",
        text:
          "Hi Manish 👋\n\nWhich city would you like to book in?"
      }
    ]);

  const chatEndRef =
    useRef(null);

  const inputRef =
    useRef(null);

  const quickActions = [
    "Book Haircut",
    "Find Salon",
    "Check Availability",
    "My Booking"
  ];

  // =====================================================
  // AUTO SCROLL
  // =====================================================

  useEffect(() => {

    if (chatEndRef.current) {

      chatEndRef.current.scrollIntoView({
        behavior: "smooth"
      });

    }

  }, [chat]);

  // =====================================================
  // INPUT FOCUS
  // =====================================================

  useEffect(() => {

    if (inputRef.current) {

      inputRef.current.focus();

    }

  }, [message, loading]);

  // =====================================================
  // SEND MESSAGE
  // =====================================================

  const handleSend = async (
    customMessage
  ) => {

    const finalMessage =
      customMessage || message;

    if (!finalMessage.trim())
      return;

    // USER MESSAGE

    const userMessage = {
      id: Date.now(),
      type: "user",
      text: finalMessage
    };

    setChat((prev) => [
      ...prev,
      userMessage
    ]);

    setMessage("");

    setLoading(true);

    try {

      const data = await sendMessage(finalMessage, sessionId);

      if (data?.sessionId) {
        setSessionId(data.sessionId);
      }

      // ======================================
      // AI RESPONSE
      // ======================================

      setChat((prev) => [
        ...prev,
        {
          id:
            Date.now() + 1,
          type: "ai",
          text:
            data.reply ||
            "No response from AI"
        }
      ]);

    } catch (error) {

      console.log(error);

      setChat((prev) => [
        ...prev,
        {
          id:
            Date.now() + 2,
          type: "ai",
          text:
            "❌ Backend connection failed"
        }
      ]);

    } finally {

      setLoading(false);

      setTimeout(() => {

        if (
          inputRef.current
        ) {

          inputRef.current.focus();

        }

      }, 100);

    }

  };

  // =====================================================
  // ENTER KEY
  // =====================================================

  const handleKeyDown = (
    e
  ) => {

    if (e.key === "Enter") {

      e.preventDefault();

      handleSend();

    }

  };

  // =====================================================
  // UI
  // =====================================================

  return (

    <div className="flex h-screen overflow-hidden bg-[#f6efff]">

      {/* SIDEBAR */}

      <div className="hidden lg:flex w-[260px] flex-col border-r border-white/20 bg-[#f3dff1]/60 backdrop-blur-2xl p-6 shrink-0">

        <h1 className="text-6xl font-bold text-[#4b2d7f]">
          Salon AI
        </h1>

        <p className="mt-2 text-[#6d5b92] text-lg">
          AI Powered Assistant
        </p>

        <div className="mt-10 space-y-5">

          {quickActions.map(
            (item) => (

              <button
                key={item}
                onClick={() =>
                  handleSend(
                    item
                  )
                }
                className="
                  w-full
                  rounded-3xl
                  bg-white/70
                  px-6
                  py-6
                  text-left
                  text-2xl
                  font-medium
                  text-[#55408c]
                  shadow-lg
                  transition-all
                  duration-200
                  hover:scale-[1.02]
                  hover:bg-white
                "
              >

                {item}

              </button>

            )
          )}

        </div>

      </div>

      {/* MAIN */}

      <div className="flex-1 p-4 md:p-8 h-screen overflow-hidden">

        <div
          className="
            h-full
            rounded-[40px]
            border
            border-white/30
            bg-white/20
            backdrop-blur-2xl
            shadow-[0_8px_32px_rgba(31,38,135,0.2)]
            flex
            flex-col
            overflow-hidden
          "
        >

          {/* HEADER */}

          <div className="flex items-center justify-between p-6 shrink-0">

            <button
              className="
                h-14
                w-14
                rounded-full
                bg-white/60
                shadow-lg
                text-black
                text-xl
              "
            >

              ☰

            </button>

            <button
              className="
                h-14
                w-14
                rounded-full
                bg-white/60
                shadow-lg
                text-black
                text-xl
              "
            >

              ⚙

            </button>

          </div>

          {/* HERO */}

          <div className="text-center shrink-0 px-4">

            <h1
              className="
                text-6xl
                md:text-8xl
                font-bold
                text-[#4b2d7f]
              "
            >

              Hello, Manish!

            </h1>

            <p
              className="
                mt-4
                text-[#6d5b92]
                text-2xl
              "
            >

              How can I help you today?

            </p>

          </div>

          {/* AI ORB */}

          <div className="flex justify-center mt-8 shrink-0">

            <div
              className="
                relative
                h-[240px]
                w-[240px]
                rounded-full
                bg-gradient-to-br
                from-blue-400
                via-purple-400
                to-pink-400
                shadow-[0_0_80px_rgba(192,132,252,0.6)]
                flex
                items-center
                justify-center
              "
            >

              <div className="flex gap-5">

                <div className="h-16 w-5 rounded-full bg-white" />

                <div className="h-16 w-5 rounded-full bg-white" />

              </div>

            </div>

          </div>

          {/* CHAT */}

          <div
            className="
              flex-1
              overflow-y-auto
              px-6
              py-6
              min-h-0
            "
          >

            <div className="space-y-5">

              {chat.map(
                (item) => (

                  <div
                    key={item.id}
                    className={`w-full flex ${
                      item.type ===
                      "user"
                        ? "justify-end"
                        : "justify-start"
                    }`}
                  >

                    <div
                      className={`
                        max-w-[85%]
                        rounded-3xl
                        px-5
                        py-4
                        shadow-xl
                        whitespace-pre-wrap
                        break-words
                        text-lg
                        
                        ${
                          item.type ===
                          "user"
                            ? "bg-gradient-to-r from-violet-500 to-purple-500 text-white"
                            : "bg-white/70 text-[#4b3475]"
                        }
                      `}
                    >

                      {item.text}

                    </div>

                  </div>

                )
              )}

              {loading && (

                <div className="flex justify-start">

                  <div
                    className="
                      max-w-[85%]
                      rounded-3xl
                      bg-white/70
                      px-5
                      py-4
                      shadow-xl
                      text-[#4b3475]
                    "
                  >

                    ✨ Thinking...

                  </div>

                </div>

              )}

              <div
                ref={
                  chatEndRef
                }
              />

            </div>

          </div>

          {/* INPUT */}

          <div className="p-6 shrink-0">

            <div
              className="
                flex
                items-center
                gap-3
                rounded-full
                bg-white/70
                p-3
                shadow-2xl
                backdrop-blur-2xl
              "
            >

              <button
                className="
                  flex
                  h-12
                  w-12
                  items-center
                  justify-center
                  rounded-full
                  bg-white
                  text-3xl
                  text-black
                  shrink-0
                "
              >

                +

              </button>

              <input
                ref={inputRef}
                type="text"
                value={message}
                placeholder="Ask me anything..."
                onChange={(e) =>
                  setMessage(
                    e.target.value
                  )
                }
                onKeyDown={
                  handleKeyDown
                }
                className="
                  flex-1
                  bg-transparent
                  outline-none
                  text-xl
                  text-[#4b3475]
                  placeholder:text-[#7e6aa8]
                "
              />

              <button
                onClick={() =>
                  handleSend()
                }
                className="
                  flex
                  h-14
                  w-14
                  items-center
                  justify-center
                  rounded-full
                  bg-gradient-to-r
                  from-pink-400
                  to-purple-500
                  text-white
                  shadow-xl
                  shrink-0
                "
              >

                🎤

              </button>

            </div>

          </div>

        </div>

      </div>

    </div>

  );

};

export default Dashboard;