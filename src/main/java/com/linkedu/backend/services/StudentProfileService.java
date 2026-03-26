package com.linkedu.backend.services;

import com.linkedu.backend.entities.StudentProfile;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.dto.StudentProfileDTO;
import com.linkedu.backend.entities.enums.OnlineStatus;
import com.linkedu.backend.entities.enums.StudyLevel;
import com.linkedu.backend.entities.enums.CollegeType;
import com.linkedu.backend.repositories.StudentProfileRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfile createProfile(Long userId, StudentProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if profile already exists
        studentProfileRepository.findByUserId(userId).ifPresent(existing -> {
            throw new RuntimeException("Profile already exists");
        });

        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setBio(dto.getBio());
        profile.setAvatar(dto.getAvatar());
        profile.setCurrentStudyLevel(dto.getCurrentStudyLevel());
        profile.setWishedStudyLevel(dto.getWishedStudyLevel());
        profile.setSpeciality(dto.getSpeciality());
        profile.setUniversityYear(dto.getUniversityYear());
        profile.setLanguages(dto.getLanguages());  // JSON string
        profile.setBudget(dto.getBudget());
        profile.setCollegeType(dto.getCollegeType());
        if (dto.getOnlineStatus() != null)
            profile.setOnlineStatus(OnlineStatus.valueOf(dto.getOnlineStatus()));
        profile.setAvailabilityTime(dto.getAvailabilityTime());
        profile.setAddress(dto.getAddress());
        profile.setPhoneNumber(dto.getPhoneNumber());

        return studentProfileRepository.save(profile);
    }

    public StudentProfile getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public StudentProfile updateProfile(Long userId, StudentProfileDTO dto) {
        StudentProfile profile = getProfile(userId);

        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setBio(dto.getBio());
        profile.setAvatar(dto.getAvatar());
        profile.setCurrentStudyLevel(dto.getCurrentStudyLevel());
        profile.setWishedStudyLevel(dto.getWishedStudyLevel());
        profile.setSpeciality(dto.getSpeciality());
        profile.setUniversityYear(dto.getUniversityYear());
        profile.setLanguages(dto.getLanguages());
        profile.setBudget(dto.getBudget());
        profile.setCollegeType(dto.getCollegeType());
        if (dto.getOnlineStatus() != null)
            profile.setOnlineStatus(OnlineStatus.valueOf(dto.getOnlineStatus()));
        profile.setAvailabilityTime(dto.getAvailabilityTime());
        profile.setAddress(dto.getAddress());
        profile.setPhoneNumber(dto.getPhoneNumber());

        return studentProfileRepository.save(profile);
    }

    public StudentProfile saveProfile(StudentProfile profile) {
        return studentProfileRepository.save(profile);
    }
}
