package com.redmopag.documentmanagment.documentservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "document_metadata")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false)
    private String name;

    @Column(nullable = true)
    private String objectKey;

    @Column(nullable = true)
    private String textId;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @Column(nullable = true)
    private String category;

    @Column(nullable = true)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String userName;

    @PrePersist
    public void onCreate() {
        this.uploadedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
