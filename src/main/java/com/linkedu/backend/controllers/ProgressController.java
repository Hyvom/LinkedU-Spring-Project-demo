package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.Progress;
import com.linkedu.backend.entities.enums.ProgressStage;
import com.linkedu.backend.entities.enums.ProgressStatus;
import com.linkedu.backend.repositories.ProgressRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<Progress> createProgress(
            @RequestParam Long studentId,
            @RequestParam ProgressStage stage) {
        Progress progress = new Progress();
        progress.setStudent(userRepository.findById(studentId).orElseThrow());
        progress.setStage(stage);
        return ResponseEntity.ok(progressRepository.save(progress));
    }

    // READ ALL for student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Progress>> getStudentProgress(@PathVariable Long studentId) {
        return ResponseEntity.ok(progressRepository.findByStudent(
                userRepository.findById(studentId).orElseThrow()));
    }

    // UPDATE status
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Progress> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Progress progress = progressRepository.findById(id).orElseThrow();
        progress.setStatus(ProgressStatus.valueOf(status));
        progress.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(progressRepository.save(progress));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProgress(@PathVariable Long id) {
        progressRepository.deleteById(id);
        return ResponseEntity.ok("Progress deleted");
    }
}
