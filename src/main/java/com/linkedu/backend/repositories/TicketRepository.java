package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.Ticket;
import com.linkedu.backend.entities.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStudentIdAndStatus(Long studentId, TicketStatus status);
    List<Ticket> findByStudentId(Long studentId);
    List<Ticket> findByAgentIdAndStatus(Long agentId, TicketStatus status);
    List<Ticket> findByStatus(TicketStatus status);
    Optional<Ticket> findByIdAndAgentId(Long id, Long agentId);
}

