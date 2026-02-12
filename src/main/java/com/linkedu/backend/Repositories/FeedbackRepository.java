package com.linkedu.backend.Repositories;

import com.linkedu.backend.Entities.Feedback;
import com.linkedu.backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByStudent(User student);
    List<Feedback> findByAgent(User agent);
}
