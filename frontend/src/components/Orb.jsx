export default function Orb() {

  return (

    <div className="flex justify-center mt-10">

      <div className="relative">

        <div className="w-52 h-52 rounded-full bg-gradient-to-br from-blue-400 via-purple-500 to-pink-400 animate-pulse shadow-[0_0_80px_rgba(168,85,247,0.7)] flex items-center justify-center">

          <div className="flex gap-5">

            <div className="w-5 h-14 bg-white rounded-full"></div>

            <div className="w-5 h-14 bg-white rounded-full"></div>

          </div>

        </div>

      </div>

    </div>
  );
}