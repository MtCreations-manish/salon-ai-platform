package com.salonai.ai.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonai.ai.dto.AiBookingRequest;
import com.salonai.ai.service.ConversationMemoryService;
import com.salonai.booking.entity.Booking;
import com.salonai.booking.repository.BookingRepository;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ConversationMemoryService memoryService;

    // =========================================================
    // CHAT API
    // =========================================================

    @PostMapping("/chat")
    public Map<String, Object> chat(
            @RequestBody Map<String, String> request
    ) {

        Map<String, Object> response =
                new HashMap<>();

        String message =
                request.get("message");

        String sessionId =
                request.get("sessionId");

        if (message == null) {
            message = "";
        }

        System.out.println("=================================");
        System.out.println("MESSAGE : " + message);
        System.out.println("SESSION : " + sessionId);

        // =========================================
        // SESSION
        // =========================================

        if (sessionId == null || sessionId.isBlank()) {

            sessionId =
                    UUID.randomUUID().toString();

            System.out.println(
                    "NEW SESSION CREATED : "
                            + sessionId
            );
        }

        // =========================================
        // MEMORY
        // =========================================

        AiBookingRequest memory =
                memoryService.get(sessionId);

        if (memory == null) {

            memory =
                    new AiBookingRequest();

            System.out.println(
                    "NEW MEMORY CREATED"
            );

        } else {

            System.out.println(
                    "OLD MEMORY FOUND"
            );

            System.out.println(
                    "CURRENT STEP : "
                            + memory.getCurrentStep()
            );
        }

        // =========================================
        // START FLOW
        // =========================================

        if (memory.getCurrentStep() == null) {

            memory.setCurrentStep("CITY");

            memoryService.save(
                    sessionId,
                    memory
            );

            response.put(
                    "reply",
                    "📍 Which city would you like to book in?\n\nDelhi\nMumbai\nBhimtal"
            );

            response.put(
                    "sessionId",
                    sessionId
            );

            return response;
        }

        // =========================================
        // CITY
        // =========================================

        if (memory.getCurrentStep().equals("CITY")) {

            memory.setCity(message);

            memory.setCurrentStep("SERVICE");

            memoryService.save(
                    sessionId,
                    memory
            );

            response.put(
                    "reply",
                    "💇 Which service would you like?\n\nHaircut\nSpa\nFacial"
            );

            response.put(
                    "sessionId",
                    sessionId
            );

            return response;
        }

        // =========================================
        // SERVICE
        // =========================================

        if (memory.getCurrentStep().equals("SERVICE")) {

            memory.setServiceName(message);

            memory.setCurrentStep("DATE");

            memoryService.save(
                    sessionId,
                    memory
            );

            response.put(
                    "reply",
                    "📅 Which date would you like?\n\nExample:\nTomorrow\n2026-05-25"
            );

            response.put(
                    "sessionId",
                    sessionId
            );

            return response;
        }

        // =========================================
        // DATE
        // =========================================

        if (memory.getCurrentStep().equals("DATE")) {

            memory.setBookingDate(
                    parseDate(message)
            );

            memory.setCurrentStep("TIME");

            memoryService.save(
                    sessionId,
                    memory
            );

            response.put(
                    "reply",
                    "⏰ What time would you prefer?\n\nExample:\n10 AM\n5 PM"
            );

            response.put(
                    "sessionId",
                    sessionId
            );

            return response;
        }

        // =========================================
        // TIME
        // =========================================

        if (memory.getCurrentStep().equals("TIME")) {

            memory.setBookingTime(message);

            memory.setCurrentStep("PHONE");

            memoryService.save(
                    sessionId,
                    memory
            );

            response.put(
                    "reply",
                    "📱 Please enter your 10 digit phone number."
            );

            response.put(
                    "sessionId",
                    sessionId
            );

            return response;
        }

        // =========================================
        // PHONE
        // =========================================

        if (memory.getCurrentStep().equals("PHONE")) {

            memory.setPhoneNumber(message);

            Booking booking =
                    new Booking();

            booking.setCustomerName(
                    "Guest"
            );

            booking.setCustomerPhone(
                    memory.getPhoneNumber()
            );

            booking.setService(
                    memory.getServiceName()
            );

            booking.setBookingDate(
                    memory.getBookingDate()
            );

            booking.setBookingTime(
                    memory.getBookingTime()
            );

            booking.setStatus(
                    "CONFIRMED"
            );

            bookingRepository.save(
                    booking
            );

            memory.setCurrentStep("DONE");

            memoryService.save(
                    sessionId,
                    memory
            );

            response.put(
                    "reply",
                    "✅ Booking Confirmed!\n\n"
                            + "📍 City: " + memory.getCity() + "\n"
                            + "💇 Service: " + memory.getServiceName() + "\n"
                            + "📅 Date: " + memory.getBookingDate() + "\n"
                            + "⏰ Time: " + memory.getBookingTime() + "\n"
                            + "📱 Phone: " + memory.getPhoneNumber()
            );

            response.put(
                    "sessionId",
                    sessionId
            );

            return response;
        }

        // =========================================
        // DONE
        // =========================================

        if (memory.getCurrentStep().equals("DONE")) {

            String lower =
                    message.toLowerCase();

            if (lower.contains("booking")) {

                response.put(
                        "reply",
                        "✅ Your booking is confirmed.\n\n"
                                + "📍 City: " + memory.getCity() + "\n"
                                + "💇 Service: " + memory.getServiceName() + "\n"
                                + "📅 Date: " + memory.getBookingDate() + "\n"
                                + "⏰ Time: " + memory.getBookingTime() + "\n"
                                + "📱 Phone: " + memory.getPhoneNumber()
                );

                response.put(
                        "sessionId",
                        sessionId
                );

                return response;
            }

            // restart flow

            memory = new AiBookingRequest();

            memory.setCurrentStep("CITY");

            memoryService.save(
                    sessionId,
                    memory
            );

            response.put(
                    "reply",
                    "📍 New booking started.\n\nWhich city would you like to book in?\n\nDelhi\nMumbai\nBhimtal"
            );

            response.put(
                    "sessionId",
                    sessionId
            );

            return response;
        }

        // =========================================
        // FALLBACK
        // =========================================

        response.put(
                "reply",
                "Please try again."
        );

        response.put(
                "sessionId",
                sessionId
        );

        return response;
    }

    // =========================================================
    // DATE PARSER
    // =========================================================

    private LocalDate parseDate(
            String text
    ) {

        text =
                text.toLowerCase();

        if (text.contains("tomorrow")) {

            return LocalDate.now().plusDays(1);

        }

        try {

            return LocalDate.parse(text);

        } catch (Exception e) {

            return LocalDate.now().plusDays(1);
        }
    }
}