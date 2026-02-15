package com.linkedu.backend.services;

import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;

    public User assignAgentToStudent(Long studentId, Long agentId) {
        User student = userRepository.findById(studentId).orElseThrow();
        User agent = userRepository.findById(agentId).orElseThrow();

        if (student.getRole() != Role.STUDENT || agent.getRole() != Role.AGENT) {
            throw new IllegalArgumentException("Invalid roles for assignment");
        }

        student.setAssignedAgent(agent);
        return userRepository.save(student);
    }

    public List<User> getStudentsByAgent(Long agentId) {
        User agent = userRepository.findById(agentId).orElseThrow();
        return userRepository.findByAssignedAgent(agent);
    }
}
