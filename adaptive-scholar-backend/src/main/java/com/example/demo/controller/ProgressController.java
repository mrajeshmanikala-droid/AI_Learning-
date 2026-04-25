package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.User;
import com.example.demo.model.UserCourseProgress;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserCourseProgressRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:52270"})
public class ProgressController {

    @Autowired
    private UserCourseProgressRepository progressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Get all course progress for the authenticated user.
     * Returns a map of courseId -> progress percentage.
     */
    @GetMapping
    public ResponseEntity<?> getUserProgress(@RequestHeader("Authorization") String authHeader) {
        String email = extractEmail(authHeader);
        if (email == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty()) return ResponseEntity.status(404).body("User not found");

        User user = optUser.get();
        List<UserCourseProgress> progressList = progressRepository.findByUserId(user.getId());

        // Build a response map: courseId -> { progress, completedLessons }
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserCourseProgress p : progressList) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("courseId", p.getCourse().getId());
            entry.put("progress", p.getProgress());
            entry.put("completedLessons", p.getCompletedLessons());
            result.add(entry);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * Update progress for a specific course for the authenticated user.
     */
    @PutMapping("/{courseId}")
    public ResponseEntity<?> updateProgress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long courseId,
            @RequestBody Map<String, Integer> body) {

        String email = extractEmail(authHeader);
        if (email == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty()) return ResponseEntity.status(404).body("User not found");

        Optional<Course> optCourse = courseRepository.findById(courseId);
        if (optCourse.isEmpty()) return ResponseEntity.status(404).body("Course not found");

        User user = optUser.get();
        Course course = optCourse.get();

        int newProgress = body.getOrDefault("progress", 0);
        int completedLessons = body.getOrDefault("completedLessons", 0);

        // Clamp progress between 0 and 100
        newProgress = Math.max(0, Math.min(100, newProgress));

        UserCourseProgress ucp = progressRepository
                .findByUserIdAndCourseId(user.getId(), courseId)
                .orElse(new UserCourseProgress(user, course, 0, 0));

        ucp.setProgress(newProgress);
        ucp.setCompletedLessons(completedLessons);
        progressRepository.save(ucp);

        Map<String, Object> result = new HashMap<>();
        result.put("courseId", courseId);
        result.put("progress", newProgress);
        result.put("completedLessons", completedLessons);

        return ResponseEntity.ok(result);
    }

    private String extractEmail(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                return jwtUtil.getEmailFromToken(authHeader.substring(7));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
