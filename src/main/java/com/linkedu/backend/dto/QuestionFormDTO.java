package com.linkedu.backend.dto;

import lombok.Data;

@Data
public class QuestionFormDTO {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption; // "A", "B", "C", or "D"
}