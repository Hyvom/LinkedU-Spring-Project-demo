package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.documents.Document;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.DocumentStatus;
import com.linkedu.backend.entities.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByStudent(User student);
    List<Document> findByStudentAndStatus(User student, DocumentStatus status);
    List<Document> findByStudentAndDocumentType(User student, DocumentType type);
}
