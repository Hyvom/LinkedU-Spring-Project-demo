package com.linkedu.backend.entities;

import com.linkedu.backend.entities.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String object;  // "Visa Application Issue"

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime availabilityDateTime;  // Student availability

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.PENDING;  // PENDING/ACCEPTED/REJECTED

    @Column(length = 500)
    private String googleMeetLink;  // Agent sets when ACCEPTED

    @Column(length = 500)
    private String rejectionReason;  // Agent sets when REJECTED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private User agent;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}