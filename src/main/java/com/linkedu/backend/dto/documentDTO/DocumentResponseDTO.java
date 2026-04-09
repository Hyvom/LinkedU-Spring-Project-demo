package com.linkedu.backend.dto.documentDTO;

import com.linkedu.backend.entities.enums.DocumentStatus;
import com.linkedu.backend.entities.enums.DocumentType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentResponseDTO {
    private Long id;
    private DocumentType documentType;
    private String fileName;
    private String filePath;
    private DocumentStatus status;
    private LocalDateTime uploadedAt;

    // CV fields
    private String summary;
    private String experience;
    private String skills;

    // Passport fields
    private String issueDate;
    private String expiryDate;
    private String issuingCountry;

    // ID Card fields
    private String numId;
    private String birthday;

    // ── Diploma fields ──
    private String degree;
    private String institution;
    private String graduationYear;
    private String fieldOfStudy;

    // ── Transcript fields ──
    private String academicYear;
    private String average;

    // ── Cover Letter fields ──
    private String targetUniversity;
    private String targetProgram;
    private String content;

    // ── Other Document fields ──
    private String documentTitle;
    private String notes;
}