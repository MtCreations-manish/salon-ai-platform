import { Mic, Plus } from "lucide-react";

export default function InputBar({
  message,
  setMessage,
  handleSend
}) {

  return (

    <div className="flex items-center gap-3 rounded-full bg-white/40 backdrop-blur-xl border border-white/40 px-3 py-3 shadow-lg">

      <button className="flex h-12 w-12 items-center justify-center rounded-full bg-white/50">

        <Plus size={20} />

      </button>

      <input
        type="text"
        placeholder="Ask me anything..."
        value={message}
        onChange={(e) =>
          setMessage(e.target.value)
        }
        onKeyDown={(e) => {

          if (e.key === "Enter") {

            handleSend();
          }
        }}
        className="flex-1 bg-transparent outline-none text-[#4d3f74] placeholder:text-[#7d71a6]"
      />

      <button
        onClick={handleSend}
        className="flex h-14 w-14 items-center justify-center rounded-full bg-gradient-to-br from-purple-400 to-pink-400 text-white shadow-xl"
      >

        <Mic size={22} />

      </button>

    </div>
  );
}