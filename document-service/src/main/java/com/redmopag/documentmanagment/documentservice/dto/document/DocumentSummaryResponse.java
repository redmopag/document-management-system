package com.redmopag.documentmanagment.documentservice.dto.document;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSummaryResponse {
    private Long id;
    private String name;
    private LocalDateTime updatedAt;
}