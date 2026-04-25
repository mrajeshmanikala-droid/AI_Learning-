package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "The Adaptive Scholar - Backend API");
        response.put("status", "RUNNING");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("endpoints", Map.of(
            "courses", "/api/courses",
            "auth_login", "/api/auth/login",
            "auth_register", "/api/auth/register",
            "admin_stats", "/api/admin/stats"
        ));
        return response;
    }
}
