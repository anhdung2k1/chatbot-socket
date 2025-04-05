package com.example.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.authentication.entity.QuestionsEntity;

@Repository
public interface QuestionsRepository extends JpaRepository<QuestionsEntity, Long> {
}
