package com.salonai.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure")
public class TestSecureController {

    @GetMapping
    public String secure(
            Authentication authentication
    ) {

        return "Authenticated User: "
                + authentication.getName();
    }
}