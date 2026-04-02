package com.linkedu.backend.controllers;

import com.linkedu.backend.dto.AcceptTicketDTO;
import com.linkedu.backend.dto.RejectTicketDTO;
import com.linkedu.backend.entities.Ticket;
import com.linkedu.backend.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agent/tickets")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AGENT')")
public class AgentTicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<?> getMyTickets(Authentication authentication) {
        Long agentId = getCurrentUserId(authentication);
        return ResponseEntity.ok(ticketService.getAgentTickets(agentId));
    }

    // ✅ FIXED - CALLS SERVICE + RETURNS FULL TICKET
    @PostMapping("/{ticketId}/accept")
    public ResponseEntity<?> acceptTicket(@PathVariable Long ticketId,
                                          @RequestBody AcceptTicketDTO dto,
                                          Authentication authentication) {
        Long agentId = getCurrentUserId(authentication);
        Ticket ticket = ticketService.acceptTicket(ticketId, dto, agentId);  // ✅ CALLS SERVICE
        return ResponseEntity.ok(Map.of(
                "message", "Meeting scheduled",
                "meetLink", ticket.getGoogleMeetLink(),
                "ticketId", ticket.getId()
        ));
    }

    @PostMapping("/{ticketId}/reject")
    public ResponseEntity<?> rejectTicket(@PathVariable Long ticketId,
                                          @RequestBody RejectTicketDTO dto,
                                          Authentication authentication) {
        Long agentId = getCurrentUserId(authentication);
        Ticket ticket = ticketService.rejectTicket(ticketId, dto, agentId);
        return ResponseEntity.ok(Map.of("message", "Ticket rejected", "reason", ticket.getRejectionReason()));
    }

    @PostMapping("/{ticketId}/response")
    public ResponseEntity<?> addResponse(@PathVariable Long ticketId,
                                         @RequestBody Map<String, String> body,
                                         Authentication authentication) {
        Long agentId = getCurrentUserId(authentication);
        String responseText = body.get("response");
        ticketService.addResponse(ticketId, responseText, agentId);
        return ResponseEntity.ok(Map.of("message", "Response added"));
    }

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