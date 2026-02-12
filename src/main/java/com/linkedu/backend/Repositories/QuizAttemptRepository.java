package com.linkedu.backend.Repositories;

import com.linkedu.backend.Entities.QuizAttempt;
import com.linkedu.backend.Entities.Quiz;
import com.linkedu.backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByStudent(User student);
    List<QuizAttempt> findByQuiz(Quiz quiz);
    List<QuizAttempt> findByStudentAndQuizOrderByCompletedAtDesc(User student, Quiz quiz);
}
