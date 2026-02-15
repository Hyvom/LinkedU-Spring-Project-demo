package com.linkedu.backend.dto.documentDTO;

import lombok.Data;

@Data
public class CoverLetterFormDTO {
    private Long studentId;
    private String fileName;
    private String filePath;
    private String receiver;
    private String hrName;
}
