package com.example.authentication.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SUBJECTS")
public class SubjectEntity {
    public SubjectEntity() {}
    public SubjectEntity(String subjectName, String subjectDescription) {
        this.subjectName = subjectName;
        this.subjectDescription = subjectDescription;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUBJECT_ID", nullable = false)
    private Long subjectId;

    @Column(name = "SUBJECT_NAME", nullable = false)
    private String subjectName;

    @Column(name = "SUBJECT_DESCRIPTION")
    private String subjectDescription;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionsEntity> questions;
}
