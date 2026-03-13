package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")  // ← Only ADMIN can assign roles
    public ResponseEntity<?> assignRole(@PathVariable Long userId, @RequestParam String role) {
        userService.assignRole(userId, Role.valueOf(role.toUpperCase()));
        return ResponseEntity.ok(Map.of("message", "Role assigned: " + role));
    }
}
