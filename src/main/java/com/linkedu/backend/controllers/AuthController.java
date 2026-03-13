package com.linkedu.backend.controllers;

import com.linkedu.backend.dto.*;
import com.linkedu.backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ← FIXED: Use LoginRequestDTO (identifier + password)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        return authService.authenticate(dto);  // Now passes DTO
    }

    @PostMapping("/register/contract")
    public ResponseEntity<?> registerWithContract(@RequestBody ContractRegistrationDTO dto) {
        return authService.registerWithContract(dto);
    }

    @PostMapping("/register/guest")
    public ResponseEntity<?> registerAsGuest(@RequestBody GuestRegistrationDTO dto) {
        return authService.registerAsGuest(dto);
    }

}
