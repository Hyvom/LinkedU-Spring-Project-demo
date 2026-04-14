package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.*;
import com.linkedu.backend.entities.enums.AnswerStatus;
import com.linkedu.backend.repositories.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private final QuizRepository quizRepository;
    private final StudentQuizAssignmentRepository studentQuizAssignmentRepository;
    private final StudentProfileRepository studentProfileRepository;

    // GET Quiz questions for student
    @GetMapping("/quiz/{quizId}/questions")
    public ResponseEntity<?> getQuizQuestions(
            @PathVariable Long quizId,
            @RequestParam Long studentId) {
        if (!canStudentAccessQuiz(studentId, quizId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Quiz is not assigned to this student"));
        }

        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        if (!isWithinQuizWindow(quiz)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Quiz is not available at this time"));
        }

        List<QuestionQuizAssignment> assignments = assignmentRepository.findByQuizId(quizId);
        List<Question> questions = assignments.stream()
                .map(assignment -> assignment.getQuestion())
                .toList();

        if (questions.isEmpty()) {
            questions = questionRepository.findByQuizId(quizId);
        }

        return ResponseEntity.ok(questions);
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<Quiz>> getAssignedQuizzes(@RequestParam Long studentId) {
        Set<Long> quizIds = new HashSet<>();

        List<Quiz> quizzes = studentQuizAssignmentRepository.findByStudentId(studentId)
                .stream()
                .map(StudentQuizAssignment::getQuiz)
            .filter(quiz -> quizIds.add(quiz.getId()))
            .toList();

        List<Quiz> matchingQuizzes = quizRepository.findAll().stream()
            .filter(quiz -> matchesStudentLanguage(studentId, quiz))
            .filter(quiz -> quizIds.add(quiz.getId()))
            .toList();

        List<Quiz> allVisibleQuizzes = new ArrayList<>(quizzes);
        allVisibleQuizzes.addAll(matchingQuizzes);
        return ResponseEntity.ok(allVisibleQuizzes);
    }

    // SUBMIT Quiz answers - FIXED
    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(
            @RequestParam Long studentId,
            @RequestParam Long quizId,
            @RequestParam(required = false, defaultValue = "false") boolean auto,
            @RequestBody List<StudentAnswerDTO> answers) {
        if (!canStudentAccessQuiz(studentId, quizId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Quiz is not assigned to this student"));
        }

        User student = userRepository.findById(studentId).orElseThrow();
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();

        if (!isWithinQuizWindow(quiz) && !auto) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Quiz deadline has passed"));
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setStudent(student);
        attempt.setQuiz(quiz);
        attempt.setCompletedAt(LocalDateTime.now());
        attempt = quizAttemptRepository.save(attempt);

        int correct = 0;
        for (StudentAnswerDTO dto : answers) {
            Question question = questionRepository.findById(dto.getQuestionId()).orElseThrow();
            StudentAnswer answer = new StudentAnswer();
            answer.setStudent(student);
            answer.setQuestion(question);
            answer.setSelectedOption(dto.getSelectedOption());
            answer.setQuizAttempt(attempt);

            if (dto.getSelectedOption() != null && dto.getSelectedOption().equalsIgnoreCase(question.getCorrectOption())) {
                answer.setStatus(AnswerStatus.CORRECT);
                correct++;
            } else {
                answer.setStatus(AnswerStatus.INCORRECT);
            }

            studentAnswerRepository.save(answer);
        }

        double score = answers.isEmpty() ? 0.0 : (correct * 100.0 / answers.size());
        attempt.setScore(score);
        attempt.setPassed(score >= 60.0);
        quizAttemptRepository.save(attempt);

        studentQuizAssignmentRepository.findByStudentId(studentId).stream()
                .filter(a -> a.getQuiz().getId().equals(quizId))
                .findFirst()
                .ifPresent(a -> {
                    a.setCompleted(true);
                    studentQuizAssignmentRepository.save(a);
                });

        return ResponseEntity.ok(Map.of(
                "quizAttemptId", attempt.getId(),
                "score", score,
                "passed", attempt.getPassed(),
                "message", "Quiz submitted and scored"
        ));
    }

    private boolean isWithinQuizWindow(Quiz quiz) {
        if (quiz == null) return true;
        if (quiz.getStartTime() == null || quiz.getEndTime() == null) return true; // no schedule => always available
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(quiz.getStartTime()) && !now.isAfter(quiz.getEndTime());
    }

    private boolean canStudentAccessQuiz(Long studentId, Long quizId) {
        if (studentQuizAssignmentRepository.existsByStudentIdAndQuizId(studentId, quizId)) {
            return true;
        }

        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) {
            return false;
        }

        return matchesStudentLanguage(studentId, quiz);
    }

    private boolean matchesStudentLanguage(Long studentId, Quiz quiz) {
        if (quiz.getLanguage() == null || quiz.getLanguage().isBlank()) {
            return false;
        }

        StudentProfile profile = studentProfileRepository.findByUserId(studentId).orElse(null);
        if (profile == null || profile.getLanguages() == null) {
            return false;
        }

        return profile.getLanguages().toLowerCase().contains(quiz.getLanguage().trim().toLowerCase());
    }
}
