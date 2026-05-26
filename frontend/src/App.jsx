import { BrowserRouter, Link, Route, Routes, useParams } from "react-router-dom";
import {
  Bell,
  CalendarClock,
  CheckCircle2,
  Clock,
  MapPin,
  Scissors,
  Sparkles,
  Users,
} from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import AIChatWidget from "./components/AIChatWidget";
import api from "./services/api";

const shell =
  "min-h-screen overflow-y-auto bg-[#efe3d2] text-[#17130f]";
const panel =
  "rounded-lg border border-[#2b241e]/10 bg-[#fff9f0] shadow-sm";
const input =
  "w-full rounded-lg border border-[#2b241e]/10 bg-[#fff9f0] px-3 py-2 text-sm outline-none transition focus:border-[#b88643] focus:ring-2 focus:ring-[#b88643]/10";

function AppNav() {
  return (
    <header className="sticky top-0 z-40 border-b border-[#2b241e]/10 bg-[#efe3d2]/95 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-4 py-3">
        <Link to="/" className="flex items-center gap-2 font-semibold">
          <span className="inline-flex h-9 w-9 items-center justify-center rounded-full bg-[#17130f] text-[#d6a65d]">
            <Scissors className="h-4 w-4" />
          </span>
          Salon AI
        </Link>
        <nav className="flex items-center gap-2 text-sm">
          <Link className="rounded-lg px-3 py-2 hover:bg-[#fff9f0]" to="/">
            Marketplace
          </Link>
          <Link className="rounded-lg px-3 py-2 hover:bg-[#fff9f0]" to="/admin">
            Manager
          </Link>
        </nav>
      </div>
    </header>
  );
}

function StatusPill({ open }) {
  return (
    <span
      className={`inline-flex items-center gap-1 rounded-full px-2.5 py-1 text-xs font-medium ${
        open ? "bg-[#e8f1df] text-[#526b3f]" : "bg-[#f3d9d1] text-[#8d3f2d]"
      }`}
    >
      <span className="h-1.5 w-1.5 rounded-full bg-current" />
      {open ? "Open" : "Closed"}
    </span>
  );
}

