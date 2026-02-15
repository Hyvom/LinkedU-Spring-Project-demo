package com.linkedu.backend.dto.documentDTO;

import lombok.Data;

@Data
public class CvFormDTO {
    private Long studentId;
    private String fileName;
    private String filePath;
    private String email;
    private String summary;
    private String experience;
    private String projects;
    private String certificates;
    private String skills;
    private String softSkills;
}
