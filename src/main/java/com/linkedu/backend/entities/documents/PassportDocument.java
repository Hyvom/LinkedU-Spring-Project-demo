package com.linkedu.backend.entities.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passport_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassportDocument extends Document {

    private String name;
    private String lastName;
    private String issueDate;
    private String expiryDate;
    private String issuingCountry;
}