package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.QuizAttempt;
import com.linkedu.backend.repositories.QuizAttemptRepository;
import com.linkedu.backend.repositories.QuizRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz-attempts")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    // STUDENT: Submit quiz attempt
    @PostMapping
    public ResponseEntity<QuizAttempt> submitAttempt(
            @RequestParam Long studentId,
            @RequestParam Long quizId,
            @RequestParam Double score) {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setStudent(userRepository.findById(studentId).orElseThrow());
        attempt.setQuiz(quizRepository.findById(quizId).orElseThrow());
        attempt.setScore(score);
        return ResponseEntity.ok(quizAttemptRepository.save(attempt));
    }

    // STUDENT: Get my quiz history
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<QuizAttempt>> getStudentAttempts(@PathVariable Long studentId) {
        return ResponseEntity.ok(quizAttemptRepository.findByStudent(
                userRepository.findById(studentId).orElseThrow()));
    }
}