function HomePage() {
  const [salons, setSalons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadSalons() {
      try {
        const res = await api.get("/api/salons/marketplace");
        setSalons(res.data || []);
      } catch {
        try {
          const fallback = await api.get("/api/salons");
          setSalons((fallback.data || []).map(normalizeSalonCard));
        } catch {
          setError("Could not load salons. Check VITE_API_BASE_URL.");
        }
      } finally {
        setLoading(false);
      }
    }

    loadSalons();
  }, []);

  return (
    <main className="mx-auto max-w-7xl px-4 pb-24 pt-5">
      <section className="mb-5 overflow-hidden rounded-lg bg-[#17130f] text-[#fff9f0] shadow-xl shadow-[#2b241e]/20">
        <div className="grid gap-5 p-5 md:grid-cols-[1.1fr_.9fr] md:p-8">
          <div className="flex flex-col justify-between gap-8">
            <div>
              <p className="text-sm font-medium text-[#d6a65d]">Beauty appointments near you</p>
              <h1 className="mt-3 max-w-3xl text-4xl font-semibold tracking-normal md:text-6xl">
                Find your next salon chair.
              </h1>
            </div>
            <div className="flex flex-wrap gap-2 text-xs">
              <span className="rounded-full bg-[#fff9f0]/10 px-3 py-1.5">Live slots</span>
              <span className="rounded-full bg-[#fff9f0]/10 px-3 py-1.5">AI booking</span>
              <span className="rounded-full bg-[#fff9f0]/10 px-3 py-1.5">Staff matched</span>
            </div>
          </div>
          <div className="min-h-56 overflow-hidden rounded-lg bg-[#2a241f]">
            <img
              src="https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?auto=format&fit=crop&w=1200&q=80"
              alt="Salon interior"
              className="h-full min-h-56 w-full object-cover"
            />
          </div>
        </div>
      </section>

      <section className="mb-4 flex items-end justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-[#8b6638]">One area supported</p>
          <h2 className="mt-1 text-2xl font-semibold">Available salons</h2>
        </div>
        <p className="hidden max-w-sm text-sm leading-6 text-[#6f6254] sm:block">
          Pick a salon or start the floating AI concierge.
        </p>
      </section>

      {loading && <div className={panel + " p-6"}>Loading salons...</div>}
      {error && <div className="rounded-lg bg-[#f3d9d1] p-4 text-sm text-[#8d3f2d]">{error}</div>}

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {salons.map((salon) => (
          <Link key={salon.id} to={`/salons/${salon.id}`} className="group overflow-hidden rounded-lg border border-[#2b241e]/10 bg-[#fff9f0] shadow-sm transition hover:-translate-y-0.5 hover:shadow-xl hover:shadow-[#2b241e]/10">
            <div className="relative">
              <img
                src={salon.imageUrl || "https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=1200&q=80"}
                alt={salon.name}
                className="h-56 w-full object-cover transition duration-300 group-hover:scale-[1.03]"
              />
              <div className="absolute left-3 top-3">
                <StatusPill open={salon.open} />
              </div>
              <div className="absolute bottom-3 right-3 rounded-full bg-[#17130f] px-3 py-1.5 text-xs font-semibold text-[#d6a65d]">
                ★ 4.8
              </div>
            </div>
            <div className="space-y-4 p-4">
              <div>
                <h2 className="text-xl font-semibold">{salon.name}</h2>
                <p className="mt-1 flex items-center gap-1 text-sm text-[#6f6254]">
                  <MapPin className="h-4 w-4 text-[#b88643]" />
                  {salon.area || salon.city}
                </p>
              </div>
              <div className="grid grid-cols-2 gap-2 text-sm">
                <div className="rounded-lg bg-[#f3e7d6] p-3">
                  <p className="text-[#756859]">Slots</p>
                  <p className="font-semibold">{salon.availableSlots} available</p>
                </div>
                <div className="rounded-lg bg-[#e8f1df] p-3">
                  <p className="text-[#60704f]">Wait</p>
                  <p className="font-semibold">{salon.estimatedWaitTime}</p>
                </div>
              </div>
              <div className="flex flex-wrap gap-2">
                {(salon.services || []).slice(0, 3).map((service) => (
                  <span key={service.id} className="rounded-full bg-[#17130f] px-2.5 py-1 text-xs text-[#f9efe1]">
                    {service.name}
                  </span>
                ))}
                <span className="rounded-full bg-[#d6a65d]/20 px-2.5 py-1 text-xs text-[#7d5724]">Popular</span>
              </div>
              <span className="block w-full rounded-lg bg-[#17130f] px-4 py-2.5 text-center text-sm font-semibold text-[#fff9f0]">
                Book now
              </span>
            </div>
          </Link>
        ))}
      </div>
      <AIChatWidget />
    </main>
  );
}

function normalizeSalonCard(salon) {
  return {
    ...salon,
    salonName: salon.salonName || salon.name,
    name: salon.name || salon.salonName,
    description: salon.description || "",
    area: salon.area || salon.city || "",
    imageUrl: salon.imageUrl || "",
    open: true,
    status: "Open",
    availableSlots: salon.maxBookingCapacity || 5,
    occupiedSlots: 0,
    maxBookingCapacity: salon.maxBookingCapacity || 5,
    estimatedWaitTime: "10-15 min",
    services: salon.services || [],
  };
}

