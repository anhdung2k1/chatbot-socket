package com.example.authentication.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import com.example.authentication.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import com.example.authentication.entity.QuestionsEntity;
import com.example.authentication.model.Questions;
import com.example.authentication.model.mapper.QuestionsMapper;
import com.example.authentication.repository.QuestionsRepository;
import com.example.authentication.service.interfaces.QuestionsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class QuestionsServiceImpl implements QuestionsService {

    private final QuestionsRepository questionsRepository;
    private final SubjectRepository subjectRepository;
    private final QuestionsMapper questionsMapper;

    @Override
    public Questions createQuestion(Questions question) {
        log.info("Creating Question: {}", question);
        
        // Convert to Entity
        QuestionsEntity questionEntity = questionsMapper.toEntity(question);
        log.info("createQuestion() question: {}", question);
        // Because the client sent with subjectID not whole subject

        questionsRepository.save(questionEntity);

        // Convert back to DTO
        Questions createdQuestion = questionsMapper.toDTO(questionEntity);
        log.info("Question created successfully: {}", createdQuestion);
        return createdQuestion;
    }

    @Override
    public List<Questions> getAllQuestions(Long subjectId) {
        log.info("Fetching all Questions");
        List<Questions> questions = questionsRepository.findAllQuestionsWithSubjectId(subjectId).stream()
                        .map(questionsMapper::toDTO)
                        .collect(Collectors.toList());
        log.info("Retrieved {} questions", questions.size());
        return questions;
    }

    @Override
    public Questions getQuestionsById(Long id) {
        log.info("Fetching question with ID: {}", id);
        var questionEntity = questionsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Question not found with ID: {}", id);
                    return new RuntimeException("Question not found with ID: " + id);
                });
        
        log.info("Question found: {}", questionEntity);
        return questionsMapper.toDTO(questionEntity);
    }

    @Override
    public Questions updateQuestions(Long id, Questions question) {
        log.info("Updating question with ID: {}", id);
        var questionEntity = questionsRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Question not found with ID: {}", id);
                return new RuntimeException("Question not found with ID: " + id);
            });

        // Update fields in entity from the provided question
        questionEntity.setQuestion(question.getQuestion());
        questionEntity.setAnswer(question.getAnswer());
        
        questionsRepository.save(questionEntity);
        
        // Convert updated entity back to DTO
        Questions updatedQuestion = questionsMapper.toDTO(questionEntity);
        log.info("Question updated successfully: {}", updatedQuestion);
        return updatedQuestion;
    }

    @Override
    public boolean deleteQuestions(Long id) {
        log.info("Deleting question with ID: {}", id);
        var questionEntity = questionsRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Question not found with ID: {}", id);
                return new RuntimeException("Question not found with ID: " + id);
            });
        
        questionsRepository.delete(questionEntity);
        log.info("Question deleted successfully with ID: {}", id);
        return true;
    }
}
