package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.enums.ProgressStage;
import com.linkedu.backend.entities.enums.ProgressStatus;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.repositories.ProgressRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
public class AdminStatisticsController {

    private final UserRepository userRepository;
    private final ProgressRepository progressRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        Map<String, Object> body = new LinkedHashMap<>();

        Map<String, Long> users = new LinkedHashMap<>();
        users.put("total", userRepository.count());
        users.put("students", userRepository.countByRole(Role.STUDENT));
        users.put("agents", userRepository.countByRole(Role.AGENT));
        users.put("admins", userRepository.countByRole(Role.ADMIN));
        users.put("languageTeachers", userRepository.countByRole(Role.LANGUAGE_TEACHER));
        users.put("guests", userRepository.countByRole(Role.GUEST));
        users.put("users", userRepository.countByRole(Role.USER));
        body.put("users", users);

        body.put("studentProgressByStage", buildStudentProgressByStage());
        return ResponseEntity.ok(body);
    }

    private List<Map<String, Object>> buildStudentProgressByStage() {
        EnumMap<ProgressStage, EnumMap<ProgressStatus, Long>> matrix = new EnumMap<>(ProgressStage.class);
        for (ProgressStage stage : ProgressStage.values()) {
            EnumMap<ProgressStatus, Long> row = new EnumMap<>(ProgressStatus.class);
            for (ProgressStatus status : ProgressStatus.values()) {
                row.put(status, 0L);
            }
            matrix.put(stage, row);
        }

        for (Object[] tuple : progressRepository.countByStageAndStatusForRole(Role.STUDENT)) {
            ProgressStage stage = (ProgressStage) tuple[0];
            ProgressStatus status = (ProgressStatus) tuple[1];
            Long count = (Long) tuple[2];
            matrix.get(stage).put(status, count);
        }

        List<Map<String, Object>> out = new ArrayList<>();
        for (ProgressStage stage : ProgressStage.values()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("stage", stage.name());
            EnumMap<ProgressStatus, Long> row = matrix.get(stage);
            m.put("notStarted", row.get(ProgressStatus.NOT_STARTED));
            m.put("inProgress", row.get(ProgressStatus.IN_PROGRESS));
            m.put("completed", row.get(ProgressStatus.COMPLETED));
            out.add(m);
        }
        return out;
    }
}
