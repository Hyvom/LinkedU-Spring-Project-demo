package com.linkedu.backend.entities;

import com.linkedu.backend.entities.enums.OnlineStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "guest_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String avatar;

    @Enumerated(EnumType.STRING)
    private OnlineStatus onlineStatus;

    private String availabilityTime;

    private String address;

    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}