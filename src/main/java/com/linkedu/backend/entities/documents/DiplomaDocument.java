package com.linkedu.backend.entities.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diploma_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiplomaDocument extends Document {
    private String degree;        // Bachelor, Master, etc.
    private String institution;   // University name
    private String graduationYear;
    private String fieldOfStudy;
}