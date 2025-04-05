package com.example.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Questions {
    private Long questionsId;
    private String question;
    private String answer;
    private Subjects subjects;
}
