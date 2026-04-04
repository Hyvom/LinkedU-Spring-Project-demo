package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.StudentQuizAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentQuizAssignmentRepository extends JpaRepository<StudentQuizAssignment, Long> {
    List<StudentQuizAssignment> findByStudentId(Long studentId);
    List<StudentQuizAssignment> findByQuizId(Long quizId);
    boolean existsByStudentIdAndQuizId(Long studentId, Long quizId);
}
