package com.example.authentication.model.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.authentication.entity.QuestionsEntity;
import com.example.authentication.model.Questions;

@Component
public class QuestionsMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public Questions toDTO(QuestionsEntity questions) {
        return modelMapper.map(questions, Questions.class);
    }

    public QuestionsEntity toEntity(Questions questionsDTO) {
        return modelMapper.map(questionsDTO, QuestionsEntity.class);
    }
}
