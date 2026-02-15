package com.linkedu.backend.dto.documentDTO;

import lombok.Data;

@Data
public class PassportFormDTO {
    private Long studentId;
    private String fileName;
    private String filePath;
    private String name;
    private String lastName;
    private String issueDate;
    private String expiryDate;
    private String issuingCountry;
}

