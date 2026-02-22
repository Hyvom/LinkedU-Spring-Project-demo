package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.documents.Document;
import com.linkedu.backend.services.DocumentService;
import com.linkedu.backend.dto.documentDTO.VerifyDocumentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // ================= CV =================
    @PostMapping(value = "/cv", consumes = "multipart/form-data")
    public ResponseEntity<Document> uploadCv(
            @RequestParam Long studentId,
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String summary,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String skills
    ) {
        return ResponseEntity.ok(
                documentService.uploadCv(studentId, file, summary, experience, skills)
        );
    }

    // ================= PASSPORT =================
    @PostMapping(value = "/passport", consumes = "multipart/form-data")
    public ResponseEntity<Document> uploadPassport(
            @RequestParam Long studentId,
            @RequestParam MultipartFile file,
            @RequestParam String issueDate,
            @RequestParam String expiryDate,
            @RequestParam String issuingCountry
    ) {
        return ResponseEntity.ok(
                documentService.uploadPassport(studentId, file, issueDate, expiryDate, issuingCountry)
        );
    }

    // ================= ID CARD =================
    @PostMapping(value = "/id-card", consumes = "multipart/form-data")
    public ResponseEntity<Document> uploadIdCard(
            @RequestParam Long studentId,
            @RequestParam MultipartFile file,
            @RequestParam String numId,
            @RequestParam String birthday
    ) {
        return ResponseEntity.ok(
                documentService.uploadIdCard(studentId, file, numId, birthday)
        );
    }

    // ================= GET STUDENT DOCUMENTS =================
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Document>> getStudentDocuments(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                documentService.getStudentDocuments(studentId)
        );
    }

    // ======== VERIFY DOCUMENTS BY ASSIGNED AGENT =========
    @PutMapping("/{documentId}/verify")
    public ResponseEntity<Document> verifyDocument(
            @PathVariable Long documentId,
            @RequestParam Long agentId,
            @RequestBody VerifyDocumentRequestDTO request
    ) {

        return ResponseEntity.ok(
                documentService.verifyDocument(
                        documentId,
                        agentId,
                        request.getStatus()
                )
        );
    }
}