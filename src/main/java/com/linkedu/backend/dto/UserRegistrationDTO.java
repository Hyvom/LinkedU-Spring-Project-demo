package com.linkedu.backend.dto;

import com.linkedu.backend.entities.enums.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegistrationDTO {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private Role role;
}