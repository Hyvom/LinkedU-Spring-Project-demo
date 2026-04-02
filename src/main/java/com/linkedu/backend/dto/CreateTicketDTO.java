package com.linkedu.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTicketDTO {
    private String object;
    private String description;
    private LocalDateTime availabilityDateTime;
}