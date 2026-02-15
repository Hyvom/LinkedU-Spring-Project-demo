package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.Feedback;
import com.linkedu.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByStudent(User student);
    List<Feedback> findByAgent(User agent);
}
