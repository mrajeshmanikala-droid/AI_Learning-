package com.example.demo.controller;

import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:52270"})
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private com.example.demo.security.DataLoader dataLoader;

    @PostMapping("/reseed")
    public Map<String, String> reseed() throws Exception {
        dataLoader.run();
        Map<String, String> res = new HashMap<>();
        res.put("status", "Curriculum and Admin re-seeded successfully");
        return res;
    }

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalLessons", lessonRepository.count());
        
        // Mock data for trends to make the dashboard look "live"
        stats.put("monthlyGrowth", 12.5);
        stats.put("activeLearners", 85);
        
        return stats;
    }
}
