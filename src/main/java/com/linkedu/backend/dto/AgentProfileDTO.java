package com.linkedu.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AgentProfileDTO {
    private LocalDate dateOfBirth;
    private String bio;
    private String avatar;
    private String address;
    private String phoneNumber;
    private String contactName;
    private String email;
    private String availabilityTime;
    private String onlineStatus;
}