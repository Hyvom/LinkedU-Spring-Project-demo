package com.linkedu.backend.controllers;

import com.linkedu.backend.dto.StudentProfileDTO;
import com.linkedu.backend.entities.StudentProfile;
import com.linkedu.backend.services.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student-profile")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @PostMapping("/create")
    public ResponseEntity<?> createProfile(
            @RequestBody StudentProfileDTO dto,
            Authentication authentication) {
        try {
            // Extract userId from JWT (assuming JWT contains userId)
            Long userId = getUserIdFromAuth(authentication);
            StudentProfile profile = studentProfileService.createProfile(userId, dto);
            return ResponseEntity.ok(Map.of(
                    "message", "Profile created successfully",
                    "profileId", profile.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(
            @RequestBody StudentProfileDTO dto,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            StudentProfile profile = studentProfileService.updateProfile(userId, dto);
            return ResponseEntity.ok(Map.of(
                    "message", "Profile updated successfully",
                    "profileId", profile.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            StudentProfile profile = studentProfileService.getProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfileById(@PathVariable Long userId) {
        try {
            StudentProfile profile = studentProfileService.getProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Extract userId from JWT principal
    private Long getUserIdFromAuth(Authentication authentication) {
        String principal = authentication.getName();  // Gets "userId:123"
        if (principal.startsWith("userId:")) {
            return Long.parseLong(principal.split(":")[1]);
        }
        throw new RuntimeException("Invalid authentication principal");
    }
}
