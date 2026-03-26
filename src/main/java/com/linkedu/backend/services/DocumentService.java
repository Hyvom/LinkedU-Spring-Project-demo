package com.linkedu.backend.services;

import com.linkedu.backend.dto.documentDTO.DocumentResponseDTO;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.documents.*;
import com.linkedu.backend.entities.enums.DocumentStatus;
import com.linkedu.backend.entities.enums.DocumentType;
import com.linkedu.backend.repositories.DocumentRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/documents/";

    // ================= FILE SAVE =================
    private String saveFile(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    // ================= CV =================
    public CvDocument uploadCv(Long studentId,
                               MultipartFile file,
                               String summary,
                               String experience,
                               String skills) {

        try {
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            CvDocument doc = new CvDocument();
            doc.setStudent(student);
            doc.setDocumentType(DocumentType.CV);
            doc.setFileName(file.getOriginalFilename());
            doc.setFilePath(saveFile(file));
            doc.setSummary(summary);
            doc.setExperience(experience);
            doc.setSkills(skills);
            doc.setUploadedAt(LocalDateTime.now());

            return documentRepository.save(doc);

        } catch (IOException e) {
            throw new RuntimeException("CV upload failed", e);
        }
    }

    // ================= PASSPORT =================
    public PassportDocument uploadPassport(Long studentId,
                                           MultipartFile file,
                                           String issueDate,
                                           String expiryDate,
                                           String issuingCountry) {

        try {
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            PassportDocument doc = new PassportDocument();
            doc.setStudent(student);
            doc.setDocumentType(DocumentType.PASSPORT);
            doc.setFileName(file.getOriginalFilename());
            doc.setFilePath(saveFile(file));
            doc.setIssueDate(issueDate);
            doc.setExpiryDate(expiryDate);
            doc.setIssuingCountry(issuingCountry);
            doc.setUploadedAt(LocalDateTime.now());

            return documentRepository.save(doc);

        } catch (IOException e) {
            throw new RuntimeException("Passport upload failed", e);
        }
    }

    // ================= ID CARD =================
    public IdCardDocument uploadIdCard(Long studentId,
                                       MultipartFile file,
                                       String numId,
                                       String birthday) {

        try {
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            IdCardDocument doc = new IdCardDocument();
            doc.setStudent(student);
            doc.setDocumentType(DocumentType.ID_CARD);
            doc.setFileName(file.getOriginalFilename());
            doc.setFilePath(saveFile(file));
            doc.setNumId(numId);
            doc.setBirthday(birthday);
            doc.setUploadedAt(LocalDateTime.now());

            return documentRepository.save(doc);

        } catch (IOException e) {
            throw new RuntimeException("ID Card upload failed", e);
        }
    }

    // ================= GET DOCUMENTS =================
    public List<DocumentResponseDTO> getStudentDocuments(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return documentRepository.findByStudent(student)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private DocumentResponseDTO toDTO(Document doc) {
        DocumentResponseDTO dto = new DocumentResponseDTO();
        dto.setId(doc.getId());
        dto.setDocumentType(doc.getDocumentType());
        dto.setFileName(doc.getFileName());
        dto.setFilePath(doc.getFilePath());
        dto.setStatus(doc.getStatus());
        dto.setUploadedAt(doc.getUploadedAt());

        if (doc instanceof CvDocument cv) {
            dto.setSummary(cv.getSummary());
            dto.setExperience(cv.getExperience());
            dto.setSkills(cv.getSkills());
        } else if (doc instanceof PassportDocument p) {
            dto.setIssueDate(p.getIssueDate());
            dto.setExpiryDate(p.getExpiryDate());
            dto.setIssuingCountry(p.getIssuingCountry());
        } else if (doc instanceof IdCardDocument id) {
            dto.setNumId(id.getNumId());
            dto.setBirthday(id.getBirthday());
        }

        return dto;
    }

    //================ VERIFY DOCUMENT ==================
    public Document verifyDocument(Long documentId,
                                   Long agentId,
                                   DocumentStatus status) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        User student = document.getStudent();

        if (student.getAssignedAgent() == null) {
            throw new RuntimeException("Student has no assigned agent");
        }

        if (!student.getAssignedAgent().getId().equals(agentId)) {
            throw new RuntimeException("You are not authorized to verify this document");
        }

        if (document.getStatus() != DocumentStatus.PENDING) {
            throw new RuntimeException("Document already verified");
        }

        document.setStatus(status);
        document.setVerifiedBy(student.getAssignedAgent());

        return documentRepository.save(document);
    }
}