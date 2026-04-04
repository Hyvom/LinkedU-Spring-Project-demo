package com.linkedu.backend.Test;

import com.linkedu.backend.dto.UserDTO;
import com.linkedu.backend.entities.StudentProfile;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.Role;
import com.linkedu.backend.entities.enums.StudyLevel;
import com.linkedu.backend.repositories.UserRepository;
import com.linkedu.backend.repositories.StudentProfileRepository;
import com.linkedu.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllAgents(); // Test agents only
    }

    @PostMapping("/create-agent")
    public User createTestAgent(@RequestBody UserDTO dto) {
        return userService.createUser(dto.getFirstName(), dto.getLastName(),
                dto.getEmail(), dto.getPassword(), dto.getRole());
    }

    @PostMapping("/create-student")
    public User createTestStudent(@RequestBody UserDTO dto) {
        return userService.createUser(dto.getFirstName(), dto.getLastName(),
                dto.getEmail(), dto.getPassword(), dto.getRole());
    }

        @PostMapping("/seed-users")
        public Map<String, Object> seedUsers() {
        Map<String, Object> result = new LinkedHashMap<>();

        User admin = createOrGetUser(
            "admin_test",
            "Admin",
            "Tester",
            "admin.test@linkedu.local",
            "+21611111111",
            "Tunis",
            Role.ADMIN,
            "Admin@123"
        );

        User teacher = createOrGetUser(
            "teacher_test",
            "Language",
            "Teacher",
            "teacher.test@linkedu.local",
            "+21622222222",
            "Sousse",
            Role.LANGUAGE_TEACHER,
            "Teacher@123"
        );

        User student = createOrGetUser(
            "student_test",
            "Student",
            "Tester",
            "student.test@linkedu.local",
            "+21633333333",
            "Sfax",
            Role.STUDENT,
            "Student@123"
        );

        StudentProfile studentProfile = studentProfileRepository.findByUserId(student.getId())
            .orElseGet(StudentProfile::new);
        studentProfile.setUser(student);
        studentProfile.setCurrentStudyLevel(StudyLevel.BACHELOR);
        studentProfile.setWishedStudyLevel(StudyLevel.MASTER);
        studentProfile.setSpeciality("Computer Science");
        studentProfile.setUniversityYear(3);
        studentProfile.setLanguages("[{\"name\":\"French\",\"level\":\"B2\",\"rank\":1},{\"name\":\"English\",\"level\":\"B1\",\"rank\":2}]");
        studentProfile.setAddress("Sfax");
        studentProfile.setPhoneNumber(student.getPhoneNumber());
        studentProfileRepository.save(studentProfile);

        result.put("message", "Seed users ready for testing");
        result.put("admin", Map.of("id", admin.getId(), "email", admin.getEmail(), "password", "Admin@123", "role", admin.getRole()));
        result.put("languageTeacher", Map.of("id", teacher.getId(), "email", teacher.getEmail(), "password", "Teacher@123", "role", teacher.getRole()));
        result.put("student", Map.of("id", student.getId(), "email", student.getEmail(), "password", "Student@123", "role", student.getRole()));
        result.put("studentProfileCreated", true);
        result.put("mailVerificationBypassed", true);
        return result;
        }

        private User createOrGetUser(
            String username,
            String firstName,
            String lastName,
            String email,
            String phone,
            String address,
            Role role,
            String rawPassword
        ) {
        User user = userRepository.findByEmail(email).orElseGet(User::new);

        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setAddress(address);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setEnabled(true);
        user.setEmailVerified(true);

        return userRepository.save(user);
        }
}
