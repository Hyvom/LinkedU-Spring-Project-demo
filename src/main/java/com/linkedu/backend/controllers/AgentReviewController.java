package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.*;
import com.linkedu.backend.entities.enums.AnswerStatus;
import com.linkedu.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agent-review")
@RequiredArgsConstructor
public class AgentReviewController {

    private final QuizAttemptRepository quizAttemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    // AGENT: Get pending quiz attempts
    @GetMapping("/pending")
    public ResponseEntity<List<QuizAttempt>> getPendingReviews(@RequestParam Long agentId) {
        User agent = userRepository.findById(agentId).orElseThrow();
        List<QuizAttempt> attempts = quizAttemptRepository.findAll(); // Filter pending later
        return ResponseEntity.ok(attempts);
    }

    // AGENT: Review quiz attempt (Pass/Fail + Score)
    @PutMapping("/attempt/{attemptId}")
    public ResponseEntity<?> reviewQuizAttempt(
            @PathVariable Long attemptId,
            @RequestParam Long agentId,
            @RequestParam boolean passed,
            @RequestParam Double finalScore) {

        QuizAttempt attempt = quizAttemptRepository.findById(attemptId).orElseThrow();
        attempt.setScore(finalScore);
        attempt.setPassed(passed);
        attempt.setReviewedBy(userRepository.findById(agentId).orElseThrow());

        // Update all answers status
        List<StudentAnswer> answers = studentAnswerRepository.findByQuizAttempt(attempt);
        for (StudentAnswer answer : answers) {
            Question question = answer.getQuestion();
            if (answer.getSelectedOption().equals(question.getCorrectOption())) {
                answer.setStatus(AnswerStatus.CORRECT);
            } else {
                answer.setStatus(AnswerStatus.INCORRECT);
            }
            studentAnswerRepository.save(answer);
        }

        return ResponseEntity.ok(Map.of(
                "message", "Quiz reviewed",
                "passed", passed,
                "finalScore", finalScore
        ));
    }
}
