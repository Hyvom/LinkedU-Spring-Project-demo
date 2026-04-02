package com.linkedu.backend.services;

import com.linkedu.backend.dto.AcceptTicketDTO;
import com.linkedu.backend.dto.CreateTicketDTO;
import com.linkedu.backend.dto.RejectTicketDTO;
import com.linkedu.backend.entities.Ticket;
import com.linkedu.backend.entities.TicketResponse;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.TicketStatus;
import com.linkedu.backend.repositories.TicketRepository;
import com.linkedu.backend.repositories.TicketResponseRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketResponseRepository responseRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Student: Create ticket
    public Ticket createTicket(CreateTicketDTO dto, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Auto-assign to student's agent
        User agent = student.getAssignedAgent();
        if (agent == null) {
            throw new RuntimeException("No assigned agent found");
        }

        Ticket ticket = new Ticket();
        ticket.setObject(dto.getObject());
        ticket.setDescription(dto.getDescription());
        ticket.setAvailabilityDateTime(dto.getAvailabilityDateTime());
        ticket.setStudent(student);
        ticket.setAgent(agent);

        Ticket saved = ticketRepository.save(ticket);

        // Notify agent via email
        emailService.sendTicketNotification(agent.getEmail(), student.getFirstName(), saved.getId());
        return saved;
    }

    // Agent: Accept ticket + set Google Meet
    public Ticket acceptTicket(Long ticketId, AcceptTicketDTO dto, Long agentId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // ✅ VALIDATION
        if (!ticket.getAgent().getId().equals(agentId)) {
            throw new RuntimeException("Not your ticket");
        }

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Ticket not pending");
        }

        // ✅ UPDATE TICKET
        ticket.setStatus(TicketStatus.ACCEPTED);
        ticket.setGoogleMeetLink(dto.getGoogleMeetLink());
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);

        // ✅ SEND EMAIL TO STUDENT (THIS WAS MISSING!)
        emailService.sendMeetLink(
                ticket.getStudent().getEmail(),
                ticket.getAgent().getFirstName(),
                dto.getGoogleMeetLink(),
                ticket.getAvailabilityDateTime()
        );

        System.out.println("✅ Meeting scheduled! Email sent to: " + ticket.getStudent().getEmail());
        return saved;
    }

    // Agent: Reject ticket
    public Ticket rejectTicket(Long ticketId, RejectTicketDTO dto, Long agentId) {
        Ticket ticket = getTicketByAgent(ticketId, agentId);
        ticket.setStatus(TicketStatus.REJECTED);
        ticket.setRejectionReason(dto.getReason());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    // Add response
    public TicketResponse addResponse(Long ticketId, String responseText, Long agentId) {
        Ticket ticket = getTicketByAgent(ticketId, agentId);
        TicketResponse response = new TicketResponse();
        response.setTicket(ticket);
        response.setAgent(ticket.getAgent());
        response.setResponseText(responseText);
        return responseRepository.save(response);
    }

public List<Ticket> getStudentTickets(Long studentId) {
        return ticketRepository.findByStudentId(studentId);
    }

    public List<Ticket> getAgentTickets(Long agentId) {
        return ticketRepository.findByAgentIdAndStatus(agentId, TicketStatus.PENDING);
    }

    private Ticket getTicketByAgent(Long ticketId, Long agentId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        if (!ticket.getAgent().getId().equals(agentId)) {
            throw new RuntimeException("Not authorized");
        }
        return ticket;
    }
}