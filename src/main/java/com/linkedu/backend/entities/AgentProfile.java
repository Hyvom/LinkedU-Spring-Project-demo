package com.linkedu.backend.entities;

import com.linkedu.backend.entities.enums.OnlineStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "agent_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String avatar;

    private String address;

    private String phoneNumber;

    private String contactName;      // "contact" field

    private String email;            // agent's contact email (can differ from login email)

    private String availabilityTime; // e.g. "Mon-Fri 9AM-6PM"

    @Enumerated(EnumType.STRING)
    private OnlineStatus onlineStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}