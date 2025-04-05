package com.example.authentication.service.interfaces;

import java.util.List;

import com.example.authentication.model.Questions;

public interface QuestionsService {
    Questions createQuestion(Questions questions);
    List<Questions> getAllQuestions();
    Questions getQuestionsById(Long id);
    Questions updateQuestions(Long id, Questions questions);
    boolean deleteQuestions(Long id);
}
