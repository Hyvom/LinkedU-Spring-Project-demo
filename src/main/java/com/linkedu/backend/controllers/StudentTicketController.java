package com.linkedu.backend.controllers;

import com.linkedu.backend.dto.CreateTicketDTO;
import com.linkedu.backend.entities.Ticket;
import com.linkedu.backend.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student/tickets")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentTicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody CreateTicketDTO dto, Authentication authentication) {
        Long studentId = getCurrentUserId(authentication);
        Ticket ticket = ticketService.createTicket(dto, studentId);
        return ResponseEntity.ok(Map.of("ticketId", ticket.getId(), "message", "Submitted to agent"));
    }

    @GetMapping
    public ResponseEntity<?> getMyTickets(Authentication authentication) {
        Long studentId = getCurrentUserId(authentication);
        return ResponseEntity.ok(ticketService.getStudentTickets(studentId));
    }

    // ✅ FIXED - Handles "userId:xx"
    private Long getCurrentUserId(Authentication authentication) {
        String principal = authentication.getName();
        try {
            if (principal != null && principal.contains("userId:")) {
                return Long.parseLong(principal.split(":")[1]);
            }
            return Long.parseLong(principal);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid user ID: " + principal);
        }
    }
}