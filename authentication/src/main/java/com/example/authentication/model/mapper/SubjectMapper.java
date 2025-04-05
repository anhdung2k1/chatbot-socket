package com.example.authentication.model.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.authentication.entity.SubjectEntity;
import com.example.authentication.model.Subjects;

@Component
public class SubjectMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public Subjects toDTO(SubjectEntity subject) {
        return modelMapper.map(subject, Subjects.class);
    }
    
    public SubjectEntity toEntity(Subjects subjectDTO) {
        return modelMapper.map(subjectDTO, SubjectEntity.class);
    }
}
