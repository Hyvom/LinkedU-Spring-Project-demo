package com.linkedu.backend.entities.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "id_card_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdCardDocument extends Document {

    private String numId;
    private String birthday;
}