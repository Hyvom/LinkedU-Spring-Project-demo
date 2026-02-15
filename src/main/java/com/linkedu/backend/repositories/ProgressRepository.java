package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.Progress;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.ProgressStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByStudent(User student);
    List<Progress> findByStudentAndStage(User student, ProgressStage stage);
}