function SalonDetails() {
  const { id } = useParams();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get(`/api/salons/${id}`)
      .then((res) => {
        if (res.data?.salon) {
          setData(res.data);
          return;
        }

        const salon = normalizeSalonCard(res.data || {});
        setData({
          salon,
          services: [],
          availability: salon,
        });
      })
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <main className="mx-auto max-w-7xl px-4 py-6">Loading salon...</main>;

  const salon = data?.salon || {};
  const availability = data?.availability || {};

  return (
    <main className="mx-auto max-w-7xl px-4 pb-24 pt-5">
      <div className="grid gap-6 lg:grid-cols-[1.2fr_.8fr]">
        <section className="overflow-hidden rounded-lg bg-[#fff9f0] shadow-xl shadow-[#2b241e]/10">
          <div className="relative">
            <img
              src={salon.imageUrl || "https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=1200&q=80"}
              alt={salon.salonName}
              className="h-80 w-full object-cover"
            />
            <div className="absolute bottom-4 left-4 rounded-full bg-[#17130f] px-3 py-1.5 text-xs font-semibold text-[#d6a65d]">
              ★ 4.8 customer rating
            </div>
          </div>
          <div className="p-5">
            <div className="flex flex-wrap items-start justify-between gap-3">
              <div>
                <h1 className="text-3xl font-semibold">{salon.salonName}</h1>
                <p className="mt-2 max-w-2xl text-[#6f6254]">{salon.description}</p>
              </div>
              <StatusPill open={availability.open} />
            </div>
            <div className="mt-5 grid gap-3 sm:grid-cols-3">
              <Metric icon={Clock} label="Hours" value={`${salon.openingTime || "09:00"} - ${salon.closingTime || "21:00"}`} />
              <Metric icon={Users} label="Available slots" value={availability.availableSlots ?? 0} />
              <Metric icon={Sparkles} label="Wait time" value={availability.estimatedWaitTime || "10-15 min"} />
            </div>
          </div>
        </section>
        <section className={panel + " p-5"}>
          <h2 className="text-lg font-semibold">Services</h2>
          <div className="mt-4 space-y-3">
            {(data?.services || []).map((service) => (
              <div key={service.id} className="flex items-center justify-between rounded-lg bg-[#f3e7d6] p-3">
                <div>
                  <p className="font-medium">{service.name}</p>
                  <p className="text-sm text-[#6f6254]">{service.duration} min</p>
                </div>
                <p className="font-semibold">₹{service.price}</p>
              </div>
            ))}
          </div>
        </section>
      </div>
      <AIChatWidget salonId={Number(id)} />
    </main>
  );
}

function Metric({ icon: Icon, label, value }) {
  return (
    <div className="rounded-lg bg-[#f3e7d6] p-3">
      <Icon className="h-4 w-4 text-[#b88643]" />
      <p className="mt-2 text-xs text-[#6f6254]">{label}</p>
      <p className="font-semibold">{value}</p>
    </div>
  );
}

