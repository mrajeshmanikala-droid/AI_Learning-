package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private final RestTemplate restTemplate = new RestTemplate();

    // Map storing email -> OTP
    // In production, redis or a database with TTL should be used.
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public String generateAndSendOtp(String toEmail) {
        // Generate a 6 digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Save OTP (Overwrites any previous active OTP for this email)
        otpStorage.put(toEmail, otp);

        // Send Email via Brevo API
        sendBrevoEmail(toEmail, otp);

        return otp; // Return for logging/debugging if needed
    }

    public boolean verifyOtp(String email, String otpCode) {
        String storedOtp = otpStorage.get(email);
        if (storedOtp != null && storedOtp.equals(otpCode)) {
            otpStorage.remove(email); // Clear OTP once used
            return true;
        }
        return false;
    }

    private void sendBrevoEmail(String toEmail, String otpCode) {
        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        Map<String, Object> body = new HashMap<>();
        
        Map<String, String> sender = new HashMap<>();
        sender.put("name", senderName);
        sender.put("email", senderEmail);
        body.put("sender", sender);
        
        body.put("to", List.of(Map.of("email", toEmail)));
        body.put("subject", "Adaptive Scholar - Your Registration OTP");
        
        String htmlContent = "<html><body>" +
                "<h2>Registration OTP Verification</h2>" +
                "<p>Hello Scholar,</p>" +
                "<p>Your One-Time Password (OTP) for completing your registration is:</p>" +
                "<h3 style='font-size: 24px; letter-spacing: 5px; color: #4B41E1;'>" + otpCode + "</h3>" +
                "<p>Please enter this code to verify your email address. It will expire shortly.</p>" +
                "</body></html>";
        body.put("htmlContent", htmlContent);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            if (brevoApiKey != null && !brevoApiKey.startsWith("YOUR_")) {
                restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                System.out.println("OTP email sent successfully to " + toEmail);
            } else {
                throw new Exception("Using placeholder API key.");
            }
        } catch (Exception e) {
            System.err.println("⚠️ MOCK MODE: Email delivery failed. Printing OTP to console for testing:");
            System.out.println("--------------------------------------------------");
            System.out.println("   OTP for " + toEmail + " is: " + otpCode);
            System.out.println("--------------------------------------------------");
        }
    }
}
