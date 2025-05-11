package com.redmopag.documentmanagment.documentservice.dto.document;

import lombok.*;

import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDetailsResponse {
    private Long id;
    private String name;
    private String category;
    private LocalDate expirationDate;
    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;
    private String hocrText;
    private String downloadUrl;
}