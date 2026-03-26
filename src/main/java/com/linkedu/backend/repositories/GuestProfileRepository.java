package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.GuestProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GuestProfileRepository extends JpaRepository<GuestProfile, Long> {
    Optional<GuestProfile> findByUserId(Long userId);
}