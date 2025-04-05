package com.example.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authentication.model.Subjects;
import com.example.authentication.service.interfaces.SubjectService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/authentication/subject")
@RequiredArgsConstructor
public class SubjectController {
    
    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<Subjects>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Subjects> getSubject(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }
    
    @PostMapping
    public ResponseEntity<Subjects> createSubject(@RequestBody Subjects subject) {
        return ResponseEntity.ok(subjectService.createSubject(subject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subjects> updateSubject(@PathVariable Long id, @RequestBody Subjects subject) {
        return ResponseEntity.ok(subjectService.updateSubject(id, subject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteSubject(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.deleteSubject(id));
    }
}
