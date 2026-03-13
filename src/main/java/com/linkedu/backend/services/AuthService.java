package com.linkedu.backend.services;

import com.linkedu.backend.dto.ContractRegistrationDTO;
import com.linkedu.backend.dto.GuestRegistrationDTO;
import com.linkedu.backend.dto.LoginRequestDTO;
import com.linkedu.backend.entities.ProductKey;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.dto.UserDTO;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.repositories.ProductKeyRepository;  // ← ADD IMPORT
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ProductKeyRepository productKeyRepository;  // ← ADDED
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> authenticate(LoginRequestDTO dto) {
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

        // 3. Create & SAVE USER FIRST
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

        user = userRepository.save(user);  // ← SAVE FIRST!

        // 4. NOW link ProductKey (user has ID)
        productKey.setUsed(true);
        productKey.setUser(user);
        productKeyRepository.save(productKey);  // ← NOW safe!

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "message", "Contract user registered (awaiting admin role assignment)"
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
        user.setBirthDate(LocalDate.parse(dto.getBirthDate()));  // ✅ LocalDate direct
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.GUEST);

        user = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "message", "Guest user registered successfully"
        ));
    }
}
