package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.Progress;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.ProgressStage;
import com.linkedu.backend.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByStudent(User student);
    List<Progress> findByStudentAndStage(User student, ProgressStage stage);

    @Query("SELECT p.stage, p.status, COUNT(p) FROM Progress p WHERE p.student.role = :role GROUP BY p.stage, p.status")
    List<Object[]> countByStageAndStatusForRole(@Param("role") Role role);
}
