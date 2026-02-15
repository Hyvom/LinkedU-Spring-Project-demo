package com.linkedu.backend.dto;

import com.linkedu.backend.entities.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
