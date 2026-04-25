package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
