package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.*;
import com.linkedu.backend.repositories.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Data
class StudentAnswerDTO {
    private Long questionId;
    private String selectedOption; // "A", "B", "C", "D"
}

@RestController
@RequestMapping("/api/student-quiz")
@RequiredArgsConstructor
public class StudentQuizController {

    private final StudentAnswerRepository studentAnswerRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionQuizAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;  // ← ADD THIS!

    // GET Quiz questions for student
    @GetMapping("/quiz/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuizQuestions(@PathVariable Long quizId) {
        List<QuestionQuizAssignment> assignments = assignmentRepository.findByQuizId(quizId);
        List<Question> questions = assignments.stream()
                .map(assignment -> assignment.getQuestion())
                .toList();
        return ResponseEntity.ok(questions);
    }

    // SUBMIT Quiz answers - FIXED
    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(
            @RequestParam Long studentId,
            @RequestParam Long quizId,
            @RequestBody List<StudentAnswerDTO> answers) {

        // Create QuizAttempt - FIXED LINE 50
        QuizAttempt attempt = new QuizAttempt();
        attempt.setStudent(userRepository.findById(studentId).orElseThrow());
        attempt.setQuiz(quizRepository.findById(quizId).orElseThrow());  // ← FIXED!
        attempt = quizAttemptRepository.save(attempt);

        // Save student answers
        for (StudentAnswerDTO dto : answers) {
            StudentAnswer answer = new StudentAnswer();
            answer.setStudent(userRepository.findById(studentId).orElseThrow());
            answer.setQuestion(questionRepository.findById(dto.getQuestionId()).orElseThrow());
            answer.setSelectedOption(dto.getSelectedOption());
            answer.setQuizAttempt(attempt);
            studentAnswerRepository.save(answer);
        }

        return ResponseEntity.ok(Map.of(
                "quizAttemptId", attempt.getId(),
                "message", "Quiz submitted for review"
        ));
    }
}
