package com.example.authentication.controller;

import com.example.authentication.model.Questions;
import com.example.authentication.service.interfaces.QuestionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authentication/questions")
@RequiredArgsConstructor
public class QuestionsController {

    private final QuestionsService questionsService;

    // Get all questions
    @GetMapping
    public ResponseEntity<List<Questions>> getAllQuestions() {
        return ResponseEntity.ok(questionsService.getAllQuestions());
    }

    // Get question by ID
    @GetMapping("/{id}")
    public ResponseEntity<Questions> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionsService.getQuestionsById(id));
    }

    // Create new question
    @PostMapping
    public ResponseEntity<Questions> createQuestion(@RequestBody Questions question) {
        return ResponseEntity.ok(questionsService.createQuestion(question));
    }

    // Update question by ID
    @PutMapping("/{id}")
    public ResponseEntity<Questions> updateQuestion(@PathVariable Long id, @RequestBody Questions question) {
        return ResponseEntity.ok(questionsService.updateQuestions(id, question));
    }

    // Delete question by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionsService.deleteQuestions(id));
    }
}