function AdminDashboard() {
  const [salons, setSalons] = useState([]);
  const [selectedSalonId, setSelectedSalonId] = useState("");
  const [dashboard, setDashboard] = useState(null);
  const [staff, setStaff] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [form, setForm] = useState({
    salonName: "",
    description: "",
    address: "",
    city: "Bhimtal",
    area: "",
    phone: "",
    openingTime: "09:00",
    closingTime: "21:00",
    maxBookingCapacity: 4,
    imageUrl: "",
  });
  const [staffForm, setStaffForm] = useState({
    name: "",
    role: "",
    phone: "",
    specialization: "",
    startTime: "09:00",
    endTime: "18:00",
  });

  const selectedSalon = useMemo(
    () => salons.find((salon) => String(salon.id) === String(selectedSalonId)),
    [salons, selectedSalonId]
  );

  const loadSalons = () => {
    api.get("/api/salons").then((res) => {
      const list = res.data || [];
      setSalons(list);
      if (!selectedSalonId && list[0]) setSelectedSalonId(list[0].id);
    });
  };

  useEffect(loadSalons, []);

  useEffect(() => {
    if (!selectedSalonId) return;
    api.get(`/api/bookings/dashboard?salonId=${selectedSalonId}`).then((res) => setDashboard(res.data));
    api.get(`/api/staff/salon/${selectedSalonId}`).then((res) => setStaff(res.data || []));
    api.get(`/api/notifications?salonId=${selectedSalonId}`).then((res) => setNotifications(res.data || []));
    const interval = setInterval(() => {
      api.get(`/api/notifications?salonId=${selectedSalonId}`).then((res) => setNotifications(res.data || []));
      api.get(`/api/bookings/dashboard?salonId=${selectedSalonId}`).then((res) => setDashboard(res.data));
    }, 4000);
    return () => clearInterval(interval);
  }, [selectedSalonId]);

  const createSalon = async (event) => {
    event.preventDefault();
    await api.post("/api/salons", form);
    setForm({ ...form, salonName: "", description: "", address: "", area: "", phone: "", imageUrl: "" });
    loadSalons();
  };

  const createStaff = async (event) => {
    event.preventDefault();
    await api.post("/api/staff", { ...staffForm, salonId: Number(selectedSalonId), available: true, attendanceStatus: "PRESENT" });
    setStaffForm({ ...staffForm, name: "", role: "", phone: "", specialization: "" });
    api.get(`/api/staff/salon/${selectedSalonId}`).then((res) => setStaff(res.data || []));
  };

  const markAttendance = async (member, status) => {
    await api.post(`/api/staff/${member.id}/attendance`, { status });
    api.get(`/api/staff/salon/${selectedSalonId}`).then((res) => setStaff(res.data || []));
  };

  return (
    <main className="mx-auto max-w-7xl px-4 py-6">
      <div className="mb-5 flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="text-3xl font-semibold">Manager Dashboard</h1>
          <p className="text-sm text-stone-600">Bookings, capacity, staff attendance, and live booking alerts.</p>
        </div>
        <select className={input + " max-w-xs"} value={selectedSalonId} onChange={(e) => setSelectedSalonId(e.target.value)}>
          {salons.map((salon) => (
            <option key={salon.id} value={salon.id}>{salon.salonName || salon.name}</option>
          ))}
        </select>
      </div>

      <div className="grid gap-4 md:grid-cols-4">
        <Metric icon={CalendarClock} label="Total bookings" value={dashboard?.totalBookings ?? 0} />
        <Metric icon={CheckCircle2} label="Today bookings" value={dashboard?.todayBookings ?? 0} />
        <Metric icon={Users} label="Available staff" value={staff.filter((s) => s.available).length} />
        <Metric icon={Clock} label="Occupied slots" value={dashboard?.occupiedSlots ?? 0} />
      </div>

      <div className="mt-6 grid gap-6 lg:grid-cols-[.9fr_1.1fr]">
        <form onSubmit={createSalon} className={panel + " space-y-3 p-5"}>
          <h2 className="text-lg font-semibold">Register salon</h2>
          <input className={input} placeholder="Salon name" value={form.salonName} onChange={(e) => setForm({ ...form, salonName: e.target.value })} required />
          <textarea className={input} placeholder="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          <div className="grid gap-3 sm:grid-cols-2">
            <input className={input} placeholder="City" value={form.city} onChange={(e) => setForm({ ...form, city: e.target.value })} />
            <input className={input} placeholder="Area" value={form.area} onChange={(e) => setForm({ ...form, area: e.target.value })} />
            <input className={input} placeholder="Phone" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
            <input className={input} type="number" min="1" placeholder="Max capacity" value={form.maxBookingCapacity} onChange={(e) => setForm({ ...form, maxBookingCapacity: Number(e.target.value) })} />
            <input className={input} type="time" value={form.openingTime} onChange={(e) => setForm({ ...form, openingTime: e.target.value })} />
            <input className={input} type="time" value={form.closingTime} onChange={(e) => setForm({ ...form, closingTime: e.target.value })} />
          </div>
          <input className={input} placeholder="Image/logo URL" value={form.imageUrl} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} />
          <input className={input} placeholder="Address" value={form.address} onChange={(e) => setForm({ ...form, address: e.target.value })} />
          <button className="rounded-lg bg-stone-950 px-4 py-2.5 text-sm font-semibold text-white">Save salon</button>
        </form>

        <section className={panel + " p-5"}>
          <div className="flex items-center justify-between gap-3">
            <h2 className="text-lg font-semibold">Staff management</h2>
            <span className="text-sm text-stone-500">{selectedSalon?.salonName}</span>
          </div>
          <form onSubmit={createStaff} className="mt-4 grid gap-3 sm:grid-cols-3">
            <input className={input} placeholder="Name" value={staffForm.name} onChange={(e) => setStaffForm({ ...staffForm, name: e.target.value })} required />
            <input className={input} placeholder="Role" value={staffForm.role} onChange={(e) => setStaffForm({ ...staffForm, role: e.target.value })} />
            <input className={input} placeholder="Specialization" value={staffForm.specialization} onChange={(e) => setStaffForm({ ...staffForm, specialization: e.target.value })} />
            <input className={input} placeholder="Phone" value={staffForm.phone} onChange={(e) => setStaffForm({ ...staffForm, phone: e.target.value })} />
            <input className={input} type="time" value={staffForm.startTime} onChange={(e) => setStaffForm({ ...staffForm, startTime: e.target.value })} />
            <input className={input} type="time" value={staffForm.endTime} onChange={(e) => setStaffForm({ ...staffForm, endTime: e.target.value })} />
            <button className="rounded-lg bg-emerald-700 px-4 py-2 text-sm font-semibold text-white sm:col-span-3">Add staff</button>
          </form>
          <div className="mt-5 space-y-3">
            {staff.map((member) => (
              <div key={member.id} className="flex flex-wrap items-center justify-between gap-3 rounded-lg bg-stone-50 p-3">
                <div>
                  <p className="font-medium">{member.name} <span className="text-sm font-normal text-stone-500">{member.role}</span></p>
                  <p className="text-sm text-stone-500">{member.specialization} · {member.startTime}-{member.endTime}</p>
                </div>
                <div className="flex gap-2">
                  {["PRESENT", "ABSENT", "LEAVE"].map((status) => (
                    <button key={status} onClick={() => markAttendance(member, status)} className={`rounded-lg px-3 py-1.5 text-xs font-semibold ${member.attendanceStatus === status ? "bg-stone-950 text-white" : "bg-white text-stone-700"}`}>
                      {status}
                    </button>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </section>
      </div>

      <div className="mt-6 grid gap-6 lg:grid-cols-2">
        <section className={panel + " p-5"}>
          <h2 className="text-lg font-semibold">Upcoming appointments</h2>
          <div className="mt-4 space-y-3">
            {(dashboard?.upcomingAppointments || []).map((booking) => (
              <div key={booking.id} className="rounded-lg bg-stone-50 p-3 text-sm">
                <p className="font-medium">{booking.customerName} · {booking.service}</p>
                <p className="text-stone-500">{booking.customerPhone} · {booking.bookingDate} at {booking.bookingTime}</p>
              </div>
            ))}
          </div>
        </section>
        <section className={panel + " p-5"}>
          <h2 className="flex items-center gap-2 text-lg font-semibold"><Bell className="h-5 w-5 text-emerald-700" /> Live notifications</h2>
          <div className="mt-4 space-y-3">
            {notifications.map((notification) => (
              <div key={notification.id} className="rounded-lg bg-emerald-50 p-3 text-sm">
                <p className="font-medium text-emerald-900">{notification.title}</p>
                <p className="text-emerald-700">{notification.message}</p>
              </div>
            ))}
          </div>
        </section>
      </div>
    </main>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <div className={shell}>
        <AppNav />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/salons/:id" element={<SalonDetails />} />
          <Route path="/admin" element={<AdminDashboard />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
