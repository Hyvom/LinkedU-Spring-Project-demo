package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.QuizAttempt;
import com.linkedu.backend.entities.Quiz;
import com.linkedu.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByStudent(User student);
    List<QuizAttempt> findByQuiz(Quiz quiz);
    List<QuizAttempt> findByStudentAndQuizOrderByCompletedAtDesc(User student, Quiz quiz);
}
