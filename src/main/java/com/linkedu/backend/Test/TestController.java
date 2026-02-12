package com.linkedu.backend.Test;

import com.linkedu.backend.Entities.User;
import com.linkedu.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/test-users")
    public List<User> test() {
        return userRepo.findAll();
    }
}

