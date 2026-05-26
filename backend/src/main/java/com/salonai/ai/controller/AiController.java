package com.salonai.ai.controller;

import com.salonai.ai.dto.AiBookingRequest;
import com.salonai.ai.service.ConversationMemoryService;
import com.salonai.booking.entity.Booking;
import com.salonai.booking.service.BookingService;
import com.salonai.salon.entity.Salon;
import com.salonai.salon.repository.SalonRepository;
import com.salonai.service.entity.SalonService;
import com.salonai.service.repository.SalonServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    private static final List<String> DEFAULT_CITIES = List.of("Bhimtal");
    private static final List<String> DEFAULT_SERVICES = List.of("Haircut", "Hair Spa", "Facial", "Beard Trim");
    private static final DateTimeFormatter SLOT_FORMATTER = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private SalonServiceRepository salonServiceRepository;

    @Autowired
    private ConversationMemoryService memoryService;

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "").trim();
        String sessionId = request.get("sessionId");
        String salonIdText = request.get("salonId");

        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        AiBookingRequest memory = memoryService.get(sessionId);
        if (memory == null) {
            memory = new AiBookingRequest();
        }

        applySalonContext(memory, salonIdText);
        rememberUsefulDetails(memory, message);

        String lower = message.toLowerCase(Locale.ENGLISH);
        if (isMyBookingIntent(lower)) {
            memoryService.save(sessionId, memory);
            return reply(sessionId, latestBookingReply(sessionId, memory));
        }

        if (isSlotIntent(lower)) {
            if (memory.getSalonId() == null) {
                memory.setCurrentStep("CITY");
                memoryService.save(sessionId, memory);
                return reply(sessionId, "Sure. First choose a city.", DEFAULT_CITIES);
            }
            LocalDate date = memory.getBookingDate() == null ? LocalDate.now() : memory.getBookingDate();
            memory.setBookingDate(date);
            memory.setCurrentStep("SLOT");
            List<String> slots = availableSlotOptions(memory.getSalonId(), date);
            memoryService.save(sessionId, memory);
            return reply(sessionId, slotReply(memory, date, slots), slots);
        }

        if (memory.getCurrentStep() == null || lower.contains("new booking") || lower.equals("start")) {
            return startFlow(sessionId, memory);
        }

        return switch (memory.getCurrentStep()) {
            case "CITY" -> handleCity(sessionId, memory, message);
            case "SALON" -> handleSalon(sessionId, memory, message);
            case "SERVICE" -> handleService(sessionId, memory, message);
            case "DATE" -> handleDate(sessionId, memory, message);
            case "SLOT" -> handleSlot(sessionId, memory, message);
            case "PHONE" -> handlePhone(sessionId, memory, message);
            case "DONE" -> handleDone(sessionId, memory, message);
            default -> startFlow(sessionId, memory);
        };
    }

    private Map<String, Object> startFlow(String sessionId, AiBookingRequest memory) {
        if (memory.getSalonId() == null) {
            memory.setCurrentStep("CITY");
            memoryService.save(sessionId, memory);
            return reply(
                    sessionId,
                    "I can help you book that. Which city should I search in?",
                    DEFAULT_CITIES
            );
        }

        LocalDate date = memory.getBookingDate() == null ? LocalDate.now() : memory.getBookingDate();
        memory.setBookingDate(date);
        memory.setCurrentStep(memory.getServiceName() == null ? "SERVICE" : "SLOT");
        memoryService.save(sessionId, memory);

        if (memory.getServiceName() == null) {
            return reply(
                    sessionId,
                    "Great, " + memory.getSalonName() + " is selected. Which service would you like?",
                    serviceOptions(memory.getSalonId())
            );
        }

        List<String> slots = availableSlotOptions(memory.getSalonId(), date);
        return reply(sessionId, slotReply(memory, date, slots), slots);
    }

    private Map<String, Object> handleCity(String sessionId, AiBookingRequest memory, String message) {
        String city = bestCity(message);
        memory.setCity(city);

        List<Salon> salons = salonsForCity(city);
        if (salons.isEmpty()) {
            memoryService.save(sessionId, memory);
            return reply(sessionId, "I could not find salons in " + city + ". Try another city.", DEFAULT_CITIES);
        }

        memory.setCurrentStep("SALON");
        memoryService.save(sessionId, memory);
        return reply(
                sessionId,
                "Here are the available salons in " + city + ". Please choose one.",
                salonOptions(salons)
        );
    }

    private Map<String, Object> handleSalon(String sessionId, AiBookingRequest memory, String message) {
        Optional<Salon> selectedSalon = selectSalon(memory.getCity(), message);
        if (selectedSalon.isEmpty()) {
            List<Salon> salons = salonsForCity(memory.getCity());
            memoryService.save(sessionId, memory);
            return reply(sessionId, "Please choose a salon from the list.", salonOptions(salons));
        }

        Salon salon = selectedSalon.get();
        memory.setSalonId(salon.getId());
        memory.setSalonName(salon.getSalonName());
        memory.setCity(salon.getCity());

        LocalDate date = memory.getBookingDate() == null ? LocalDate.now() : memory.getBookingDate();
        memory.setBookingDate(date);

        if (memory.getServiceName() == null) {
            memory.setCurrentStep("SERVICE");
            memoryService.save(sessionId, memory);
            return reply(
                    sessionId,
                    salon.getSalonName() + " is selected. Today has these open slots:\n\n"
                            + joinOptions(availableSlotOptions(salon.getId(), date))
                            + "\n\nWhich service would you like?",
                    serviceOptions(salon.getId())
            );
        }

        memory.setCurrentStep("SLOT");
        if (memory.getBookingTime() != null && isSlotAvailable(salon.getId(), date, memory.getBookingTime())) {
            memory.setCurrentStep("PHONE");
            memoryService.save(sessionId, memory);
            return reply(
                    sessionId,
                    salon.getSalonName() + " is selected and " + memory.getBookingTime()
                            + " is available today. Please enter your 10 digit phone number to confirm."
            );
        }

        List<String> slots = availableSlotOptions(salon.getId(), date);
        memoryService.save(sessionId, memory);
        return reply(sessionId, slotReply(memory, date, slots), slots);
    }

    private Map<String, Object> handleService(String sessionId, AiBookingRequest memory, String message) {
        memory.setServiceName(cleanService(message));

        if (memory.getBookingDate() == null) {
            memory.setBookingDate(LocalDate.now());
        }

        memory.setCurrentStep("SLOT");
        if (memory.getBookingTime() != null && isSlotAvailable(memory.getSalonId(), memory.getBookingDate(), memory.getBookingTime())) {
            memory.setCurrentStep("PHONE");
            memoryService.save(sessionId, memory);
            return reply(
                    sessionId,
                    memory.getBookingTime() + " is available for " + memory.getServiceName()
                            + ". Please enter your 10 digit phone number to confirm."
            );
        }

        List<String> slots = availableSlotOptions(memory.getSalonId(), memory.getBookingDate());
        memoryService.save(sessionId, memory);

        return reply(
                sessionId,
                "Perfect. Select a time slot for " + memory.getServiceName() + " on " + memory.getBookingDate() + ".",
                slots
        );
    }

    private Map<String, Object> handleDate(String sessionId, AiBookingRequest memory, String message) {
        memory.setBookingDate(parseDate(message));
        memory.setCurrentStep("SLOT");
        List<String> slots = availableSlotOptions(memory.getSalonId(), memory.getBookingDate());
        memoryService.save(sessionId, memory);
        return reply(sessionId, slotReply(memory, memory.getBookingDate(), slots), slots);
    }

    private Map<String, Object> handleSlot(String sessionId, AiBookingRequest memory, String message) {
        String lower = message.toLowerCase(Locale.ENGLISH);
        if (lower.contains("tomorrow") || lower.contains("tommorow") || lower.contains("today") || looksLikeDate(message)) {
            memory.setBookingDate(parseDate(message));
            List<String> slots = availableSlotOptions(memory.getSalonId(), memory.getBookingDate());
            memoryService.save(sessionId, memory);
            return reply(sessionId, slotReply(memory, memory.getBookingDate(), slots), slots);
        }

        String slot = selectSlot(memory.getSalonId(), memory.getBookingDate(), message);
        if (slot == null) {
            List<String> slots = availableSlotOptions(memory.getSalonId(), memory.getBookingDate());
            return reply(
                    sessionId,
                    "That slot is not available. Choose one of these, or type another date like tomorrow.",
                    slots
            );
        }

        memory.setBookingTime(slot);
        memory.setCurrentStep("PHONE");
        memoryService.save(sessionId, memory);
        return reply(sessionId, "Nice. Please enter your 10 digit phone number to confirm.");
    }

    private Map<String, Object> handlePhone(String sessionId, AiBookingRequest memory, String message) {
        String phone = digitsOnly(message);
        if (phone.length() < 10) {
            memoryService.save(sessionId, memory);
            return reply(sessionId, "Please enter a valid 10 digit phone number.");
        }

        memory.setPhoneNumber(phone.substring(phone.length() - 10));
        Booking booking = bookingService.createBooking(
                sessionId,
                memory.getSalonId(),
                "Guest",
                memory.getPhoneNumber(),
                memory.getServiceName(),
                memory.getBookingDate().toString(),
                memory.getBookingTime()
        );

        memory.setCurrentStep("DONE");
        memoryService.save(sessionId, memory);

        return reply(
                sessionId,
                "✅ Booking confirmed!\n\n"
                        + "City: " + memory.getCity() + "\n"
                        + "Salon: " + memory.getSalonName() + "\n"
                        + "Service: " + memory.getServiceName() + "\n"
                        + "Date: " + memory.getBookingDate() + "\n"
                        + "Time: " + memory.getBookingTime() + "\n"
                        + "Phone: " + memory.getPhoneNumber() + "\n"
                        + "Staff: " + (booking.getAssignedStaff() == null ? "Assigned by salon" : booking.getAssignedStaff().getName())
        );
    }

    private Map<String, Object> handleDone(String sessionId, AiBookingRequest memory, String message) {
        String lower = message.toLowerCase(Locale.ENGLISH);

        if (isMyBookingIntent(lower)) {
            return reply(sessionId, latestBookingReply(sessionId, memory));
        }

        if (isSlotIntent(lower)) {
            LocalDate date = parseDateOrExisting(message, memory.getBookingDate());
            memory.setBookingDate(date);
            memory.setCurrentStep("SLOT");
            List<String> slots = availableSlotOptions(memory.getSalonId(), date);
            memoryService.save(sessionId, memory);
            return reply(sessionId, slotReply(memory, date, slots), slots);
        }

        memory.setCurrentStep("CITY");
        memory.setSalonId(null);
        memory.setSalonName(null);
        memory.setServiceName(null);
        memory.setBookingDate(null);
        memory.setBookingTime(null);
        memory.setPhoneNumber(null);
        memoryService.save(sessionId, memory);
        return reply(sessionId, "Starting a new booking. Which city should I search in?", DEFAULT_CITIES);
    }

    private void applySalonContext(AiBookingRequest memory, String salonIdText) {
        if (salonIdText == null || salonIdText.isBlank()) {
            return;
        }

        try {
            Long salonId = Long.parseLong(salonIdText);
            salonRepository.findById(salonId).ifPresent(salon -> {
                memory.setSalonId(salon.getId());
                memory.setSalonName(salon.getSalonName());
                memory.setCity(salon.getCity());
            });
        } catch (NumberFormatException ignored) {
        }
    }

    private void rememberUsefulDetails(AiBookingRequest memory, String message) {
        if (memory.getServiceName() == null) {
            String service = extractService(message);
            if (service != null) {
                memory.setServiceName(service);
            }
        }

        if (memory.getBookingTime() == null) {
            String time = extractTime(message);
            if (time != null) {
                memory.setBookingTime(time);
            }
        }

        String lower = message.toLowerCase(Locale.ENGLISH);
        if (memory.getBookingDate() == null && (lower.contains("today") || lower.contains("tomorrow") || lower.contains("tommorow") || looksLikeDate(message))) {
            memory.setBookingDate(parseDate(message));
        }
    }

    private List<Salon> salonsForCity(String city) {
        List<Salon> salons = salonRepository.findByCityIgnoreCase(city == null ? "" : city);
        if (salons.isEmpty()) {
            return salonRepository.findAll();
        }
        return salons;
    }

    private Optional<Salon> selectSalon(String city, String message) {
        List<Salon> salons = salonsForCity(city);
        String trimmed = message.trim();

        try {
            Matcher numberMatcher = Pattern.compile("^(\\d+)").matcher(trimmed);
            int number = numberMatcher.find()
                    ? Integer.parseInt(numberMatcher.group(1))
                    : Integer.parseInt(trimmed);
            if (number >= 1 && number <= salons.size()) {
                return Optional.of(salons.get(number - 1));
            }
        } catch (NumberFormatException ignored) {
        }

        String lower = trimmed.toLowerCase(Locale.ENGLISH);
        return salons.stream()
                .filter(salon -> salon.getSalonName() != null && salon.getSalonName().toLowerCase(Locale.ENGLISH).contains(lower))
                .findFirst();
    }

    private List<String> salonOptions(List<Salon> salons) {
        List<String> options = new ArrayList<>();
        for (int i = 0; i < salons.size(); i++) {
            Salon salon = salons.get(i);
            options.add((i + 1) + ". " + salon.getSalonName());
        }
        return options;
    }

    private List<String> serviceOptions(Long salonId) {
        if (salonId == null) {
            return DEFAULT_SERVICES;
        }

        List<String> services = salonServiceRepository.findBySalonId(salonId)
                .stream()
                .map(SalonService::getName)
                .filter(name -> name != null && !name.isBlank())
                .toList();

        return services.isEmpty() ? DEFAULT_SERVICES : services;
    }

    private List<String> availableSlotOptions(Long salonId, LocalDate date) {
        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new RuntimeException("Salon not found"));

        LocalTime open = parseSalonTime(salon.getOpeningTime(), LocalTime.of(10, 0));
        LocalTime close = parseSalonTime(salon.getClosingTime(), LocalTime.of(20, 0));
        List<String> slots = new ArrayList<>();

        LocalTime cursor = open;
        while (cursor.isBefore(close)) {
            String slot = cursor.format(SLOT_FORMATTER);
            int available = bookingService.getAvailableSlots(salonId, date.toString(), slot);
            if (available > 0) {
                slots.add(slot);
            }
            cursor = cursor.plusHours(1);
        }

        return slots;
    }

    private String selectSlot(Long salonId, LocalDate date, String message) {
        List<String> slots = availableSlotOptions(salonId, date);
        String extracted = extractTime(message);
        String lower = message.toLowerCase(Locale.ENGLISH);

        for (String slot : slots) {
            if (slot.equalsIgnoreCase(message.trim()) || slot.equalsIgnoreCase(extracted) || lower.contains(slot.toLowerCase(Locale.ENGLISH))) {
                return slot;
            }
        }

        try {
            int number = Integer.parseInt(message.trim());
            if (number >= 1 && number <= slots.size()) {
                return slots.get(number - 1);
            }
        } catch (NumberFormatException ignored) {
        }

        return null;
    }

    private boolean isSlotAvailable(Long salonId, LocalDate date, String slot) {
        return availableSlotOptions(salonId, date).stream()
                .anyMatch(option -> option.equalsIgnoreCase(slot));
    }

    private String slotReply(AiBookingRequest memory, LocalDate date, List<String> slots) {
        if (slots.isEmpty()) {
            memory.setCurrentStep("DATE");
            return "No slots are available at " + memory.getSalonName()
                    + " on " + date + ". Try tomorrow or another date.";
        }

        return "Available slots at " + memory.getSalonName() + " on " + date + ":\n\n"
                + joinOptions(slots)
                + "\n\nSelect a slot, or type another date.";
    }

    private String latestBookingReply(String sessionId, AiBookingRequest memory) {
        Optional<Booking> bookingOptional = bookingService.getLatestBooking(sessionId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            return "Your latest booking:\n\n"
                    + "Salon: " + (booking.getSalon() == null ? memory.getSalonName() : booking.getSalon().getSalonName()) + "\n"
                    + "Service: " + booking.getService() + "\n"
                    + "Date: " + booking.getBookingDate() + "\n"
                    + "Time: " + booking.getBookingTime() + "\n"
                    + "Phone: " + booking.getCustomerPhone() + "\n"
                    + "Status: " + booking.getStatus();
        }

        if (memory.getBookingDate() != null) {
            return "Your current booking draft is for " + memory.getBookingDate()
                    + (memory.getBookingTime() == null ? "." : " at " + memory.getBookingTime() + ".");
        }

        return "I do not see a booking yet in this chat. Start with your city and I will help book it.";
    }

    private Map<String, Object> reply(String sessionId, String text) {
        return reply(sessionId, text, List.of());
    }

    private Map<String, Object> reply(String sessionId, String text, List<String> options) {
        Map<String, Object> response = new HashMap<>();
        response.put("reply", text);
        response.put("sessionId", sessionId);
        response.put("options", options);
        return response;
    }

    private String joinOptions(List<String> options) {
        if (options.isEmpty()) {
            return "No slots available.";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < options.size(); i++) {
            builder.append(i + 1).append(". ").append(options.get(i));
            if (i < options.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private String bestCity(String message) {
        String lower = message.toLowerCase(Locale.ENGLISH);
        for (String city : DEFAULT_CITIES) {
            if (lower.contains(city.toLowerCase(Locale.ENGLISH))) {
                return city;
            }
        }
        return message.isBlank() ? DEFAULT_CITIES.get(0) : message.trim();
    }

    private String cleanService(String message) {
        String extracted = extractService(message);
        return extracted == null ? message.trim() : extracted;
    }

    private String extractService(String message) {
        String lower = message.toLowerCase(Locale.ENGLISH);
        if (lower.contains("hair cut") || lower.contains("haircut")) return "Haircut";
        if (lower.contains("spa")) return "Hair Spa";
        if (lower.contains("facial")) return "Facial";
        if (lower.contains("beard") || lower.contains("trim")) return "Beard Trim";
        return null;
    }

    private String extractTime(String message) {
        Matcher matcher = Pattern
                .compile("\\b(1[0-2]|0?[1-9])(?::([0-5][0-9]))?\\s*(am|pm)\\b", Pattern.CASE_INSENSITIVE)
                .matcher(message);

        if (!matcher.find()) {
            return null;
        }

        int hour = Integer.parseInt(matcher.group(1));
        int minute = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2));
        String meridiem = matcher.group(3).toUpperCase(Locale.ENGLISH);

        if ("PM".equals(meridiem) && hour < 12) {
            hour += 12;
        }
        if ("AM".equals(meridiem) && hour == 12) {
            hour = 0;
        }

        return LocalTime.of(hour, minute).format(SLOT_FORMATTER);
    }

    private boolean isMyBookingIntent(String lower) {
        return lower.contains("my booking")
                || lower.contains("booking date")
                || lower.contains("which date")
                || lower.contains("when is my booking")
                || lower.contains("show booking");
    }

    private boolean isSlotIntent(String lower) {
        return lower.contains("slot")
                || lower.contains("availability")
                || lower.contains("available time")
                || lower.contains("slots available");
    }

    private LocalDate parseDateOrExisting(String text, LocalDate existing) {
        if (text == null || text.isBlank()) {
            return existing == null ? LocalDate.now() : existing;
        }
        if (text.toLowerCase(Locale.ENGLISH).contains("slot") && !looksLikeDate(text) && !text.toLowerCase(Locale.ENGLISH).contains("tomorrow") && !text.toLowerCase(Locale.ENGLISH).contains("today")) {
            return existing == null ? LocalDate.now() : existing;
        }
        return parseDate(text);
    }

    private LocalDate parseDate(String text) {
        String lower = text == null ? "" : text.toLowerCase(Locale.ENGLISH);

        if (lower.contains("today")) {
            return LocalDate.now();
        }
        if (lower.contains("tomorrow") || lower.contains("tommorow")) {
            return LocalDate.now().plusDays(1);
        }

        Matcher matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(lower);
        if (matcher.find()) {
            return LocalDate.parse(matcher.group());
        }

        return LocalDate.now().plusDays(1);
    }

    private boolean looksLikeDate(String text) {
        return text != null && Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(text).find();
    }

    private LocalTime parseSalonTime(String time, LocalTime fallback) {
        try {
            if (time == null || time.isBlank()) {
                return fallback;
            }
            return LocalTime.parse(time.length() == 4 ? "0" + time : time);
        } catch (Exception e) {
            return fallback;
        }
    }

    private String digitsOnly(String text) {
        return text == null ? "" : text.replaceAll("\\D", "");
    }
}
