export default function QuickAction({
  title
}) {

  return (

    <button className="rounded-3xl bg-white/40 backdrop-blur-xl border border-white/40 px-4 py-4 text-left text-[#5f4d8d] shadow-lg transition hover:scale-105">

      {title}

    </button>
  );
}