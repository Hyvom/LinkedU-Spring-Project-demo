package com.linkedu.backend.entities;

import com.linkedu.backend.entities.enums.AnswerStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String selectedOption; // "A", "B", "C", "D"

    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id")
    private QuizAttempt quizAttempt;

    @Enumerated(EnumType.STRING)
    private AnswerStatus status = AnswerStatus.PENDING; // PENDING, CORRECT, INCORRECT

    private LocalDateTime submittedAt = LocalDateTime.now();
}