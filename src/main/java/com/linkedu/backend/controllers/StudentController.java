package com.linkedu.backend.controllers;

import com.linkedu.backend.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/my-students")
    public ResponseEntity<?> getMyStudents(Authentication authentication) {
        String email = authentication.getName();
        // Find agent by email and return their students
        return ResponseEntity.ok("Protected endpoint - logged in as: " + email);
    }

    @PostMapping("/assign-agent")
    public ResponseEntity<?> assignAgent(
            @RequestParam Long studentId,
            @RequestParam Long agentId) {
        return ResponseEntity.ok(studentService.assignAgentToStudent(studentId, agentId));
    }

    @GetMapping("/agent/{agentId}/students")
    public ResponseEntity<?> getAgentStudents(@PathVariable Long agentId) {
        return ResponseEntity.ok(studentService.getStudentsByAgent(agentId));
    }
}
