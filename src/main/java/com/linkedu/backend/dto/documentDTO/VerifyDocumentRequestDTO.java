package com.linkedu.backend.dto.documentDTO;

import com.linkedu.backend.entities.enums.DocumentStatus;
import lombok.Data;

@Data
public class VerifyDocumentRequestDTO {
    private DocumentStatus status; // APPROVED or REJECTED
}