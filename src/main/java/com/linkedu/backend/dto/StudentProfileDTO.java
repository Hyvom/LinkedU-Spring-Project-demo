package com.linkedu.backend.dto;

import com.linkedu.backend.entities.enums.StudyLevel;
import com.linkedu.backend.entities.enums.CollegeType;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDTO {

    private LocalDate dateOfBirth;
    private String bio;
    private String avatar;
    private StudyLevel currentStudyLevel;
    private StudyLevel wishedStudyLevel;
    private String speciality;
    private Integer universityYear;
    private String languages;  // JSON: '[{"name":"English","level":"C1","rank":1}]'
    private Double budget;
    private CollegeType collegeType;
    private String onlineStatus;      // "ONLINE" / "AWAY" / "OFFLINE"
    private String availabilityTime;
    private String address;
    private String phoneNumber;

}
