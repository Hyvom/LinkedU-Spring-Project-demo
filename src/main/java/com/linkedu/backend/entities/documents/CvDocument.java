package com.linkedu.backend.entities.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cv_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvDocument extends Document {

    private String summary;
    private String experience;
    private String projects;
    private String certificates;
    private String email;
    private String skills;
    private String softSkills;
}