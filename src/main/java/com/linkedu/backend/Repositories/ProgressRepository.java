package com.linkedu.backend.Repositories;

import com.linkedu.backend.Entities.Progress;
import com.linkedu.backend.Entities.User;
import com.linkedu.backend.Entities.enums.ProgressStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByStudent(User student);
    List<Progress> findByStudentAndStage(User student, ProgressStage stage);
}
