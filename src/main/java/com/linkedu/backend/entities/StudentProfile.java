package com.linkedu.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.linkedu.backend.entities.enums.CollegeType;
import com.linkedu.backend.entities.enums.OnlineStatus;
import com.linkedu.backend.entities.enums.StudyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "student_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String avatar;

    @Enumerated(EnumType.STRING)
    private StudyLevel currentStudyLevel;  // BACHELOR, MASTER, PHD

    @Enumerated(EnumType.STRING)
    private StudyLevel wishedStudyLevel;

    private String speciality;

    private Integer universityYear;  // 1, 2, 3, 4

    // Languages (JSON or separate table - simple JSON for now)
    @Column(columnDefinition = "jsonb")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    private String languages;  // '[{"name":"English","level":"C1","rank":1},...]'

    private Double budget;

    @Enumerated(EnumType.STRING)
    private CollegeType collegeType;  // PUBLIC, PRIVATE

    @Enumerated(EnumType.STRING)
    private OnlineStatus onlineStatus;   // ONLINE / AWAY / OFFLINE

    private String availabilityTime;     // e.g. "9AM-5PM"

    private String address;

    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
