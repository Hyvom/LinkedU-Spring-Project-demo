package com.linkedu.backend.services;

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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(email, user.getRole().name());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "userId", user.getId(),
                    "role", user.getRole(),
                    "message", "Login successful"
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
    }

    public ResponseEntity<?> register(UserDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already exists"));
        }
        //Validate role existance
        Role role;
        try {
            role = Role.valueOf(dto.getRole().name());  // Convert String → Enum
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role: " + dto.getRole()));
        }
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);

        user = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "message", "User registered successfully"
        ));
    }
}
