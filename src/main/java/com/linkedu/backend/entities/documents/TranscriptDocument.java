package com.linkedu.backend.entities.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transcript_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptDocument extends Document {
    private String institution;
    private String academicYear;
    private String average;       // GPA or average grade
}