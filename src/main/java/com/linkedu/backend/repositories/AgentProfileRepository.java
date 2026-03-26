package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.AgentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AgentProfileRepository extends JpaRepository<AgentProfile, Long> {
    Optional<AgentProfile> findByUserId(Long userId);
}