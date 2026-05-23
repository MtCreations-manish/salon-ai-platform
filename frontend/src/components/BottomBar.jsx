export default function BottomBar({
  message,
  setMessage,
  handleSend
}) {

  return (

    <div className="mt-6 flex items-center gap-3 rounded-full border border-white/10 bg-white/5 px-4 py-3">

      <button className="flex h-11 w-11 items-center justify-center rounded-full bg-white/10 text-xl">
        +
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
        className="flex-1 bg-transparent outline-none text-slate-100 placeholder:text-slate-500"
      />

      <button
        onClick={handleSend}
        className="flex h-12 w-12 items-center justify-center rounded-full bg-gradient-to-br from-indigo-500 to-pink-500"
      >
        🎤
      </button>

    </div>
  );
}