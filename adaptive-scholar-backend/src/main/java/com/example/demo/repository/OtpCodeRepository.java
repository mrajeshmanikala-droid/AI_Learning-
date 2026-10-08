package com.example.demo.repository;

import com.example.demo.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findTopByEmailOrderByExpiresAtDesc(String email);
    void deleteByEmail(String email);
}
