package com.redmopag.documentmanagment.documentservice.dto.document;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDetailsResponse {
    private Long id;
    private String name;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private String hocrText;
    private String downloadUrl;
}