package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.StudentAnswer;
import com.linkedu.backend.entities.QuizAttempt;
import com.linkedu.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByQuizAttempt(QuizAttempt quizAttempt);
    List<StudentAnswer> findByStudent(User student);
}
