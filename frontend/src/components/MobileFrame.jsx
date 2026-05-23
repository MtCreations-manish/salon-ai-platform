export default function MobileFrame({
  children
}) {

  return (

    <div
      className="
      w-full
      max-w-[420px]

      sm:max-w-[480px]

      md:max-w-[700px]

      lg:max-w-[1100px]

      min-h-[85vh]

      rounded-[40px]

      border border-white/30

      bg-white/20

      backdrop-blur-2xl

      p-5 sm:p-8 lg:p-10

      shadow-[0_8px_32px_rgba(31,38,135,0.2)]

      transition-all duration-500
      "
    >

      {children}

    </div>
  );
}