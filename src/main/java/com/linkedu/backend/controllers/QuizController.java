package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.Quiz;
import com.linkedu.backend.entities.StudentProfile;
import com.linkedu.backend.entities.StudentQuizAssignment;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.repositories.QuizRepository;
import com.linkedu.backend.repositories.StudentProfileRepository;
import com.linkedu.backend.repositories.StudentQuizAssignmentRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final StudentQuizAssignmentRepository studentQuizAssignmentRepository;

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String language,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam Long createdById) {
        User creator = userRepository.findById(createdById).orElseThrow();
        if (creator.getRole() != Role.LANGUAGE_TEACHER && creator.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only language teachers or admins can create quizzes");
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setLanguage(language.trim());
        quiz.setStartTime(startTime);
        quiz.setEndTime(endTime);
        quiz.setCreatedBy(creator);
        Quiz savedQuiz = quizRepository.save(quiz);
        autoAssignQuizToMatchingStudents(savedQuiz, creator);
        return ResponseEntity.ok(savedQuiz);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Quiz>> getTeacherQuizzes(@PathVariable Long teacherId) {
        return ResponseEntity.ok(quizRepository.findByCreatedByIdOrderByCreatedAtDesc(teacherId));
    }

    // STUDENT: Get all quizzes
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizRepository.findAll());
    }

    // STUDENT: Get specific quiz
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id) {
        return ResponseEntity.ok(quizRepository.findById(id).orElseThrow());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuiz(
            @PathVariable Long id,
            @RequestParam Long adminId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String language,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime) {
        User admin = userRepository.findById(adminId).orElseThrow();
        if (admin.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only admins can edit quizzes"));
        }

        Quiz quiz = quizRepository.findById(id).orElseThrow();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setLanguage(language.trim());
        quiz.setStartTime(startTime);
        quiz.setEndTime(endTime);
        Quiz savedQuiz = quizRepository.save(quiz);
        autoAssignQuizToMatchingStudents(savedQuiz, admin);
        return ResponseEntity.ok(savedQuiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuiz(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow();
        if (admin.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only admins can delete quizzes"));
        }

        quizRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Quiz deleted"));
    }

    private void autoAssignQuizToMatchingStudents(Quiz quiz, User assignedBy) {
        if (quiz.getLanguage() == null || quiz.getLanguage().isBlank()) {
            return;
        }

        String quizLanguage = quiz.getLanguage().trim().toLowerCase();
        List<StudentProfile> profiles = studentProfileRepository.findAll();

        for (StudentProfile profile : profiles) {
            if (profile.getUser() == null || profile.getUser().getRole() != Role.STUDENT) {
                continue;
            }

            String languages = profile.getLanguages() == null ? "" : profile.getLanguages().toLowerCase();
            if (!languages.contains(quizLanguage)) {
                continue;
            }

            Long studentId = profile.getUser().getId();
            if (studentQuizAssignmentRepository.existsByStudentIdAndQuizId(studentId, quiz.getId())) {
                continue;
            }

            StudentQuizAssignment assignment = new StudentQuizAssignment();
            assignment.setStudent(profile.getUser());
            assignment.setQuiz(quiz);
            assignment.setAssignedBy(assignedBy);
            assignment.setCompleted(false);
            studentQuizAssignmentRepository.save(assignment);
        }
    }
}
