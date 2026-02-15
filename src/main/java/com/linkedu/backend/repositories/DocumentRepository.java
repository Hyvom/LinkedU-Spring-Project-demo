package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.Document;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByStudent(User student);
    List<Document> findByStudentAndStatus(User student, DocumentStatus status);
}
