package com.example.demo.service;


import com.example.demo.model.OtpCode;
import com.example.demo.repository.OtpCodeRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class OtpService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final JavaMailSender mailSender;
    private final OtpCodeRepository otpCodeRepository;

    @Value("${app.sender.email}")
    private String senderEmail;

    @Value("${app.sender.name}")
    private String senderName;

    public OtpService(JavaMailSender mailSender, OtpCodeRepository otpCodeRepository) {
        this.mailSender = mailSender;
        this.otpCodeRepository = otpCodeRepository;
    }

    @Transactional
    public boolean generateAndSendOtp(String toEmail) {
        String email = normalizeEmail(toEmail);
        if (email.isEmpty()) {
            return false;
        }

        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));

        otpCodeRepository.deleteByEmail(email);
        OtpCode record = new OtpCode();
        record.setEmail(email);
        record.setCode(otp);
        record.setExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));
        otpCodeRepository.save(record);

        return sendEmail(email, otp);
    }

    public String getStoredOtp(String email) {
        return otpCodeRepository.findTopByEmailOrderByExpiresAtDesc(normalizeEmail(email))
                .map(OtpCode::getCode)
                .orElse(null);
    }

    @Transactional
    public boolean verifyOtp(String email, String otpCode) {
        String normalizedEmail = normalizeEmail(email);
        String submitted = normalizeOtp(otpCode);
        if (normalizedEmail.isEmpty() || submitted.length() != 6) {
            return false;
        }

        OtpCode stored = otpCodeRepository.findTopByEmailOrderByExpiresAtDesc(normalizedEmail).orElse(null);
        if (stored == null) {
            return false;
        }
        if (stored.getExpiresAt().isBefore(Instant.now())) {
            otpCodeRepository.deleteByEmail(normalizedEmail);
            return false;
        }
        if (!stored.getCode().equals(submitted)) {
            return false;
        }

        otpCodeRepository.deleteByEmail(normalizedEmail);
        return true;
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private String normalizeOtp(String otpCode) {
        return otpCode == null ? "" : otpCode.replaceAll("\\D", "");
    }

    private boolean sendEmail(String toEmail, String otpCode) {
        String htmlContent = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 30px;'>" +
                "<div style='max-width: 480px; margin: auto; background: #fff; border-radius: 12px; padding: 32px; box-shadow: 0 4px 20px rgba(0,0,0,0.08);'>" +
                "<h2 style='color: #4B41E1; margin-bottom: 8px;'>The Adaptive Scholar</h2>" +
                "<hr style='border: none; border-top: 2px solid #eee; margin: 16px 0;'/>" +
                "<p style='color: #333;'>Hello Scholar,</p>" +
                "<p style='color: #555;'>Your One-Time Password (OTP) for verification is:</p>" +
                "<div style='text-align: center; margin: 24px 0;'>" +
                "<span style='font-size: 32px; letter-spacing: 8px; color: #4B41E1; font-weight: bold; " +
                "background: #f0efff; padding: 12px 24px; border-radius: 8px; display: inline-block;'>" + otpCode + "</span>" +
                "</div>" +
                "<p style='color: #888; font-size: 13px;'>This code will expire in 10 minutes. Do not share it with anyone.</p>" +
                "<hr style='border: none; border-top: 1px solid #eee; margin: 24px 0;'/>" +
                "<p style='color: #aaa; font-size: 12px; text-align: center;'>&copy; The Adaptive Scholar — AI-Powered Learning Platform</p>" +
                "</div></body></html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail, senderName);
            helper.setTo(toEmail);
            helper.setSubject("Adaptive Scholar - Your Verification OTP");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("✅ OTP email sent successfully to " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("⚠️ Email delivery failed: " + e.getMessage());
            System.out.println("==================================================");
            System.out.println("   📧 EMAIL FAILED — USE THIS OTP MANUALLY");
            System.out.println("   Email: " + toEmail);
            System.out.println("   OTP:   " + otpCode);
            System.out.println("==================================================");
            return false;
        }
    }
}
