import { motion } from "framer-motion";

export default function AiOrb() {

  return (

    <div className="flex justify-center mt-10">

      <motion.div
        animate={{
          scale: [1, 1.05, 1]
        }}
        transition={{
          duration: 3,
          repeat: Infinity
        }}
        className="relative"
      >

        <div className="w-44 h-44 md:w-64 md:h-64 rounded-full bg-gradient-to-br from-blue-400 via-purple-400 to-pink-400 shadow-[0_0_80px_rgba(168,85,247,0.6)] flex items-center justify-center">

          <div className="flex gap-5">

            <div className="w-5 h-14 bg-white rounded-full"></div>

            <div className="w-5 h-14 bg-white rounded-full"></div>

          </div>

        </div>

        <div className="absolute -bottom-6 left-1/2 -translate-x-1/2 h-10 w-40 rounded-full bg-purple-300/40 blur-2xl"></div>

      </motion.div>

    </div>
  );
}