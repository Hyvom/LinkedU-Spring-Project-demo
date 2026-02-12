package com.linkedu.backend.Repositories;

import com.linkedu.backend.Entities.User;
import com.linkedu.backend.Entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRoleAndEnabled(Role role, boolean enabled);
    Optional<User> findByIdAndEnabled(Long id, boolean enabled);
}
