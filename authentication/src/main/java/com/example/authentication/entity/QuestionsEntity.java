package com.example.authentication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "QUESTIONS")
public class QuestionsEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTIONS_ID", nullable = false)
    private Long questionsId;

    @Column(name = "QUESTION", nullable = false)
    private String question;

    @Column(name = "ANSWER")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "SUBJECT_ID", nullable = false)
    private SubjectEntity subject;
}
