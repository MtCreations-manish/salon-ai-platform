package com.salonai.ai.service;

import com.salonai.ai.dto.AiBookingRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConversationMemoryService {

    // =========================================
    // MEMORY STORE
    // =========================================

    private final Map<String, AiBookingRequest> memoryStore =
            new HashMap<>();

    // =========================================
    // GET MEMORY
    // =========================================

    public AiBookingRequest get(
            String sessionId
    ) {

        return memoryStore.get(
                sessionId
        );
    }

    // =========================================
    // SAVE MEMORY
    // =========================================

    public void save(
            String sessionId,
            AiBookingRequest request
    ) {

        memoryStore.put(
                sessionId,
                request
        );

        System.out.println(
                "MEMORY SAVED : "
                        + sessionId
        );

        System.out.println(
                "STEP : "
                        + request.getCurrentStep()
        );
    }

    // =========================================
    // CLEAR MEMORY
    // =========================================

    public void clear(
            String sessionId
    ) {

        memoryStore.remove(
                sessionId
        );
    }
}