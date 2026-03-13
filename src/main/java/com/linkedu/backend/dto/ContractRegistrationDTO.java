package com.linkedu.backend.dto;

import lombok.Data;

@Data
public class ContractRegistrationDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private String productKey;  // ← NEW
}

