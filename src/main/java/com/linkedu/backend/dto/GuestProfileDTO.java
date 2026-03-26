package com.linkedu.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class GuestProfileDTO {
    private LocalDate dateOfBirth;
    private String bio;
    private String avatar;
    private String onlineStatus;
    private String availabilityTime;
    private String address;
    private String phoneNumber;
}