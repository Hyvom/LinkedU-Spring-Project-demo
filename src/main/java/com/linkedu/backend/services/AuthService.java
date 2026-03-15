package com.linkedu.backend.services;

import com.linkedu.backend.dto.ContractRegistrationDTO;
import com.linkedu.backend.dto.GuestRegistrationDTO;
import com.linkedu.backend.dto.LoginRequestDTO;
import com.linkedu.backend.entities.EmailVerificationToken;
import com.linkedu.backend.entities.ProductKey;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.dto.UserDTO;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.repositories.EmailVerificationTokenRepository;
import com.linkedu.backend.repositories.ProductKeyRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ProductKeyRepository productKeyRepository;
    private final EmailVerificationTokenRepository emailTokenRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> authenticate(LoginRequestDTO dto) {
        User user = userRepository.findByEmailOrUsername(dto.getIdentifier(), dto.getIdentifier())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }

        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Please verify your email first"));
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole(),
                "message", "Login successful"
        ));
    }

    public ResponseEntity<?> registerWithContract(ContractRegistrationDTO dto) {
        // 1. Validate product key
        ProductKey productKey = productKeyRepository.findByKeyValue(dto.getProductKey())
                .orElseThrow(() -> new RuntimeException("Invalid product key"));

        if (productKey.isUsed()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Product key already used"));
        }

        // 2. Check email
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        // 3. Create & SAVE USER FIRST (disabled until verified)
        User user = new User();
        user.setUsername(dto.getEmail());  // Auto username from email
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(false);  // ← Disabled until email verified!

        user = userRepository.save(user);

        // 4. Link ProductKey
        productKey.setUsed(true);
        productKey.setUser(user);
        productKeyRepository.save(productKey);

        // 5. SEND VERIFICATION EMAIL
        sendVerificationEmail(user);

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "message", "Contract user registered. Check your email for verification link!"
        ));
    }

    public ResponseEntity<?> registerAsGuest(GuestRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent() ||
                userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email or username exists"));
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.GUEST);
        user.setEnabled(false);  // ← Disabled until email verified!

        user = userRepository.save(user);

        // SEND VERIFICATION EMAIL
        sendVerificationEmail(user);

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "message", "Guest user registered. Check your email for verification link!"
        ));
    }

    public ResponseEntity<?> verifyEmail(String token) {
        var emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (emailToken.getVerifiedAt() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already verified"));
        }

        if (emailToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token expired"));
        }

        User user = userRepository.findById(emailToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        emailToken.setVerifiedAt(LocalDateTime.now());
        emailTokenRepository.save(emailToken);

        user.setEnabled(true);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Email verified successfully! You can now login."));
    }

    private void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken emailToken = new EmailVerificationToken(token, user.getId());
        emailTokenRepository.save(emailToken);

        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), user.getFirstName(), verificationUrl);
    }
}
