package com.linkedu.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvAnalysisResponseDTO {
    private int score;
    private String overallFeedback;
    private String strengths;
    private String weaknesses;
    private String suggestions;
    private String formattingFeedback;
    private String keywordsFound;
    private String missingKeywords;
}