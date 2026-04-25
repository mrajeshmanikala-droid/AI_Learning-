package com.example.demo.repository;

import com.example.demo.model.UserCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Long> {
    List<UserCourseProgress> findByUserId(Long userId);
    Optional<UserCourseProgress> findByUserIdAndCourseId(Long userId, Long courseId);
}
