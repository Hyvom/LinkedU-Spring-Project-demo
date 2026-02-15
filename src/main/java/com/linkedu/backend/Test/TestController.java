package com.linkedu.backend.Test;

import com.linkedu.backend.dto.UserDTO;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllAgents(); // Test agents only
    }

    @PostMapping("/create-agent")
    public User createTestAgent(@RequestBody UserDTO dto) {
        return userService.createUser(dto.getFirstName(), dto.getLastName(),
                dto.getEmail(), dto.getPassword(), dto.getRole());
    }

    @PostMapping("/create-student")
    public User createTestStudent(@RequestBody UserDTO dto) {
        return userService.createUser(dto.getFirstName(), dto.getLastName(),
                dto.getEmail(), dto.getPassword(), dto.getRole());
    }
}
