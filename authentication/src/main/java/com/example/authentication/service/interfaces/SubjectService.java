package com.example.authentication.service.interfaces;

import java.util.List;

import com.example.authentication.model.Subjects;

public interface SubjectService {
    Subjects createSubject(Subjects subject);
    List<Subjects> getAllSubjects();
    Subjects getSubjectById(Long id);
    Subjects updateSubject(Long id, Subjects subject);
    boolean deleteSubject(Long id);
}
