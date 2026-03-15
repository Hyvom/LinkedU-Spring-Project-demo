package com.linkedu.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkedu.backend.entities.documents.Document;
import com.linkedu.backend.entities.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true, nullable =false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column (name="birthDate")
    private LocalDate birthDate;

    @Column(unique = true, nullable = false)
    private String email;

    @Column (nullable = false)
    private String phoneNumber;

    @Column (nullable = true)
    private String address;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_key_id")
    private ProductKey productKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 30, nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean enabled = false;

    // Self-referencing relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_agent_id")
    private User assignedAgent;

    @OneToMany(mappedBy = "assignedAgent", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> assignedStudents = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private StudentProfile studentProfile;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Progress> progressList = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Document> documents = new ArrayList<>();

    @Column(nullable = false)
    private boolean emailVerified = false;

}

