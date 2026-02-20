package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.Question;
import com.linkedu.backend.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuiz(Quiz quiz);
    List<Question> findByCreatedById(Long teacherId);
}
