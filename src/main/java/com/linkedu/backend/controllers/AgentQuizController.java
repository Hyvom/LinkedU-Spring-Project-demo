package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.QuestionQuizAssignment;
import com.linkedu.backend.entities.Quiz;
import com.linkedu.backend.entities.StudentProfile;
import com.linkedu.backend.entities.StudentQuizAssignment;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.repositories.QuestionQuizAssignmentRepository;
import com.linkedu.backend.repositories.QuestionRepository;
import com.linkedu.backend.repositories.QuizRepository;
import com.linkedu.backend.repositories.StudentProfileRepository;
import com.linkedu.backend.repositories.StudentQuizAssignmentRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz-assignments")
@RequiredArgsConstructor
public class AgentQuizController {

    private final QuestionQuizAssignmentRepository assignmentRepo;
    private final QuestionRepository questionRepo;
    private final QuizRepository quizRepo;
    private final UserRepository userRepo;
    private final StudentQuizAssignmentRepository studentQuizAssignmentRepository;
    private final StudentProfileRepository studentProfileRepository;

    @PostMapping
    public ResponseEntity<?> assignQuestionToQuiz(
            @RequestParam Long questionId,
            @RequestParam Long quizId,
            @RequestParam Long agentId) {
        User agent = userRepo.findById(agentId).orElseThrow();
        if (agent.getRole() != Role.AGENT && agent.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only agent/admin can assign questions"));
        }

        QuestionQuizAssignment assignment = new QuestionQuizAssignment();
        assignment.setQuestion(questionRepo.findById(questionId).orElseThrow());
        assignment.setQuiz(quizRepo.findById(quizId).orElseThrow());
        assignment.setAssignedBy(agent);
        return ResponseEntity.ok(assignmentRepo.save(assignment));
    }

    @PostMapping("/assign-quiz-to-student")
    public ResponseEntity<?> assignQuizToStudent(
            @RequestParam Long quizId,
            @RequestParam Long studentId,
            @RequestParam Long agentId) {
        User agent = userRepo.findById(agentId).orElseThrow();
        User student = userRepo.findById(studentId).orElseThrow();
        Quiz quiz = quizRepo.findById(quizId).orElseThrow();

        if (agent.getRole() != Role.AGENT && agent.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only agent/admin can assign quizzes"));
        }
        if (student.getRole() != Role.STUDENT) {
            return ResponseEntity.badRequest().body(Map.of("error", "Target user is not a student"));
        }
        if (studentQuizAssignmentRepository.existsByStudentIdAndQuizId(studentId, quizId)) {
            return ResponseEntity.ok(Map.of("message", "Quiz already assigned to this student"));
        }

        StudentQuizAssignment assignment = new StudentQuizAssignment();
        assignment.setStudent(student);
        assignment.setQuiz(quiz);
        assignment.setAssignedBy(agent);
        assignment.setCompleted(false);
        return ResponseEntity.ok(studentQuizAssignmentRepository.save(assignment));
    }

    @PostMapping("/auto-assign-by-language")
    public ResponseEntity<?> autoAssignByLanguage(
            @RequestParam Long quizId,
            @RequestParam Long agentId) {
        User agent = userRepo.findById(agentId).orElseThrow();
        if (agent.getRole() != Role.AGENT && agent.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only agent/admin can auto assign quizzes"));
        }

        Quiz quiz = quizRepo.findById(quizId).orElseThrow();
        String quizLanguage = quiz.getLanguage() == null ? "" : quiz.getLanguage().trim();
        if (quizLanguage.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Quiz language is required"));
        }

        List<StudentProfile> profiles = studentProfileRepository.findAll();
        int assignedCount = 0;

        for (StudentProfile profile : profiles) {
            if (profile.getUser() == null || profile.getUser().getRole() != Role.STUDENT) {
                continue;
            }

            String languages = profile.getLanguages() == null ? "" : profile.getLanguages().toLowerCase();
            if (!languages.contains(quizLanguage.toLowerCase())) {
                continue;
            }

            Long studentId = profile.getUser().getId();
            if (studentQuizAssignmentRepository.existsByStudentIdAndQuizId(studentId, quizId)) {
                continue;
            }

            StudentQuizAssignment assignment = new StudentQuizAssignment();
            assignment.setStudent(profile.getUser());
            assignment.setQuiz(quiz);
            assignment.setAssignedBy(agent);
            assignment.setCompleted(false);
            studentQuizAssignmentRepository.save(assignment);
            assignedCount++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Auto assignment completed");
        result.put("quizLanguage", quizLanguage);
        result.put("assignedStudents", assignedCount);
        return ResponseEntity.ok(result);
    }
}
