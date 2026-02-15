package com.linkedu.backend.services;

import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(String firstName, String lastName, String email,
                           String password, Role role) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> getAllAgents() {
        return userRepository.findByRole(Role.AGENT);
    }

    public List<User> getAllStudents() {
        return userRepository.findByRole(Role.STUDENT);
    }
}
