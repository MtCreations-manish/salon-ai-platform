export default function ChatBubble({
  type,
  text
}) {

  return (

    <div
      className={`max-w-[80%] rounded-2xl px-4 py-3 text-sm leading-7 ${
        type === "user"
          ? "ml-auto bg-indigo-500 text-white"
          : "bg-white/10 text-slate-100"
      }`}
    >

      {text}

    </div>
  );
}