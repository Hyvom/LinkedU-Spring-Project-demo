package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.QuestionQuizAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionQuizAssignmentRepository extends JpaRepository<QuestionQuizAssignment, Long> {
    List<QuestionQuizAssignment> findByQuizId(Long quizId);
    List<QuestionQuizAssignment> findByQuestionId(Long questionId);
}
