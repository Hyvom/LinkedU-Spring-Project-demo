package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrUsername(String email, String username);
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
    List<User> findByRoleAndEnabled(Role role, boolean enabled);
    List<User> findByAssignedAgent(User agent);
    Optional<User> findByIdAndEnabled(Long id, boolean enabled);

    long countByRole(Role role);
}
