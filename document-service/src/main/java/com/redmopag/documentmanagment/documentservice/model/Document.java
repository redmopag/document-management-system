package com.redmopag.documentmanagment.documentservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String storagePath;
    private String textId;
    private String type;
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;
    private LocalDateTime expirationDate;
    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;

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
