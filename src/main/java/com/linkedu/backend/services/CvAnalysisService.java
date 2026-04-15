package com.linkedu.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedu.backend.dto.CvAnalysisResponseDTO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CvAnalysisService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    // ── Extract text from uploaded PDF ──
    public String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    // ── Extract text from file path ──
    public String extractTextFromPath(String filePath) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    // ── Analyze CV using Gemini ──
    public CvAnalysisResponseDTO analyzeCv(String cvText) throws IOException {

        String prompt = """
                You are an expert CV/Resume reviewer for a study abroad platform called LinkedU.
                Evaluate the following CV and respond ONLY with a valid JSON object.
                No markdown, no explanation, no extra text — ONLY the JSON.
                
                {
                  "score": <integer 0-100>,
                  "overallFeedback": "<2-3 sentence overall assessment>",
                  "strengths": "<bullet points separated by |>",
                  "weaknesses": "<bullet points separated by |>",
                  "suggestions": "<actionable suggestions separated by |>",
                  "formattingFeedback": "<feedback on CV structure and formatting>",
                  "keywordsFound": "<important keywords found separated by |>",
                  "missingKeywords": "<important missing keywords separated by |>"
                }
                
                Scoring criteria:
                - Contact information (10 pts)
                - Clear objective/summary (10 pts)
                - Education section (15 pts)
                - Work/internship experience (20 pts)
                - Skills relevance (15 pts)
                - Languages (10 pts)
                - Formatting and readability (10 pts)
                - Overall professionalism (10 pts)
                
                CV Content:
                %s
                """.formatted(cvText.length() > 4000 ? cvText.substring(0, 4000) : cvText);

        // Build Gemini request body
        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> parts = Map.of("parts", List.of(textPart));
        Map<String, Object> body = Map.of("contents", List.of(parts));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String urlWithKey = geminiApiUrl + "?key=" + geminiApiKey;

        ResponseEntity<String> response = restTemplate.postForEntity(urlWithKey, request, String.class);

        // Parse Gemini response
        JsonNode root = objectMapper.readTree(response.getBody());
        String content = root
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        // Clean markdown wrappers if present
        content = content.trim();
        if (content.startsWith("```json")) content = content.substring(7);
        if (content.startsWith("```"))     content = content.substring(3);
        if (content.endsWith("```"))       content = content.substring(0, content.length() - 3);
        content = content.trim();

        return objectMapper.readValue(content, CvAnalysisResponseDTO.class);
    }

    // ── Analyze already uploaded CV from file path ──
    public CvAnalysisResponseDTO analyzeCvFromPath(String filePath) throws IOException {
        String cvText = extractTextFromPath(filePath);
        if (cvText == null || cvText.trim().isEmpty()) {
            throw new RuntimeException("Could not extract text from CV. Make sure it is a text-based PDF, not a scanned image.");
        }
        return analyzeCv(cvText);
    }
}