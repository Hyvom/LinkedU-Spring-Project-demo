package com.linkedu.backend.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String identifier;  // username OR email
    private String password;
}
