package com.linkedu.backend.services;

import com.linkedu.backend.dto.LoginRequestDTO;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.dto.UserDTO;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> authenticate(LoginRequestDTO dto) {
        // Login by username OR email
        User user = userRepository.findByEmailOrUsername(dto.getIdentifier(), dto.getIdentifier())
                .orElse(null);

        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "userId", user.getId(),
                    "username", user.getUsername(),
                    "role", user.getRole(),
                    "message", "Login successful"
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
    }

    // ← FIXED: Use your existing UserDTO (complete mapping)
    public ResponseEntity<?> register(UserDTO dto) {
        // Check email uniqueness
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already exists"));
        }

        // Check username uniqueness
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username already exists"));
        }

        // Validate role
        Role role;
        try {
            role = Role.valueOf(dto.getRole().name());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role: " + dto.getRole()));
        }

        // Create complete User from your UserDTO
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getBirthDate());  // Convert String → Date
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());  // Add this field to User.java if missing
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);

        user = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "message", "User registered successfully"
        ));
    }
}

