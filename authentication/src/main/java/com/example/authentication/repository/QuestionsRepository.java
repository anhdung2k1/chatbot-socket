package com.example.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.authentication.entity.QuestionsEntity;
import java.util.List;

@Repository
public interface QuestionsRepository extends JpaRepository<QuestionsEntity, Long> {

    @Query(value = "SELECT q.* FROM questions q " +
            "WHERE q.subject_id =:subjectId", nativeQuery = true)
    List<QuestionsEntity> findAllQuestionsWithSubjectId(Long subjectId);
}
