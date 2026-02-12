package com.linkedu.backend.Repositories;

import com.linkedu.backend.Entities.Document;
import com.linkedu.backend.Entities.User;
import com.linkedu.backend.Entities.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByStudent(User student);
    List<Document> findByStudentAndStatus(User student, DocumentStatus status);
}
