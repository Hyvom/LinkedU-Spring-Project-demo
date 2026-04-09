package com.linkedu.backend.entities.documents;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cover_letter_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoverLetterDocument extends Document {
    private String targetUniversity;
    private String targetProgram;
    @Column(columnDefinition = "TEXT")
    private String content;       // Optional: paste letter content
}