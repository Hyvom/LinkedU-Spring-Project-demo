package com.linkedu.backend.controllers;

import com.linkedu.backend.dto.AgentProfileDTO;
import com.linkedu.backend.entities.AgentProfile;
import com.linkedu.backend.services.AgentProfileService;
import com.linkedu.backend.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/agent-profile")
@RequiredArgsConstructor
public class AgentProfileController {

    private final AgentProfileService agentProfileService;
    private final ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<?> createProfile(@RequestBody AgentProfileDTO dto,
                                           Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            AgentProfile profile = agentProfileService.createProfile(userId, dto);
            return ResponseEntity.ok(Map.of("message", "Profile created", "profileId", profile.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody AgentProfileDTO dto,
                                           Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            AgentProfile profile = agentProfileService.updateProfile(userId, dto);
            return ResponseEntity.ok(Map.of("message", "Profile updated", "profileId", profile.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            return ResponseEntity.ok(agentProfileService.getProfile(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfileById(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(agentProfileService.getProfile(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file,
                                          Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            String avatarUrl = imageService.saveAvatar(file);
            AgentProfile profile = agentProfileService.getProfile(userId);
            profile.setAvatar(avatarUrl);
            agentProfileService.saveProfile(profile);
            return ResponseEntity.ok(Map.of("message", "Avatar uploaded", "avatarUrl", avatarUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        String principal = authentication.getName();
        if (principal.startsWith("userId:"))
            return Long.parseLong(principal.split(":")[1]);
        throw new RuntimeException("Invalid authentication principal");
    }
}