package com.linkedu.backend.entities.documents;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "other_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherDocument extends Document {
    private String documentTitle;  // User gives a name
    @Column(columnDefinition = "TEXT")
    private String notes;          // Optional description
}