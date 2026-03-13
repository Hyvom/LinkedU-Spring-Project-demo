package com.linkedu.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_keys", uniqueConstraints = @UniqueConstraint(columnNames = "key_value"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_value", unique = true, nullable = false)
    private String keyValue;  // Unique product key

    private boolean used = false;

    @OneToOne(mappedBy = "productKey")
    private User user;  // Assigned user

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}

