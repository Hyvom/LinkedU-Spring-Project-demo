package com.linkedu.backend.entities;

import com.linkedu.backend.entities.enums.DocumentStatus;
import com.linkedu.backend.entities.enums.DocumentType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    private String fileName;
    private String filePath;

    //Passport
    private String name;
    private String lastName;
    private String issueDate;
    private String expiryDate;
    private String issuingCountry;

    //ID Card
    private String numId;
    private String birthday;

    //CV
    private String summary;
    private String experince;
    private String projects;
    private String certeficates;
    private String email;
    private String skills;
    private String softSkills;

    //Cover Letter
    private String receiver;
    private String hrName;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status = DocumentStatus.PENDING;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;
}

