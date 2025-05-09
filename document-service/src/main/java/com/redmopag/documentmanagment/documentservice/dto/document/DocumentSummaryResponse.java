package com.redmopag.documentmanagment.documentservice.dto.document;

import com.redmopag.documentmanagment.documentservice.model.DocumentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSummaryResponse {
    private Long id;
    private String name;
    private LocalDateTime updatedAt;
    private DocumentStatus status;
}