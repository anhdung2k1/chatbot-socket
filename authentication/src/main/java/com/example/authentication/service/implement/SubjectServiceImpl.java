package com.example.authentication.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.authentication.entity.SubjectEntity;
import com.example.authentication.model.Subjects;
import com.example.authentication.model.mapper.SubjectMapper;
import com.example.authentication.repository.SubjectRepository;
import com.example.authentication.service.interfaces.SubjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    public Subjects createSubject(Subjects subject) {
        log.info("Creating Subject: {}", subject);
        
        SubjectEntity subjectEntity = subjectMapper.toEntity(subject);
        subjectRepository.save(subjectEntity);

        Subjects createdSubject = subjectMapper.toDTO(subjectEntity);
        log.info("Subject created successfully: {}", createdSubject);
        return createdSubject;
    }

    @Override
    public List<Subjects> getAllSubjects() {
        log.info("Fetching all Subjects");
        List<Subjects> subjects = subjectRepository.findAll().stream()
                        .map(subjectMapper::toDTO)
                        .collect(Collectors.toList());
        log.info("Retrieved {} subjects", subjects.size());
        return subjects;
    }

    @Override
    public Subjects getSubjectById(Long id) {
        log.info("Fetching subject with ID: {}", id);
        var subjectEntity = subjectRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Subject not found with ID: {}", id);
                    return new RuntimeException("Subject not found with ID: " + id);
                });
        
        log.info("Subject found: {}", subjectEntity);
        return subjectMapper.toDTO(subjectEntity);
    }

    @Override
    public Subjects updateSubject(Long id, Subjects subject) {
        log.info("Updating subject with ID: {}", id);
        var subjectEntity = subjectRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Subject not found with ID: {}", id);
                return new RuntimeException("Subject not found with ID: " + id);
            });
        subject.setSubjectName(subject.getSubjectName());
        subject.setSubjectDescription(subject.getSubjectDescription());
        
        subjectRepository.save(subjectEntity);
        Subjects updatedSubject = subjectMapper.toDTO(subjectEntity);

        log.info("Subject updated successfully: {}", updatedSubject);
        return updatedSubject;
    }

    @Override
    public boolean deleteSubject(Long id) {
        log.info("Deleting subject with ID: {}", id);
        var subjectEntity = subjectRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Subject not found with ID: {}", id);
                return new RuntimeException("Subject not found with ID: " + id);
            });
        subjectRepository.delete(subjectEntity);
        log.info("Subject deleted successfully with ID: {}", id);
        return true;
    }
    
}
