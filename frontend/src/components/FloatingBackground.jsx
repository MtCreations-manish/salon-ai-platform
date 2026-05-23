export default function FloatingBackground() {

  return (

    <div className="absolute inset-0 overflow-hidden">

      <div className="absolute top-[-100px] left-[-80px] h-[400px] w-[400px] rounded-full bg-pink-300/40 blur-3xl"></div>

      <div className="absolute bottom-[-120px] right-[-80px] h-[420px] w-[420px] rounded-full bg-purple-300/40 blur-3xl"></div>

      <div className="absolute top-[30%] right-[20%] h-[250px] w-[250px] rounded-full bg-blue-300/30 blur-3xl"></div>

    </div>
  );
}