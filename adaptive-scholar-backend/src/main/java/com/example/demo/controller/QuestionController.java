package com.example.demo.controller;

import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/course/{courseId}")
    public List<Question> getQuestionsForCourse(@PathVariable Long courseId) {
        return questionRepository.findByCourseId(courseId);
    }
}
