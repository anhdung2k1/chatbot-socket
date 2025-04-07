package com.example.authentication.model.mapper;

import com.example.authentication.entity.SubjectEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.authentication.entity.QuestionsEntity;
import com.example.authentication.model.Questions;

@Component
public class QuestionsMapper {
    private final ModelMapper modelMapper = new ModelMapper();
    public Questions toDTO(QuestionsEntity questions) {
        Questions dto = modelMapper.map(questions, Questions.class);
        if (questions.getSubject() != null) {
            dto.setSubjectId(questions.getSubject().getSubjectId());
        }
        return dto;
    }

    public QuestionsEntity toEntity(Questions questionsDTO) {
        QuestionsEntity entity = modelMapper.map(questionsDTO, QuestionsEntity.class);
        if (questionsDTO.getSubjectId() != null) {
            SubjectEntity subject = new SubjectEntity();
            subject.setSubjectId(questionsDTO.getSubjectId());
            entity.setSubject(subject);
        }
        return entity;
    }
}
